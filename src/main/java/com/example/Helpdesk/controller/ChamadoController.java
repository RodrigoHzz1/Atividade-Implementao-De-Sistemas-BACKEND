package com.example.Helpdesk.controller;

import com.example.Helpdesk.dto.ChamadoRequestDto;
import com.example.Helpdesk.dto.ChamadoResponseDto;
import com.example.Helpdesk.dto.ChamadoUpdateRequestDto;
import com.example.Helpdesk.dto.RespostaApiDto;
import com.example.Helpdesk.services.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para operações de chamados.
 * Permite criar, listar, editar e excluir registros de suporte.
 */
@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @PostMapping
    public ResponseEntity<ChamadoResponseDto> criar(
            @RequestBody @Valid ChamadoRequestDto dto,
            Authentication authentication) {

        String emailUsuario = authentication.getName();
        ChamadoResponseDto novoChamado = chamadoService.criar(dto, emailUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoChamado);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ChamadoResponseDto>> listarPorUsuario(@PathVariable Long usuarioId){
        return ResponseEntity.ok(chamadoService.listarPorUsuario(usuarioId));
    }

    @GetMapping
    public ResponseEntity<List<ChamadoResponseDto>> listarTodos() {
        return ResponseEntity.ok(chamadoService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespostaApiDto<Void>> excluir(@PathVariable Long id) {
        chamadoService.excluir(id);
        return ResponseEntity.ok(new RespostaApiDto<>("Chamado excluído com sucesso!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespostaApiDto<ChamadoResponseDto>> atualizar(
            @PathVariable Long id, @RequestBody ChamadoUpdateRequestDto dto) {
        ChamadoResponseDto atualizado = chamadoService.atualizar(id, dto);
        return ResponseEntity.ok(new RespostaApiDto<>("Chamado atualizado com sucesso!", atualizado));
    }
}