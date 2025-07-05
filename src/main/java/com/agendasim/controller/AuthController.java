package com.agendasim.controller;

import com.agendasim.dto.LoginRequest;
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

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtTokenService.generateToken(user);

        return new JwtResponse(token);
    }
}
