package com.example.Helpdesk.controller;

import com.example.Helpdesk.dto.UsuarioResponseDto;
import com.example.Helpdesk.dto.UsuarioResquestDto;
import com.example.Helpdesk.model.ChamadosEnum.PerfilUsuario;
import com.example.Helpdesk.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> criar(@Valid @RequestBody UsuarioResquestDto dto) {
        UsuarioResponseDto novoUsuario = usuarioService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioResquestDto dto) {
        return ResponseEntity.ok(usuarioService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/perfil")
    public ResponseEntity<UsuarioResponseDto> alterarPerfil(
            @PathVariable Long id,
            @RequestParam String perfil) {


        PerfilUsuario perfilEnum = PerfilUsuario.valueOf(perfil.trim().toUpperCase());

        UsuarioResponseDto usuarioAtualizado = usuarioService.alterarPerfil(id, perfilEnum);
        return ResponseEntity.ok(usuarioAtualizado);
    }
}