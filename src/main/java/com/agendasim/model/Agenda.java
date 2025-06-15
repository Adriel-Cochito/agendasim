package com.agendasim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

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
    @Pattern(regexp = "^\\+55\\s\\d{2}\\s9\\d{4}-\\d{4}$", message = "O telefone deve estar no formato +55 31 99999-8888")
    private String telefoneCliente;

    @NotNull(message = "A empresa é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @NotNull(message = "O profissional é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;

    @NotNull(message = "O serviço é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id")
    private Servico servico;

    @NotNull(message = "A data e hora da agenda são obrigatórias")
    private LocalDateTime dataHora;

    @NotBlank(message = "O status da agenda é obrigatório")
    @Pattern(regexp = "^(AGENDADO|CONFIRMADO|REALIZADO|CANCELADO)$",
             message = "Status inválido. Use AGENDADO, CONFIRMADO, REALIZADO ou CANCELADO")
    private String status;
}
