package com.example.Helpdesk.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO usado na autenticação do usuário.
 */
public record LoginRequestDto(
        @NotBlank String email,
        @NotBlank String senha
) {}