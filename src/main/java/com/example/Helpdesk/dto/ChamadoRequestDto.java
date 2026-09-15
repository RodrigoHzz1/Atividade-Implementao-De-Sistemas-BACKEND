package com.example.Helpdesk.dto;

import jakarta.validation.constraints.NotBlank;


/**
 * DTO para criação de um novo chamado.
 * Recebe apenas os dados essenciais necessários para abrir um ticket.
 */
public record ChamadoRequestDto(
        @NotBlank(message = "O título é obrigatório")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        String equipamento


) {}