package com.agendasim.controller;

import com.agendasim.dto.dashboard.*;
import com.agendasim.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dash")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // Endpoint principal que retorna todas as métricas de uma vez - otimizado
    @GetMapping
    public ResponseEntity<DashboardResumoDTO> obterResumo(@RequestParam Long empresaId) {
        return ResponseEntity.ok(dashboardService.obterResumo(empresaId));
    }

    // Endpoints específicos para consultas individuais (opcional)
    
    @GetMapping("/metricas")
    public ResponseEntity<MetricasPrincipaisDTO> obterMetricasPrincipais(@RequestParam Long empresaId) {
        return ResponseEntity.ok(dashboardService.obterMetricasPrincipais(empresaId));
    }

    @GetMapping("/indicadores")
    public ResponseEntity<IndicadoresGestaoDTO> obterIndicadoresGestao(@RequestParam Long empresaId) {
        return ResponseEntity.ok(dashboardService.obterIndicadoresGestao(empresaId));
    }

    @GetMapping("/graficos")
    public ResponseEntity<GraficosDTO> obterGraficos(@RequestParam Long empresaId) {
        return ResponseEntity.ok(dashboardService.obterGraficos(empresaId));
    }

    @GetMapping("/alertas")
    public ResponseEntity<AlertasDTO> obterAlertas(@RequestParam Long empresaId) {
        return ResponseEntity.ok(dashboardService.obterAlertas(empresaId));
    }
}