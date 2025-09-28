package com.agendasim.dto.lgpd;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AceitePoliticaDTO {
    
    private Long id;
    
    private Long politicaId;
    
    private String versao;
    
    private String titulo;
    
    private Boolean aceito;
    
    private LocalDateTime dataAceite;
    
    private String versaoAceita;
    
    private String ipAddress;
    
    private String userAgent;
    
    private LocalDateTime dataCriacao;
}
