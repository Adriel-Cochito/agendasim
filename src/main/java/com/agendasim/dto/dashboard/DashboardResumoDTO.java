package com.agendasim.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResumoDTO {
    private MetricasPrincipaisDTO metricas;
    private IndicadoresGestaoDTO indicadores;
    private GraficosDTO graficos;
    private AlertasDTO alertas;
}