package com.example.Helpdesk.controller;

import com.example.Helpdesk.config.TokenService;
import com.example.Helpdesk.dto.LoginRequestDto;
import com.example.Helpdesk.dto.RespostaApiDto;
import com.example.Helpdesk.dto.TokenResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<RespostaApiDto<TokenResponseDto>> login(@RequestBody @Valid LoginRequestDto dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
        this.authenticationManager.authenticate(usernamePassword);

        String token = tokenService.gerarToken(dto.email());

        return ResponseEntity.ok(
                new RespostaApiDto<>("Login realizado com sucesso!", new TokenResponseDto(token))
        );
    }
}