package com.example.Helpdesk.controller;

import com.example.Helpdesk.dto.RespostaApiDto;
import com.example.Helpdesk.model.UsuarioModel;
import com.example.Helpdesk.repository.UsuarioRepository;
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

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<RespostaApiDto<UsuarioModel>> cadastrar(@Valid @RequestBody UsuarioModel usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        UsuarioModel salvo = usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RespostaApiDto<>("Usuário cadastrado com sucesso!", salvo));
    }
}