package com.example.Helpdesk.controller;

import com.example.Helpdesk.dto.ChamadoRequestDto;
import com.example.Helpdesk.dto.ChamadoResponseDto;
import com.example.Helpdesk.services.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @PostMapping
    public ResponseEntity<ChamadoResponseDto> criar(@Valid @RequestBody ChamadoRequestDto dto) {
        ChamadoResponseDto chamadoCriado = chamadoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(chamadoCriado);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ChamadoResponseDto>> listarPorUsuario(@PathVariable Long usuarioId){
        return ResponseEntity.ok(chamadoService.listarPorUsuario(usuarioId));
    }

    @GetMapping
    public ResponseEntity<List<ChamadoResponseDto>> listarTodos() {
        return ResponseEntity.ok(chamadoService.listarTodos());
    }
}