// src/main/java/com/agendasim/security/SecurityConfig.java
package com.agendasim.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.agendasim.exception.response.ApiError;
import com.agendasim.exception.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthFilter jwtAuthFilter;
        private final ProfissionalUserDetailsService userDetailsService;

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setUserDetailsService(userDetailsService);
                provider.setPasswordEncoder(passwordEncoder());
                return provider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        private void handleAccessDeniedException(HttpServletRequest request,
                        HttpServletResponse response,
                        AccessDeniedException ex) throws IOException {
                ApiError error = ApiError.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.FORBIDDEN.value())
                                .error("Access Denied")
                                .message("Você não tem permissão para realizar esta ação")
                                .path(request.getRequestURI())
                                .method(request.getMethod())
                                .traceId(UUID.randomUUID().toString().substring(0, 8))
                                .details(Map.of(
                                                "code", "ACCESS_DENIED",
                                                "suggestion", "Entre em contato com o administrador"))
                                .build();

                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType("application/json;charset=UTF-8");

                ApiResponse<Void> apiResponse = ApiResponse.error(error);
                ObjectMapper mapper = new ObjectMapper();
                mapper.findAndRegisterModules(); // Para LocalDateTime
                mapper.writeValue(response.getWriter(), apiResponse);
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .csrf(AbstractHttpConfigurer::disable)
                                .headers(headers -> headers.frameOptions().disable())
                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))
                                .exceptionHandling(exception -> exception
                                                .accessDeniedHandler(this::handleAccessDeniedException))

                                .authorizeHttpRequests(auth -> auth
                                                // Rotas públicas (sem autenticação)
                                                .requestMatchers(
                                                                "/auth/**",
                                                                "/agendamentos/**",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**",
                                                                "/h2-console/**",
                                                                "/empresas/com-owner",
                                                                // ENDPOINTS PÚBLICOS PARA AGENDAMENTO - ADICIONAR ESTAS
                                                                // LINHAS:
                                                                "/disponibilidades/profissional/data",
                                                                "/agendas/admin/data",
                                                                // ENDPOINTS LGPD PÚBLICOS
                                                                "/api/lgpd/clientes/**",
                                                                "/api/lgpd/termos/ativo",
                                                                "/api/lgpd/politicas/ativa",
                                                                // ROTAS ALTERNATIVAS PARA FRONTEND
                                                                "/api/termos/ativo",
                                                                "/api/politicas/ativa")
                                                .permitAll()

                                                // EMPRESAS - apenas OWNER (exceto criação com owner que é pública)
                                                .requestMatchers("/empresas/**").hasRole("OWNER")

                                                // PROFISSIONAIS
                                                .requestMatchers(HttpMethod.PATCH, "/profissionais/**")
                                                .hasAnyRole("OWNER", "ADMIN", "USER")
                                                .requestMatchers(HttpMethod.GET, "/profissionais/**")
                                                .hasAnyRole("OWNER", "ADMIN", "USER")
                                                .requestMatchers(HttpMethod.POST, "/profissionais/**")
                                                .hasAnyRole("OWNER")
                                                .requestMatchers(HttpMethod.PUT, "/profissionais/**")
                                                .hasAnyRole("OWNER", "ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/profissionais/**")
                                                .hasAnyRole("OWNER")

                                                // AGENDAS (exceto endpoints públicos já configurados acima)
                                                .requestMatchers(HttpMethod.GET, "/agendas/**")
                                                .hasAnyRole("OWNER", "ADMIN", "USER")
                                                .requestMatchers(HttpMethod.POST, "/agendas/**")
                                                .hasAnyRole("OWNER", "ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/agendas/**")
                                                .hasAnyRole("OWNER", "ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/agendas/**")
                                                .hasAnyRole("OWNER", "ADMIN")

                                                // DISPONIBILIDADES (exceto endpoint público já configurado acima)
                                                .requestMatchers(HttpMethod.GET, "/disponibilidades/**")
                                                .hasAnyRole("OWNER", "ADMIN", "USER")
                                                .requestMatchers(HttpMethod.POST, "/disponibilidades/**")
                                                .hasAnyRole("OWNER", "ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/disponibilidades/**")
                                                .hasAnyRole("OWNER", "ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/disponibilidades/**")
                                                .hasAnyRole("OWNER", "ADMIN")

                                                // SERVIÇOS
                                                .requestMatchers(HttpMethod.GET, "/servicos/**")
                                                .hasAnyRole("OWNER", "ADMIN", "USER")
                                                .requestMatchers(HttpMethod.POST, "/servicos/**")
                                                .hasAnyRole("OWNER", "ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/servicos/**")
                                                .hasAnyRole("OWNER", "ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/servicos/**")
                                                .hasAnyRole("OWNER", "ADMIN")

                                                // LGPD - Consentimentos (autenticado)
                                                .requestMatchers("/api/consentimentos/**")
                                                .hasAnyRole("OWNER", "ADMIN", "USER")
                                                .requestMatchers("/api/lgpd/consentimentos/**")
                                                .hasAnyRole("OWNER", "ADMIN", "USER")
                                                
                                                // LGPD - Termos e Políticas (aceitar - autenticado)
                                                .requestMatchers(HttpMethod.POST, "/api/lgpd/termos/aceitar")
                                                .hasAnyRole("OWNER", "ADMIN", "USER")
                                                .requestMatchers(HttpMethod.POST, "/api/lgpd/politicas/aceitar")
                                                .hasAnyRole("OWNER", "ADMIN", "USER")
                                                
                                                // ROTAS ALTERNATIVAS PARA FRONTEND (autenticado)
                                                .requestMatchers("/api/termos/**")
                                                .hasAnyRole("OWNER", "ADMIN", "USER")
                                                .requestMatchers("/api/politicas/**")
                                                .hasAnyRole("OWNER", "ADMIN", "USER")
                                                
                                                // SUPORTE - endpoints públicos e autenticados
                                                .requestMatchers("/api/suporte/publico/**")
                                                .permitAll()
                                                .requestMatchers("/api/suporte/**")
                                                .hasAnyRole("OWNER", "ADMIN", "USER")

                                                // Qualquer outra requisição exige autenticação
                                                .anyRequest().authenticated())

                                .authenticationProvider(authenticationProvider())
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .build();
        }
}