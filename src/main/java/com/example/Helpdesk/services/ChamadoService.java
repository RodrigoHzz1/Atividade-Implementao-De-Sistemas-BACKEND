package com.example.Helpdesk.services;

import com.example.Helpdesk.dto.ChamadoRequestDto;
import com.example.Helpdesk.dto.ChamadoResponseDto;
import com.example.Helpdesk.model.ChamadoModel;
import com.example.Helpdesk.model.ChamadosEnum.NivelSuporte;
import com.example.Helpdesk.model.UsuarioModel;
import com.example.Helpdesk.repository.AtendimentoRepository;
import com.example.Helpdesk.repository.ChamadoRepository;
import com.example.Helpdesk.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AtendimentoRepository atendimentoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository, UsuarioRepository usuarioRepository, AtendimentoRepository atendimentoRepository) {
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.atendimentoRepository = atendimentoRepository;
    }

    // Método atualizado para receber o e-mail/login do usuário autenticado
    public ChamadoResponseDto criar(ChamadoRequestDto dto, String emailUsuario) {
        UsuarioModel solicitante = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário solicitante não encontrado."));

        ChamadoModel chamado = new ChamadoModel();
        chamado.setTitulo(dto.titulo());             // Chamada direta do record
        chamado.setDescricao(dto.descricao());       // Chamada direta do record
        chamado.setEquipamento(dto.equipamento());   // Chamada direta do record
        chamado.setSolicitante(solicitante);

        ChamadoModel salvo = chamadoRepository.save(chamado);
        return converterParaDto(salvo);
    }

    public List<ChamadoResponseDto> listarPorUsuario(Long usuarioID) {
        UsuarioModel usuario = usuarioRepository.findById(usuarioID)
                .orElseThrow(() -> new RuntimeException("Não encontrado usuário com o ID:" + usuarioID));
        List<ChamadoModel> chamados = List.of();

        switch (usuario.getPerfil()) {
            case CLIENTE:
                chamados = chamadoRepository.findBySolicitanteId(usuarioID);
                break;
            case TECNICO_N1:
                chamados = chamadoRepository.findByNivelAtual(NivelSuporte.N1);
                break;
            case TECNICO_N2:
                chamados = chamadoRepository.findByNivelAtualIn(List.of(NivelSuporte.N1, NivelSuporte.N2));
                break;
            case TECNICO_N3:
            case ADMIN:
            default:
                chamados = chamadoRepository.findAll();
                break;
        }

        return chamados.stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public List<ChamadoResponseDto> listarTodos() {
        return chamadoRepository.findAll().stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    // Exclui o chamado e o histórico de atendimentos vinculados a ele
    // (o atendimento não tem sentido de existir sem o chamado que o originou).
    public void excluir(Long id) {
        if (!chamadoRepository.existsById(id)) {
            throw new RuntimeException("Chamado não encontrado com o ID: " + id);
        }
        atendimentoRepository.deleteAll(atendimentoRepository.findByChamadoId(id));
        chamadoRepository.deleteById(id);
    }

    private ChamadoResponseDto converterParaDto(ChamadoModel chamado) {
        return new ChamadoResponseDto(
                chamado.getId(),
                chamado.getTitulo(),
                chamado.getDescricao(),
                chamado.getEquipamento(),
                chamado.getPrioridade(),
                chamado.getStatus(),
                chamado.getSolicitante().getNome(),
                chamado.getDataCriacao()
        );
    }
}