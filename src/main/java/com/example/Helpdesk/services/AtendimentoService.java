package com.example.Helpdesk.services;

import com.example.Helpdesk.dto.AtendimentoRequestDto;
import com.example.Helpdesk.dto.AtendimentoResponseDto;
import com.example.Helpdesk.model.AtendimentoModel;
import com.example.Helpdesk.model.ChamadoModel;
import com.example.Helpdesk.model.ChamadosEnum.NivelSuporte;
import com.example.Helpdesk.model.ChamadosEnum.PerfilUsuario;
import com.example.Helpdesk.model.UsuarioModel;
import com.example.Helpdesk.repository.AtendimentoRepository;
import com.example.Helpdesk.repository.ChamadoRepository;
import com.example.Helpdesk.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;

    public AtendimentoService(AtendimentoRepository atendimentoRepository,
                              ChamadoRepository chamadoRepository,
                              UsuarioRepository usuarioRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public AtendimentoResponseDto registrarAtendimento(AtendimentoRequestDto dto) {
        ChamadoModel chamado = chamadoRepository.findById(dto.getChamadoId())
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado com o ID: " + dto.getChamadoId()));

        UsuarioModel tecnico = usuarioRepository.findById(dto.getTecnicoId())
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado com o ID: " + dto.getTecnicoId()));

        validarPermissoes(tecnico, chamado, dto.getNivelSuporte());

        AtendimentoModel atendimento = new AtendimentoModel();
        atendimento.setChamado(chamado);
        atendimento.setTecnico(tecnico);
        atendimento.setObservacao(dto.getObservacao());
        atendimento.setPrioridade(dto.getPrioridade());
        atendimento.setStatus(dto.getStatus());
        atendimento.setNivelSuporte(dto.getNivelSuporte());

        AtendimentoModel salvo = atendimentoRepository.save(atendimento);

        chamado.setPrioridade(dto.getPrioridade());
        chamado.setStatus(dto.getStatus());
        chamado.setNivelAtual(dto.getNivelSuporte());
        chamado.setTecnicoAtribuido(tecnico);
        chamadoRepository.save(chamado);

        return converterParaDto(salvo);
    }

    public List<AtendimentoResponseDto> listarPorChamado(Long chamadoId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isTecnicoOuAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TECNICO") || a.getAuthority().equals("ROLE_ADMIN"));

        return atendimentoRepository.findByChamadoId(chamadoId).stream()
                .map(atendimento -> isTecnicoOuAdmin ? new AtendimentoResponseDto(atendimento) : AtendimentoResponseDto.paraCliente(atendimento))
                .collect(Collectors.toList());
    }

    public List<AtendimentoResponseDto> listarTodos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isTecnicoOuAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TECNICO") || a.getAuthority().equals("ROLE_ADMIN"));

        List<AtendimentoModel> atendimentos = atendimentoRepository.findAll();

        if (isTecnicoOuAdmin) {
            return atendimentos.stream()
                    .map(AtendimentoResponseDto::new)
                    .toList();
        } else {
            return atendimentos.stream()
                    .map(AtendimentoResponseDto::paraCliente)
                    .toList();
        }
    }

    private void validarPermissoes(UsuarioModel tecnico, ChamadoModel chamado, NivelSuporte novoNivel) {
        if (tecnico.getPerfil() == PerfilUsuario.ADMIN) {
            return;
        }
        if (tecnico.getPerfil() == PerfilUsuario.CLIENTE) {
            throw new RuntimeException("Permissão negada: Você não tem autorização para atender chamados");
        }
        if (tecnico.getPerfil() == PerfilUsuario.TECNICO_N1 && chamado.getNivelAtual() != NivelSuporte.N1) {
            throw new RuntimeException("Permissão negada: Técnico N1 não tem autorização para alterar chamados de nível " + chamado.getNivelAtual());
        }
        if (tecnico.getPerfil() == PerfilUsuario.TECNICO_N2 && chamado.getNivelAtual() == NivelSuporte.N3) {
            throw new RuntimeException("Permissão negada: Técnico N2 não tem acesso a chamados do N3");
        }
        if (tecnico.getPerfil() == PerfilUsuario.TECNICO_N1 && novoNivel == NivelSuporte.N3) {
            throw new RuntimeException("Permissão negada: Técnico N1 não pode transferir chamados diretamente para o N3");
        }
    }

    private AtendimentoResponseDto converterParaDto(AtendimentoModel atendimento) {
        return new AtendimentoResponseDto(atendimento);
    }
}