package com.agendasim.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(@NonNull CorsRegistry registry) {
	    registry.addMapping("/**")
	            .allowedOrigins(
	                "http://localhost:3000",
	                "http://127.0.0.1:3000",
	                "http://localhost:5173",
	                "http://localhost:3001",
	                "https://agendesim.netlify.app",
	                "https://agendesim.com"
	            ) 
	            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
	            .allowedHeaders("*")
	            .allowCredentials(true)
	            .maxAge(3600);
	}
}