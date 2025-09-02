package com.agendasim.dto.dashboard;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoPorDiaDTO {
    private LocalDate data;
    private Long quantidade;
}
