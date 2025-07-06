package com.agendasim.controller;

import com.agendasim.dto.LoginRequest;
import com.agendasim.model.Profissional;
import com.agendasim.repository.ProfissionalRepository;
import com.agendasim.security.JwtTokenService;
import com.agendasim.security.dto.JwtResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;
    private final ProfissionalRepository profissionalRepository;

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());
        Profissional profissional = profissionalRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));

        String token = jwtTokenService.generateToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);
        long expiresIn = jwtTokenService.getExpirationTimeInSeconds();

        return new JwtResponse(
                token, 
                refreshToken, 
                JwtResponse.UserInfo.fromProfissional(profissional),
                expiresIn
        );
    }
}