package com.agendasim.dto;

import com.agendasim.model.Empresa;
import com.agendasim.model.Profissional;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmpresaComOwnerResponseDTO {
    private Empresa empresa;
    private Profissional profissional;
    private String mensagem;
    
    public EmpresaComOwnerResponseDTO(Empresa empresa, Profissional profissional) {
        this.empresa = empresa;
        this.profissional = profissional;
        this.mensagem = "Empresa e profissional owner criados com sucesso";
    }
}