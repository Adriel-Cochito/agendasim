package com.agendasim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaClienteDTO {
    private LocalDateTime dataHora;
    private Integer duracao;
}
