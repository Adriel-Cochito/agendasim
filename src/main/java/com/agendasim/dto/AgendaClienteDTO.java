package com.agendasim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaClienteDTO {
    private Instant dataHora;
    private Integer duracao;
}
