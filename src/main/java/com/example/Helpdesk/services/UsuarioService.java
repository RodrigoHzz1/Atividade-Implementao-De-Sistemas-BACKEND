package com.example.Helpdesk.services;

import com.example.Helpdesk.dto.UsuarioResponseDto;
import com.example.Helpdesk.dto.UsuarioResquestDto;
import com.example.Helpdesk.model.ChamadoModel;
import com.example.Helpdesk.model.ChamadosEnum.PerfilUsuario;
import com.example.Helpdesk.model.UsuarioModel;
import com.example.Helpdesk.repository.AtendimentoRepository;
import com.example.Helpdesk.repository.ChamadoRepository;
import com.example.Helpdesk.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.Helpdesk.model.ChamadosEnum.PerfilUsuario;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela lógica de negócio relacionada aos usuários.
 * Centraliza criação, atualização, listagem, exclusão e alteração de perfil.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ChamadoRepository chamadoRepository;
    private final AtendimentoRepository atendimentoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                           ChamadoRepository chamadoRepository, AtendimentoRepository atendimentoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.chamadoRepository = chamadoRepository;
        this.atendimentoRepository = atendimentoRepository;
    }

    public UsuarioResponseDto criar(UsuarioResquestDto dto) {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        // Define CLIENTE caso o perfil venha nulo no DTO
        usuario.setPerfil(dto.getPerfil() != null ? dto.getPerfil() : PerfilUsuario.CLIENTE);

        UsuarioModel usuarioSalvo = usuarioRepository.save(usuario);
        return converterParaDto(usuarioSalvo);
    }

    public UsuarioResponseDto atualizar(Long id, UsuarioResquestDto dto) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            usuario.setEmail(dto.getEmail());
        }
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        UsuarioModel usuarioAtualizado = usuarioRepository.save(usuario);
        return converterParaDto(usuarioAtualizado);
    }

    public List<UsuarioResponseDto> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDto buscarPorId(Long id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
        return converterParaDto(usuario);
    }

    // Exclui o usuário mesmo que existam chamados/atendimentos vinculados a
    // ele, tratando cada vínculo de forma coerente:
    //  1) Chamados que ELE mesmo abriu (solicitante) são removidos, junto
    //     com o próprio histórico de atendimentos desses chamados — sem
    //     solicitante, o chamado não faz mais sentido de existir.
    //  2) Chamados de OUTRAS pessoas onde ele só estava como técnico
    //     atribuído são mantidos, apenas ficam sem técnico (desatribuídos).
    //  3) Atendimentos que ele registrou como técnico em chamados de
    //     terceiros são removidos (o histórico daquele atendimento
    //     específico deixa de existir, mas o chamado em si permanece).
    public void deletar(Long id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        List<ChamadoModel> chamadosAbertosPorEle = chamadoRepository.findBySolicitanteId(id);
        for (ChamadoModel chamado : chamadosAbertosPorEle) {
            atendimentoRepository.deleteAll(atendimentoRepository.findByChamadoId(chamado.getId()));
        }
        chamadoRepository.deleteAll(chamadosAbertosPorEle);

        List<ChamadoModel> chamadosAtribuidosAEle = chamadoRepository.findByTecnicoAtribuidoId(id);
        for (ChamadoModel chamado : chamadosAtribuidosAEle) {
            chamado.setTecnicoAtribuido(null);
        }
        chamadoRepository.saveAll(chamadosAtribuidosAEle);

        atendimentoRepository.deleteAll(atendimentoRepository.findByTecnicoId(id));

        try {
            usuarioRepository.delete(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(
                "Não foi possível excluir este usuário devido a um vínculo não tratado no sistema."
            );
        }
    }

    private UsuarioResponseDto converterParaDto(UsuarioModel usuario) {
        return new UsuarioResponseDto(usuario);
    }

    public UsuarioResponseDto alterarPerfil(Long id, PerfilUsuario novoPerfil) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        usuario.setPerfil(novoPerfil);

        UsuarioModel usuarioAtualizado = usuarioRepository.save(usuario);
        return converterParaDto(usuarioAtualizado);
    }


    public UsuarioResponseDto promoverParaAdmin(Long id) {
        return alterarPerfil(id, PerfilUsuario.ADMIN);
    }
}