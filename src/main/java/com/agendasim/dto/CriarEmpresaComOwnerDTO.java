package com.agendasim.dto;

import com.agendasim.enums.Perfil;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CriarEmpresaComOwnerDTO {
    
    // Dados da Empresa
    @NotBlank(message = "O nome da empresa é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nomeEmpresa;

    @NotBlank(message = "O e-mail da empresa é obrigatório")
    @Email(message = "E-mail da empresa inválido")
    private String emailEmpresa;

    @Pattern(regexp = "\\+55\\s\\d{2}\\s9\\d{4}-\\d{4}", message = "Telefone inválido. Formato esperado: +55 31 99999-8888")
    @NotBlank(message = "O telefone da empresa é obrigatório")
    private String telefoneEmpresa;

    @NotBlank(message = "O CNPJ é obrigatório")
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "CNPJ inválido. Formato esperado: 00.000.000/0000-00")
    private String cnpjEmpresa;

    private Boolean ativoEmpresa = true;

    // Dados do Profissional Owner
    @NotBlank(message = "O nome do profissional é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nomeProfissional;

    @NotBlank(message = "O e-mail do profissional é obrigatório")
    @Email(message = "E-mail do profissional inválido")
    private String emailProfissional;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 3, max = 120, message = "A senha deve ter entre 3 e 120 caracteres")
    private String senhaProfissional;

    private Perfil perfilProfissional = Perfil.OWNER;
    private String googleAccessToken;
    private String googleRefreshToken;
    private Boolean ativoProfissional = true;
}