package com.example.Helpdesk.dto;

import com.example.Helpdesk.model.ChamadosEnum.NivelSuporte;
import com.example.Helpdesk.model.ChamadosEnum.Prioridade;
import com.example.Helpdesk.model.ChamadosEnum.StatusChamado;

// Usado pelo Admin para editar diretamente um atendimento já registrado.
// Campos opcionais — só o que vier preenchido é atualizado. Não mexe no
// chamado nem no técnico vinculados ao atendimento (isso é feito ao
// registrar um novo atendimento, não editando um já existente).
public record AtendimentoUpdateRequestDto(
        String observacao,
        Prioridade prioridade,
        StatusChamado status,
        NivelSuporte nivelSuporte
) {}
