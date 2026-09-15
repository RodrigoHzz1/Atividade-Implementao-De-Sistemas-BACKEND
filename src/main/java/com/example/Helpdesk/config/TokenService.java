package com.example.Helpdesk.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Serviço responsável por emitir e validar tokens JWT usados na autenticação da API.
 */
@Service
public class TokenService {

    /**
     * Chave secreta usada para assinar os tokens.
     * O valor pode ser configurado no application.properties.
     */
    @Value("${api.security.token.secret:sua-chave-secreta-com-pelo-menos-32-caracteres-helpdesk}")
    private String secret;

    /**
     * Cria a chave criptográfica usada para assinar e verificar o JWT.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Gera um token JWT contendo o e-mail do usuário e validade de 8 horas.
     */
    public String gerarToken(String email) {
        // Validade de 8 horas (1000ms * 60s * 60m * 8h)
        long duracaoEmMilissegundos = 1000L * 60 * 60 * 8;

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + duracaoEmMilissegundos))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Verifica a autenticidade do token e retorna o assunto, que neste projeto é o e-mail do usuário.
     */
    public String validarToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}