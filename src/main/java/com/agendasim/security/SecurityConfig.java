package com.agendasim.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.agendasim.exception.response.ApiError;
import com.agendasim.exception.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.access.AccessDeniedException; // ✅ Correto


import java.io.IOException;



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
                .headers(headers -> headers.frameOptions().disable()) // permite /h2-console em frame
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(this::handleAccessDeniedException))

                .authorizeHttpRequests(auth -> auth
                        // Rotas públicas (sem autenticação)
                        .requestMatchers("/auth/**", "/agendamentos/**", "/swagger-ui/**", "/v3/api-docs/**",
                                "/h2-console/**", "/empresas/com-owner") // ← Adicionado endpoint público
                        .permitAll()

                        // EMPRESAS - apenas OWNER (exceto criação com owner que é pública)
                        .requestMatchers("/empresas/**").hasRole("OWNER")

                        // PROFISSIONAIS
                        .requestMatchers(HttpMethod.PATCH, "/profissionais/**").hasAnyRole("OWNER", "ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/profissionais/**").hasAnyRole("OWNER", "ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/profissionais/**").hasAnyRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/profissionais/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/profissionais/**").hasAnyRole("OWNER")

                        // AGENDAS
                        .requestMatchers(HttpMethod.GET, "/agendas/**").hasAnyRole("OWNER", "ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/agendas/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/agendas/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/agendas/**").hasAnyRole("OWNER", "ADMIN")

                        // DISPONIBILIDADES
                        .requestMatchers(HttpMethod.GET, "/disponibilidades/**").hasAnyRole("OWNER", "ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/disponibilidades/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/disponibilidades/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/disponibilidades/**").hasAnyRole("OWNER", "ADMIN")

                        // SERVIÇOS
                        .requestMatchers(HttpMethod.GET, "/servicos/**").hasAnyRole("OWNER", "ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/servicos/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/servicos/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/servicos/**").hasAnyRole("OWNER", "ADMIN")

                        // Qualquer outra requisição exige autenticação
                        .anyRequest().authenticated())

                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}