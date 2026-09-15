package com.example.Helpdesk.controller;

import com.example.Helpdesk.config.TokenService;
import com.example.Helpdesk.dto.LoginRequestDto;
import com.example.Helpdesk.dto.RespostaApiDto;
import com.example.Helpdesk.dto.TokenResponseDto;
import com.example.Helpdesk.repository.UsuarioRepository; // Importe seu repository aqui
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsável pela autenticação dos usuários.
 * Recebe o e-mail e senha, valida as credenciais e retorna um token JWT.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository; // 1. Adicionado

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository; // 2. Injetado
    }

    @PostMapping("/login")
    public ResponseEntity<RespostaApiDto<TokenResponseDto>> login(@RequestBody @Valid LoginRequestDto dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
        this.authenticationManager.authenticate(usernamePassword);

        // Busca o usuário no banco pelo e-mail do DTO
        var usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = tokenService.gerarToken(dto.email());

        TokenResponseDto responseDto = new TokenResponseDto(
                usuario.getId(),
                token,
                usuario.getEmail(),
                usuario.getPerfil() != null ? usuario.getPerfil().toString() : ""
        );

        return ResponseEntity.ok(
                new RespostaApiDto<>("Login realizado com sucesso!", responseDto)
        );
    }
}