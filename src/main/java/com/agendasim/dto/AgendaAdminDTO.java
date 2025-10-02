package com.agendasim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaAdminDTO {
    private Long id;
    private String nomeCliente;
    private String telefoneCliente;
    private Instant dataHora;
    private String status;
    private String observacoes;
    private Long profissionalId;
    private String profissionalNome;
    private Long servicoId;
    private String servicoTitulo;
}
