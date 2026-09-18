package com.example.Helpdesk.model.ChamadosEnum;

/**
 * Status possíveis para um chamado ao longo do ciclo de atendimento.
 */
public enum StatusChamado {
    ABERTO,
    EM_ATENDIMENTO,
    AGURDANDO,
    RESOLVIDO,
    CANCELADO;

    StatusChamado() {
    }
}
