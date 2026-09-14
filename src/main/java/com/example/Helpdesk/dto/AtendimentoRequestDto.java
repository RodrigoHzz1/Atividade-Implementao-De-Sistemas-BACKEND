package com.example.Helpdesk.dto;

import com.example.Helpdesk.model.ChamadosEnum.NivelSuporte;
import com.example.Helpdesk.model.ChamadosEnum.Prioridade;
import com.example.Helpdesk.model.ChamadosEnum.StatusChamado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AtendimentoRequestDto {

    @NotNull(message = "O ID do chamado é obrigatório")
    private Long chamadoId;

    @NotNull(message = "O ID do técnico é obrigatório")
    private Long tecnicoId;

    @NotBlank(message = "A observação do atendimento é obrigatória")
    private String observacao;

    @NotNull(message = "A prioridade é obrigatória")
    private Prioridade prioridade;

    @NotNull(message = "O status é obrigatório")
    private StatusChamado status;

    @NotNull(message = "O nível de suporte é obrigatório")
    private NivelSuporte nivelSuporte;

    public AtendimentoRequestDto() {
    }

    public Long getChamadoId() {
        return chamadoId;
    }

    public void setChamadoId(Long chamadoId) {
        this.chamadoId = chamadoId;
    }

    public Long getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public StatusChamado getStatus() {
        return status;
    }

    public void setStatus(StatusChamado status) {
        this.status = status;
    }

    public NivelSuporte getNivelSuporte() {
        return nivelSuporte;
    }

    public void setNivelSuporte(NivelSuporte nivelSuporte) {
        this.nivelSuporte = nivelSuporte;
    }
}