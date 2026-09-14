package com.example.Helpdesk.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// Indica que a classe é um componente de serviço gerenciado pelo Spring
@Service
public class TokenService {

    // Injeta a chave secreta definida no application.properties (com valor padrão de segurança)
    @Value("${api.security.token.secret:sua-chave-secreta-com-pelo-menos-32-caracteres-helpdesk}")
    private String secret;

    // Converte a String do segredo em uma chave criptográfica HMAC-SHA para assinatura digital
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Monta e assina o JWT contendo o e-mail do usuário e prazo de expiração de 8h
    public String gerarToken(String email) {
        long duracaoEmMilissegundos = 1000L * 60 * 60 * 8; // Cálculo explícito: 8 horas

        return Jwts.builder()
                .subject(email) // Define o identificador do usuário no payload
                .issuedAt(new Date()) // Registra o momento exato da criação
                .expiration(new Date(System.currentTimeMillis() + duracaoEmMilissegundos)) // Define data limite
                .signWith(getSigningKey()) // Assina com a chave privada HMAC
                .compact(); // Concatena as partes e gera a String final codificada em Base64
    }

    // Valida a assinatura/tempo de vida e extrai a identidade do usuário
    public String validarToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey()) // Aplica a chave para verificar integridade da assinatura
                    .build()
                    .parseSignedClaims(token) // Lança exceção se o token for adulterado ou expirado
                    .getPayload();
            return claims.getSubject(); // Retorna o e-mail caso a validação seja bem-sucedida
        } catch (Exception e) {
            // Em caso de falha de assinatura ou expiração, bloqueia o acesso retornando null
            return null;
        }
    }
}
