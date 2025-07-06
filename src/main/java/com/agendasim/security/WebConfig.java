package com.agendasim.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // permite CORS para todas as rotas
                .allowedOrigins("http://localhost:3000/") // origem que pode acessar (frontend)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // métodos HTTP permitidos
                .allowCredentials(true);
    }
}

