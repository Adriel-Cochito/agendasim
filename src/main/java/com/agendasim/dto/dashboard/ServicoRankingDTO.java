package com.agendasim.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicoRankingDTO {
    private Long servicoId;
    private String servicoTitulo;
    private Long quantidade;
}
