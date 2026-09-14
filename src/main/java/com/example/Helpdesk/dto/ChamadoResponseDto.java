package com.example.Helpdesk.dto;

import com.example.Helpdesk.model.ChamadosEnum.Prioridade;
import com.example.Helpdesk.model.ChamadosEnum.StatusChamado;

import java.time.LocalDateTime;

public class ChamadoResponseDto {
    private Long id;
    private String titulo;
    private String descricao;
    private String equipamento;
    private Prioridade prioridade;
    private StatusChamado status;
    private String nomeSolicitante;
    private LocalDateTime dataCriacao;

    public ChamadoResponseDto() {
    }

    public ChamadoResponseDto(Long id, String titulo, String descricao, String equipamento,
                              Prioridade prioridade, StatusChamado status,
                              String nomeSolicitante, LocalDateTime dataCriacao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.equipamento = equipamento;
        this.prioridade = prioridade;
        this.status = status;
        this.nomeSolicitante = nomeSolicitante;
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeSolicitante() {
        return nomeSolicitante;
    }

    public void setNomeSolicitante(String nomeSolicitante) {
        this.nomeSolicitante = nomeSolicitante;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
