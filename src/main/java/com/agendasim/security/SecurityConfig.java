package com.agendasim.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions().disable()) // permite /h2-console em frame
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((req, res, ex) -> {
                            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        }))

                .authorizeHttpRequests(auth -> auth
                        // Rotas públicas (sem autenticação)
                        .requestMatchers("/auth/**", "/agendamentos/**", "/swagger-ui/**", "/v3/api-docs/**",
                                "/h2-console/**", "/empresas/com-owner") // ← Adicionado endpoint público
                        .permitAll()

                        // EMPRESAS - apenas OWNER (exceto criação com owner que é pública)
                        .requestMatchers("/empresas/**").hasRole("OWNER")

                        // PROFISSIONAIS
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
                        .requestMatchers(HttpMethod.POST, "/servicos/**").hasAnyRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/servicos/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/servicos/**").hasAnyRole("OWNER")

                        // Qualquer outra requisição exige autenticação
                        .anyRequest().authenticated())

                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}