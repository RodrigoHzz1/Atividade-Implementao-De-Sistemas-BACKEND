package com.example.Helpdesk.dto;

import com.example.Helpdesk.model.ChamadosEnum.NivelSuporte;
import com.example.Helpdesk.model.ChamadosEnum.Prioridade;
import com.example.Helpdesk.model.ChamadosEnum.StatusChamado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO usado para registrar uma interação de atendimento.
 * Inclui dados do chamado, do usuário que enviou a mensagem e, opcionalmente, do técnico e do status.
 */
public class AtendimentoRequestDto {

    @NotNull(message = "O ID do chamado é obrigatório")
    private Long chamadoId;

    // Opcional: pode ser nulo se for enviado por um cliente
    private Long tecnicoId;

    // Novo campo para registrar o remetente da mensagem
    @NotNull(message = "O ID do usuário remetente é obrigatório")
    private Long usuarioId;

    @NotBlank(message = "A observação do atendimento é obrigatória")
    private String observacao;

    // Opcionais: mensagens simples do chat não exigem alteração de status/prioridade/nível
    private Prioridade prioridade;
    private StatusChamado status;
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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
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