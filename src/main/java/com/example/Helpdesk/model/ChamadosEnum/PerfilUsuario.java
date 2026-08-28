package com.example.Helpdesk.model.ChamadosEnum;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PerfilUsuario {
    CLIENTE,
    FUNCIONARIO,
    TECNICO_N1,
    TECNICO_N2,
    TECNICO_N3,
    ADMIN;

    PerfilUsuario() {
    }

    @JsonCreator
    public static PerfilUsuario fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        for (PerfilUsuario perfil : PerfilUsuario.values()) {
            if (perfil.name().equalsIgnoreCase(value.trim())) {
                return perfil;
            }
        }
        throw new IllegalArgumentException("Perfil inválido: " + value);
    }
}