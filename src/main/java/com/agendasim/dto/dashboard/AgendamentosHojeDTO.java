package com.agendasim.dto.dashboard;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentosHojeDTO {
    private Long total;
    private Map<String, Long> porStatus; // CONFIRMADO: 5, REALIZADO: 3, CANCELADO: 1, AGENDADO: 2
}
