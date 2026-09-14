package com.example.Helpdesk.model;

import com.example.Helpdesk.model.ChamadosEnum.NivelSuporte;
import com.example.Helpdesk.model.ChamadosEnum.Prioridade;
import com.example.Helpdesk.model.ChamadosEnum.StatusChamado;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Tab_Atendimento")
public class AtendimentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chamado_id", nullable = false)
    private ChamadoModel chamado;

    @ManyToOne
    @JoinColumn(name = "tecnico_id", nullable = false)
    private UsuarioModel tecnico;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String observacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusChamado status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelSuporte nivelSuporte;

    private LocalDateTime dataAtendimento;

    @PrePersist
    public void prePersist() {
        this.dataAtendimento = LocalDateTime.now();
    }

    public AtendimentoModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChamadoModel getChamado() {
        return chamado;
    }

    public void setChamado(ChamadoModel chamado) {
        this.chamado = chamado;
    }

    public UsuarioModel getTecnico() {
        return tecnico;
    }

    public void setTecnico(UsuarioModel tecnico) {
        this.tecnico = tecnico;
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

    public LocalDateTime getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(LocalDateTime dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }
}