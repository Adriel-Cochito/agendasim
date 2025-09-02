package com.agendasim.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndicadoresGestaoDTO {
    private Long profissionaisAtivos;
    private Long servicosOferecidos;
}