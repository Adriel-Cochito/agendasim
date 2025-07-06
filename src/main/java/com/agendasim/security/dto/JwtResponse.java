package com.agendasim.security.dto;

import com.agendasim.model.Profissional;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String refreshToken;
    private UserInfo user;
    private long expiresIn; // em segundos

    @Data
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String nome;
        private String email;
        private String perfil;
        private Long empresaId;
        
        public static UserInfo fromProfissional(Profissional profissional) {
            return new UserInfo(
                profissional.getId(),
                profissional.getNome(),
                profissional.getEmail(),
                profissional.getPerfil().name(),
                profissional.getEmpresaId()
            );
        }
    }
}