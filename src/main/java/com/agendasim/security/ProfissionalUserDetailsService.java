package com.agendasim.security;

import com.agendasim.model.Profissional;
import com.agendasim.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class ProfissionalUserDetailsService implements UserDetailsService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Profissional profissional = profissionalRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Profissional não encontrado com email: " + email));

        return User.builder()
                .username(profissional.getEmail())
                .password(profissional.getSenha())
                .roles(profissional.getPerfil().name()) // ex: ADMIN
                .build();

    }
}
