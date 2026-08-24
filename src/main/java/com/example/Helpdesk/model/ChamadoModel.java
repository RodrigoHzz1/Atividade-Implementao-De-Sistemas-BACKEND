package com.example.Helpdesk.model;

import com.example.Helpdesk.model.ChamadosEnum.NivelSuporte;
import com.example.Helpdesk.model.ChamadosEnum.Prioridade;
import com.example.Helpdesk.model.ChamadosEnum.StatusChamado;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Tab_Chamado")
public class ChamadoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    private String equipamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridade prioridade = Prioridade.BAIXA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusChamado status = StatusChamado.ABERTO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelSuporte nivelAtual = NivelSuporte.N1;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel solicitante;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private UsuarioModel tecnicoAtribuido;

    private LocalDateTime dataCriacao;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }

    public ChamadoModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
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

    public NivelSuporte getNivelAtual() {
        return nivelAtual;
    }

    public void setNivelAtual(NivelSuporte nivelAtual) {
        this.nivelAtual = nivelAtual;
    }

    public UsuarioModel getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(UsuarioModel solicitante) {
        this.solicitante = solicitante;
    }

    public UsuarioModel getTecnicoAtribuido() {
        return tecnicoAtribuido;
    }

    public void setTecnicoAtribuido(UsuarioModel tecnicoAtribuido) {
        this.tecnicoAtribuido = tecnicoAtribuido;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}