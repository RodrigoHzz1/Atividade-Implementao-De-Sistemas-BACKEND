package com.example.Helpdesk.dto;

import com.example.Helpdesk.model.ChamadosEnum.Prioridade;
import com.example.Helpdesk.model.ChamadosEnum.StatusChamado;

// Usado pelo Admin para editar um chamado diretamente (título, descrição,
// equipamento, prioridade e status). Todos os campos são opcionais — só o
// que vier preenchido no corpo da requisição é atualizado.
public record ChamadoUpdateRequestDto(
        String titulo,
        String descricao,
        String equipamento,
        Prioridade prioridade,
        StatusChamado status
) {}
