package com.agendasim.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertasDTO {
    private Long agendamentosParaConfirmar;
    private Long conflitosHorario;
    private List<ProfissionalSemAgendaDTO> profissionaisSemAgenda;
}
