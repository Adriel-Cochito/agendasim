package com.agendasim.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // permite CORS para todas as rotas
                .allowedOrigins(
                    "http://localhost:3000",
                    "http://127.0.0.1:3000",
                    "http://localhost:3001"  // Caso esteja rodando em outra porta
                ) 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")          // Permitir todos os headers
                .allowCredentials(true)
                .maxAge(3600);               // Cache do preflight por 1 hora
    }
}