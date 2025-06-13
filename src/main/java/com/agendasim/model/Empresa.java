package com.agendasim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "empresas")
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @Pattern(
        regexp = "\\+55\\s\\d{2}\\s9\\d{4}-\\d{4}",
        message = "Telefone inválido. Formato esperado: +55 31 99999-8888"
    )
    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;


    @NotBlank(message = "O CNPJ é obrigatório")
    @Pattern(
        regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}",
        message = "CNPJ inválido. Formato esperado: 00.000.000/0000-00"
    )
    private String cnpj;

    
}
