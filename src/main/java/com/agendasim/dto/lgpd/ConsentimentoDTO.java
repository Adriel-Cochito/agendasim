package com.agendasim.dto.lgpd;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsentimentoDTO {

    private Long id;
    
    private Long usuarioId;
    
    @NotBlank(message = "O tipo de consentimento é obrigatório")
    private String tipoConsentimento;
    
    @NotBlank(message = "A finalidade é obrigatória")
    private String finalidade;
    
    @NotNull(message = "O status de consentimento é obrigatório")
    private Boolean consentido;
    
    private LocalDateTime dataConsentimento;
    
    private LocalDateTime dataRevogacao;
    
    private String ipAddress;
    
    private String versaoPolitica;
    
    private LocalDateTime dataCriacao;
    
    private LocalDateTime dataAtualizacao;
    
    private String descricaoTipo;
    
    // DTOs para operações específicas
    @Data
    public static class CriarConsentimentoDTO {
        @NotBlank(message = "O tipo de consentimento é obrigatório")
        private String tipoConsentimento;
        
        @NotBlank(message = "A finalidade é obrigatória")
        private String finalidade;
        
        @NotNull(message = "O status de consentimento é obrigatório")
        private Boolean consentido;
    }
    
    @Data
    public static class AtualizarConsentimentoDTO {
        @NotNull(message = "O status de consentimento é obrigatório")
        private Boolean consentido;
    }
    
    @Data
    public static class ConsentimentoResumoDTO {
        private String tipoConsentimento;
        private String descricaoTipo;
        private Boolean consentido;
        private LocalDateTime dataConsentimento;
        private LocalDateTime dataRevogacao;
        private Boolean podeRevogar;
    }
}
