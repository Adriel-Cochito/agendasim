package com.agendasim.dto.lgpd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DadosPessoaisDTO {

    private Long usuarioId;
    
    private String nome;
    
    private String email;
    
    private String telefone;
    
    private String perfil;
    
    private EmpresaDTO empresa;
    
    private List<AgendamentoDTO> agendamentos;
    
    private LocalDateTime dataColeta;
    
    private LocalDateTime ultimaAtualizacao;
    
    private List<ConsentimentoDTO> consentimentos;
    
    private List<AceiteDTO> aceites;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmpresaDTO {
        private Long id;
        private String nome;
        private String email;
        private String telefone;
        private String cnpj;
        private LocalDateTime dataCriacao;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgendamentoDTO {
        private Long id;
        private String nomeCliente;
        private String telefoneCliente;
        private String servico;
        private LocalDateTime dataHora;
        private String status;
        private LocalDateTime dataCriacao;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AceiteDTO {
        private String tipo; // TERMOS ou POLITICA
        private String versao;
        private Boolean aceito;
        private LocalDateTime dataAceite;
    }
}
