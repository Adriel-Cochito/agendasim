package com.agendasim.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraficosDTO {
    private List<AgendamentoPorDiaDTO> agendamentosPorDia;
    private List<ServicoRankingDTO> servicosMaisProcurados;
    private List<ProfissionalRankingDTO> profissionaisMaisOcupados;
    private Map<String, Long> statusAgendamentos;
}
