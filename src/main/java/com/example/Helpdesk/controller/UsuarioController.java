package com.example.Helpdesk.controller;

import com.example.Helpdesk.dto.RespostaApiDto;
import com.example.Helpdesk.dto.UsuarioResponseDto;
import com.example.Helpdesk.model.ChamadosEnum.PerfilUsuario;
import com.example.Helpdesk.model.UsuarioModel;
import com.example.Helpdesk.repository.UsuarioRepository;
import com.example.Helpdesk.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    // Construtor atualizado com a injeção do UsuarioService
    public UsuarioController(UsuarioRepository usuarioRepository,
                             PasswordEncoder passwordEncoder,
                             UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<RespostaApiDto<UsuarioModel>> cadastrar(@Valid @RequestBody UsuarioModel usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        UsuarioModel salvo = usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RespostaApiDto<>("Usuário cadastrado com sucesso!", salvo));
    }

    @PatchMapping("/{id}/perfil")
    public ResponseEntity<RespostaApiDto<UsuarioResponseDto>> alterarPerfil(
            @PathVariable Long id,
            @RequestParam String perfil) {

        PerfilUsuario perfilEnum = PerfilUsuario.valueOf(perfil.trim().toUpperCase());

        UsuarioResponseDto usuarioAtualizado = usuarioService.alterarPerfil(id, perfilEnum);

        return ResponseEntity.ok(
                new RespostaApiDto<>("Perfil alterado com sucesso!", usuarioAtualizado)
        );
    }
}