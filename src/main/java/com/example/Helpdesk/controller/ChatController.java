package com.example.Helpdesk.controller;

import com.example.Helpdesk.dto.AtendimentoRequestDto;
import com.example.Helpdesk.dto.AtendimentoResponseDto;
import com.example.Helpdesk.services.AtendimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Controller que processa mensagens do chat em tempo real via WebSocket.
 * As mensagens recebidas são persistidas e distribuídas para os clientes inscritos.
 */
@Controller
public class ChatController {

    // Serviço para persistência das mensagens no banco de dados
    @Autowired
    private AtendimentoService atendimentoService;

    // Utilitário para envio ativo de mensagens para salas ou tópicos específicos
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Escuta requisições disparadas para: /app/chat.enviar/{chamadoId}
    @MessageMapping("/chat.enviar/{chamadoId}")
    public void processarMensagemChat(@DestinationVariable Long chamadoId, @Payload AtendimentoRequestDto dto) {
        // Grava o novo registro de atendimento/mensagem na base de dados
        AtendimentoResponseDto atendimentoSalvo = atendimentoService.registrarAtendimento(dto);

        // Transmite a mensagem em tempo real para todos os inscritos no canal /topic/chamado/{chamadoId}
        messagingTemplate.convertAndSend("/topic/chamado/" + chamadoId, atendimentoSalvo);
    }
}