package com.example.Helpdesk.dto;

import jakarta.validation.constraints.NotBlank;


public record ChamadoRequestDto(
        @NotBlank(message = "O título é obrigatório")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        String equipamento


) {}