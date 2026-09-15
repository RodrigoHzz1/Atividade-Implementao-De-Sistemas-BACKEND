package com.example.Helpdesk.controller;

import com.example.Helpdesk.dto.AtendimentoRequestDto;
import com.example.Helpdesk.dto.AtendimentoResponseDto;
import com.example.Helpdesk.dto.AtendimentoUpdateRequestDto;
import com.example.Helpdesk.dto.RespostaApiDto;
import com.example.Helpdesk.services.AtendimentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável pelo histórico e registro de atendimentos.
 * Gerencia as operações de criação, leitura, atualização e remoção das interações.
 */
@RestController
@RequestMapping("/atendimentos")
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    public AtendimentoController(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    @PostMapping
    public ResponseEntity<RespostaApiDto<AtendimentoResponseDto>> registrar(@Valid @RequestBody AtendimentoRequestDto dto) {
        AtendimentoResponseDto resposta = atendimentoService.registrarAtendimento(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RespostaApiDto<>("Atendimento registrado com sucesso!", resposta));
    }

    @GetMapping
    public ResponseEntity<RespostaApiDto<List<AtendimentoResponseDto>>> listarTodos() {
        List<AtendimentoResponseDto> lista = atendimentoService.listarTodos();
        return ResponseEntity.ok(new RespostaApiDto<>("Atendimentos listados com sucesso!", lista));
    }

    @GetMapping("/chamado/{chamadoId}")
    public ResponseEntity<RespostaApiDto<List<AtendimentoResponseDto>>> listarPorChamado(@PathVariable Long chamadoId) {
        List<AtendimentoResponseDto> lista = atendimentoService.listarPorChamado(chamadoId);
        return ResponseEntity.ok(new RespostaApiDto<>("Atendimentos do chamado listados com sucesso!", lista));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespostaApiDto<Void>> excluir(@PathVariable Long id) {
        atendimentoService.excluir(id);
        return ResponseEntity.ok(new RespostaApiDto<>("Atendimento excluído com sucesso!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespostaApiDto<AtendimentoResponseDto>> atualizar(
            @PathVariable Long id, @RequestBody AtendimentoUpdateRequestDto dto) {
        AtendimentoResponseDto atualizado = atendimentoService.atualizar(id, dto);
        return ResponseEntity.ok(new RespostaApiDto<>("Atendimento atualizado com sucesso!", atualizado));
    }
}