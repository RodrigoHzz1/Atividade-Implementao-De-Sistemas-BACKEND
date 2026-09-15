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

/**
 * Filtro executado a cada requisição para validar o JWT do cliente.
 * Se o token for válido, o usuário é autenticado no contexto do Spring Security.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    /**
     * Serviço responsável por gerar e validar tokens JWT.
     */
    private final TokenService tokenService;

    /**
     * Repositório usado para buscar o usuário autenticado pelo e-mail contido no token.
     */
    private final UsuarioRepository usuarioRepository;

    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Valida o token presente no cabeçalho Authorization e define a autenticação atual.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var token = recuperarToken(request);

        if (token != null) {
            var login = tokenService.validarToken(token);
            if (login != null) {
                UserDetails usuario = usuarioRepository.findByEmail(login)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Extrai o valor do token a partir do cabeçalho Authorization com prefixo Bearer.
     */
    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return authHeader.replace("Bearer ", "");
    }
}