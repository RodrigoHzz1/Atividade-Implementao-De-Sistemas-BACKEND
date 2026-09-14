package com.example.Helpdesk.dto;

import com.example.Helpdesk.model.AtendimentoModel;
import com.example.Helpdesk.model.ChamadosEnum.NivelSuporte;
import com.example.Helpdesk.model.ChamadosEnum.Prioridade;
import com.example.Helpdesk.model.ChamadosEnum.StatusChamado;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AtendimentoResponseDto {

    private Long id;
    private Long chamadoId;
    private String nomeTecnico;
    private String observacao;
    private Prioridade prioridade;
    private StatusChamado status;
    private NivelSuporte nivelSuporte;
    private LocalDateTime dataAtendimento;

    public AtendimentoResponseDto() {
    }

    // Construtor completo com todos os parâmetros
    public AtendimentoResponseDto(Long id, Long chamadoId, String nomeTecnico, String observacao,
                                  Prioridade prioridade, StatusChamado status,
                                  NivelSuporte nivelSuporte, LocalDateTime dataAtendimento) {
        this.id = id;
        this.chamadoId = chamadoId;
        this.nomeTecnico = nomeTecnico;
        this.observacao = observacao;
        this.prioridade = prioridade;
        this.status = status;
        this.nivelSuporte = nivelSuporte;
        this.dataAtendimento = dataAtendimento;
    }

    // Mapeamento completo para Técnico e Admin
    public AtendimentoResponseDto(AtendimentoModel atendimento) {
        this.id = atendimento.getId();
        this.chamadoId = atendimento.getChamado() != null ? atendimento.getChamado().getId() : null;
        this.nomeTecnico = atendimento.getTecnico() != null ? atendimento.getTecnico().getNome() : null;
        this.observacao = atendimento.getObservacao();
        this.prioridade = atendimento.getPrioridade();
        this.status = atendimento.getStatus();
        this.nivelSuporte = atendimento.getNivelSuporte();
        this.dataAtendimento = atendimento.getDataAtendimento();
    }

    // Mapeamento resumido para Cliente (exibe apenas nomeTecnico, status e dataAtendimento)
    public static AtendimentoResponseDto paraCliente(AtendimentoModel atendimento) {
        AtendimentoResponseDto dto = new AtendimentoResponseDto();
        dto.setNomeTecnico(atendimento.getTecnico() != null ? atendimento.getTecnico().getNome() : null);
        dto.setStatus(atendimento.getStatus());
        dto.setDataAtendimento(atendimento.getDataAtendimento());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getChamadoId() { return chamadoId; }
    public void setChamadoId(Long chamadoId) { this.chamadoId = chamadoId; }

    public String getNomeTecnico() { return nomeTecnico; }
    public void setNomeTecnico(String nomeTecnico) { this.nomeTecnico = nomeTecnico; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public Prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }

    public StatusChamado getStatus() { return status; }
    public void setStatus(StatusChamado status) { this.status = status; }

    public NivelSuporte getNivelSuporte() { return nivelSuporte; }
    public void setNivelSuporte(NivelSuporte nivelSuporte) { this.nivelSuporte = nivelSuporte; }

    public LocalDateTime getDataAtendimento() { return dataAtendimento; }
    public void setDataAtendimento(LocalDateTime dataAtendimento) { this.dataAtendimento = dataAtendimento; }
}