package com.example.Helpdesk.dto;

import com.example.Helpdesk.model.ChamadosEnum.PerfilUsuario;
import com.example.Helpdesk.model.UsuarioModel;

public class UsuarioResponseDto {
    private Long id;
    private String nome;
    private String email;
    private PerfilUsuario perfil;

    public UsuarioResponseDto() {
    }

    public UsuarioResponseDto(Long id, String nome, String email, PerfilUsuario perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
    }

    public UsuarioResponseDto(UsuarioModel usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.perfil = usuario.getPerfil();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }
}