package com.example.Helpdesk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// Classe de configuração central da segurança da aplicação Spring
@Configuration
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    // Define a cadeia de filtros e regras de acesso das rotas HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilita o CORS configurado
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF por ser uma API REST Stateless (não usa Cookies)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Não cria sessões no servidor
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // Rota pública para autenticação
                        .requestMatchers(HttpMethod.POST, "/usuarios").permitAll() // Rota pública para cadastro inicial
                        .requestMatchers("/error").permitAll() // Libera rotas internas de erro do Spring
                        .anyRequest().authenticated() // Exige autenticação JWT para todas as outras rotas
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // Injeta o filtro JWT antes do filtro padrão do Spring
                .build();
    }

    // Configura o compartilhamento de recursos entre origens diferentes (CORS)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Origens do frontend autorizadas a consumir a API (React, Vite, Live Server, etc.)
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:5173",
                "http://127.0.0.1:5500",
                "http://localhost:5500",
                "http://localhost:8080",
                "http://127.0.0.1:8080"
        ));

        // Verbos e cabeçalhos liberados para comunicação cross-origin
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a política para todos os endpoints
        return source;
    }

    // Expõe o gerenciador de autenticação nativo para o controller de login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Define o algoritmo de hashing para criptografar e validar senhas de usuários
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
