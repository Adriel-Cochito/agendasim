package com.agendasim.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfissionalRankingDTO {
    private Long profissionalId;
    private String profissionalNome;
    private Long quantidade;
}
