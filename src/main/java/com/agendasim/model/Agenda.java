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

    @NotNull(message = "O ID da empresa é obrigatório")
    private Long empresaId;

    @NotNull(message = "O ID da empresa é obrigatório")
    private Long profissionalId;

    @NotNull(message = "O ID do serviço é obrigatório")
    private Long servicoId;

    @NotNull(message = "A data e hora da agenda são obrigatórias")
    private LocalDateTime dataHora;

    @NotBlank(message = "O status da agenda é obrigatório")
    @Pattern(regexp = "^(AGENDADO|CONFIRMADO|REALIZADO|CANCELADO)$", message = "Status inválido. Use AGENDADO, CANCELADO ou REALIZADO")
    private String status;
}
