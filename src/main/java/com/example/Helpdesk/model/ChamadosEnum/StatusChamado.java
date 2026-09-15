package com.example.Helpdesk.model.ChamadosEnum;

/**
 * Status possíveis para um chamado ao longo do ciclo de atendimento.
 */
public enum StatusChamado {
    ABERTO,
    EM_ATENDIMENTO,
    ARGURDANDO,
    RESOLVIDO,
    CANCELADO;

    StatusChamado() {
    }
}
