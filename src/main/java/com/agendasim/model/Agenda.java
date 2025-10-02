package com.agendasim.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

@Entity
@Table(name = "agendas")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do cliente é obrigatório")
    @Size(max = 100, message = "O nome do cliente deve ter no máximo 100 caracteres")
    private String nomeCliente;

    @NotBlank(message = "O telefone do cliente é obrigatório")
    private String telefoneCliente;

    @Email(message = "Email deve ter formato válido")
    private String emailCliente;

    @Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres")
    private String enderecoCliente;

    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$|^\\d{11}$", message = "CPF deve ter formato válido")
    private String cpfCliente;

    @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
    private String observacoes;

    @NotNull(message = "A empresa é obrigatória")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @NotNull(message = "O profissional é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;

    @NotNull(message = "O serviço é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "servico_id")
    private Servico servico;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    @NotNull(message = "A data e hora da agenda são obrigatórias")
    private Instant dataHora;

    @Column(name = "data_agendamento")
    private java.time.LocalDate dataAgendamento;

    @Column(name = "empresa_id", insertable = false, updatable = false)
    private Long empresaId;

    @NotBlank(message = "O status da agenda é obrigatório")
    @Pattern(regexp = "^(AGENDADO|CONFIRMADO|REALIZADO|CANCELADO)$", message = "Status inválido. Use AGENDADO, CONFIRMADO, REALIZADO ou CANCELADO")
    private String status;

    @Column(name = "created_at", updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now(); // Se createdAt também for Instant
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now(); // Se updatedAt também for Instant
    }

}
