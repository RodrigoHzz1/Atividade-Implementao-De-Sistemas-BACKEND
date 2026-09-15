package com.example.Helpdesk.dto;

/**
 * DTO retornado após o login com sucesso, incluindo o token JWT do usuário.
 */
public record TokenResponseDto(
        Long id,
        String token,
        String email,
        String perfil
) {
}