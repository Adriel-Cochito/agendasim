package com.agendasim.dto.lgpd;

import com.agendasim.model.LogAuditoria;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogAuditoriaDTO {

    private Long id;
    
    private Long usuarioId;
    
    private String acao;
    
    private String tabelaAfetada;
    
    private Long registroId;
    
    private String dadosAnteriores;
    
    private String dadosNovos;
    
    private String ipAddress;
    
    private String userAgent;
    
    private String sessaoId;
    
    private String traceId;
    
    private LogAuditoria.NivelRisco nivelRisco;
    
    private LocalDateTime dataOperacao;
    
    private String descricaoAcao;
    
    // DTOs para relatórios
    @Data
    public static class EstatisticasDTO {
        private String acao;
        private Long total;
        private String percentual;
    }
    
    @Data
    public static class RelatorioPeriodoDTO {
        private LocalDateTime data;
        private Long total;
        private Long criticos;
        private Long altos;
        private Long baixos;
    }
    
    @Data
    public static class ResumoAuditoriaDTO {
        private Long totalLogs;
        private Long logsCriticos;
        private Long logsAltos;
        private Long logsMedios;
        private Long logsBaixos;
        private Long acessosDadosPessoais;
        private Long violacoesSeguranca;
        private Long acoesLGPD;
    }
}
