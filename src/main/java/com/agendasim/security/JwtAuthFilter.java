// src/main/java/com/agendasim/security/JwtAuthFilter.java
package com.agendasim.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;

    // Lista de rotas públicas que NÃO devem processar JWT
    private static final List<String> PUBLIC_ROUTES = Arrays.asList(
        "/auth/",
        "/agendamentos/",
        "/empresas/com-owner",
        "/swagger-ui/",
        "/v3/api-docs/",
        "/h2-console/"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Verificar se é uma rota pública
        String requestPath = request.getRequestURI();
        if (isPublicRoute(requestPath)) {
            // Para rotas públicas, pular processamento JWT completamente
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String token;
        final String email;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7);
        
        try {
            email = jwtTokenService.extractUsername(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (jwtTokenService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Para rotas protegidas com JWT inválido/expirado, deixar Spring Security tratar
            // Não logar erro aqui pois pode ser spam
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicRoute(String requestPath) {
        return PUBLIC_ROUTES.stream().anyMatch(requestPath::startsWith);
    }
}