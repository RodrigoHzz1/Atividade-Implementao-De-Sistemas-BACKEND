package com.example.Helpdesk.services;

import com.example.Helpdesk.dto.ChamadoRequestDto;
import com.example.Helpdesk.dto.ChamadoResponseDto;
import com.example.Helpdesk.model.ChamadoModel;
import com.example.Helpdesk.model.ChamadosEnum.NivelSuporte;
import com.example.Helpdesk.model.UsuarioModel;
import com.example.Helpdesk.repository.ChamadoRepository;
import com.example.Helpdesk.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;

    public ChamadoService(ChamadoRepository chamadoRepository, UsuarioRepository usuarioRepository) {
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ChamadoResponseDto criar(ChamadoRequestDto dto) {
        UsuarioModel solicitante = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário solicitante não encontrado."));

        ChamadoModel chamado = new ChamadoModel();
        chamado.setTitulo(dto.getTitulo());
        chamado.setDescricao(dto.getDescricao());
        chamado.setEquipamento(dto.getEquipamento());
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