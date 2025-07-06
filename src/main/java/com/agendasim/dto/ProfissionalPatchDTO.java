package com.agendasim.dto;

import com.agendasim.enums.Perfil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfissionalPatchDTO {
    private String nome;
    private String email;
    private String senha;
    private Perfil perfil;
    private String googleAccessToken;
    private String googleRefreshToken;
    private Boolean ativo;
    private Long empresaId;
}
