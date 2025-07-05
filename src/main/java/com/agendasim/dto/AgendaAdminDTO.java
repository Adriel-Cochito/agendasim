package com.agendasim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaAdminDTO {
    private Long id;
    private String nomeCliente;
    private String telefoneCliente;
    private LocalDateTime dataHora;
    private String status;
    private Long profissionalId;
    private String profissionalNome;
    private Long servicoId;
    private String servicoTitulo;
}
