package com.agendasim.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricasPrincipaisDTO {
    private AgendamentosHojeDTO agendamentosHoje;
    private Long proximosSeteDias;
    private Double taxaOcupacaoHoje;
}