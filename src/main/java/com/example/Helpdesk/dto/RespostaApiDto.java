package com.example.Helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RespostaApiDto<T> {

    private String mensagem;
    private T dados;

    public RespostaApiDto(String mensagem) {
        this.mensagem = mensagem;
    }

    public RespostaApiDto(String mensagem, T dados) {
        this.mensagem = mensagem;
        this.dados = dados;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public T getDados() {
        return dados;
    }

    public void setDados(T dados) {
        this.dados = dados;
    }
}