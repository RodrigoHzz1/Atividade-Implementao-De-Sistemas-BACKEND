package com.example.Helpdesk.config;

import com.example.Helpdesk.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Componente gerenciado pelo Spring para interceptar requisições HTTP na camada web
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    // Injeção de dependências via construtor (prática recomendada para imutabilidade)
    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    // Método executado garantidamente uma única vez a cada requisição HTTP recebida
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extrai a String do token presente no cabeçalho Authorization
        var token = recuperarToken(request);

        if (token != null) {
            // 2. Valida o token e recupera o e-mail (subject) do usuário
            var login = tokenService.validarToken(token);
            if (login != null) {
                // 3. Carrega o usuário do banco para recuperar suas permissões (Authorities)
                UserDetails usuario = usuarioRepository.findByEmail(login)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                // 4. Cria o objeto de autenticação com o usuário e suas permissões
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                
                // 5. Registra a autenticação na sessão/thread atual da aplicação
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        // 6. Passa a requisição adiante para o próximo filtro ou Controller da aplicação
        filterChain.doFilter(request, response);
    }

    // Trata o padrão HTTP "Authorization: Bearer <token>" e isola a hash JWT
    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return authHeader.replace("Bearer ", "");
    }
}
