package com.agendasim.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
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

    @Pattern(regexp = "\\+55\\s\\d{2}\\s9\\d{4}-\\d{4}", message = "Telefone inválido. Formato esperado: +55 31 99999-8888")
    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;

    @Pattern(regexp = "(^$|\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2})", message = "CNPJ inválido. Formato esperado: 00.000.000/0000-00")
    private String cnpj;

    @NotBlank(message = "O nome único é obrigatório")
    @Size(min = 3, max = 50, message = "O nome único deve ter entre 3 e 50 caracteres")
    @Pattern(regexp = "^[a-z0-9]+$", message = "O nome único deve conter apenas letras minúsculas e números, sem espaços ou caracteres especiais")
    @Column(name = "nome_unico", unique = true, nullable = false)
    private String nomeUnico;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "created_at", updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
