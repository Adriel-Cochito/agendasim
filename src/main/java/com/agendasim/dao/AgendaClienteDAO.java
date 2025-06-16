package com.agendasim.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaClienteDAO {
    private LocalDateTime dataHora;
    private Integer duracao;
}
