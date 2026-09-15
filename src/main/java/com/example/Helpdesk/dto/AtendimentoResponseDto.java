package com.example.Helpdesk.dto;

import com.example.Helpdesk.model.AtendimentoModel;
import com.example.Helpdesk.model.ChamadosEnum.NivelSuporte;
import com.example.Helpdesk.model.ChamadosEnum.Prioridade;
import com.example.Helpdesk.model.ChamadosEnum.StatusChamado;

import java.time.LocalDateTime;

/**
 * DTO de resposta para um atendimento registrado.
 * Expõe os dados relevantes para o frontend exibir o histórico do chat e do chamado.
 */
public class AtendimentoResponseDto {

    private Long id;
    private Long chamadoId;
    private Long usuarioId;
    private String usuarioNome;
    private Long tecnicoId;
    private String tecnicoNome;
    private String observacao;
    private Prioridade prioridade;
    private StatusChamado status;
    private NivelSuporte nivelSuporte;
    private LocalDateTime dataAtendimento;

    public AtendimentoResponseDto() {
    }

    public AtendimentoResponseDto(AtendimentoModel atendimento) {
        this.id = atendimento.getId();
        this.chamadoId = atendimento.getChamado() != null ? atendimento.getChamado().getId() : null;

        // Tratamento seguro para o Usuário remetente
        if (atendimento.getUsuario() != null) {
            this.usuarioId = atendimento.getUsuario().getId();
            this.usuarioNome = atendimento.getUsuario().getNome();
        }

        // Tratamento seguro para o Técnico (pode ser null)
        if (atendimento.getTecnico() != null) {
            this.tecnicoId = atendimento.getTecnico().getId();
            this.tecnicoNome = atendimento.getTecnico().getNome();
        }

        this.observacao = atendimento.getObservacao();
        this.prioridade = atendimento.getPrioridade();
        this.status = atendimento.getStatus();
        this.nivelSuporte = atendimento.getNivelSuporte();
        this.dataAtendimento = atendimento.getDataAtendimento();
    }

    public static AtendimentoResponseDto paraCliente(AtendimentoModel atendimento) {
        return new AtendimentoResponseDto(atendimento);
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getChamadoId() { return chamadoId; }
    public void setChamadoId(Long chamadoId) { this.chamadoId = chamadoId; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }

    public Long getTecnicoId() { return tecnicoId; }
    public void setTecnicoId(Long tecnicoId) { this.tecnicoId = tecnicoId; }

    public String getTecnicoNome() { return tecnicoNome; }
    public void setTecnicoNome(String tecnicoNome) { this.tecnicoNome = tecnicoNome; }

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