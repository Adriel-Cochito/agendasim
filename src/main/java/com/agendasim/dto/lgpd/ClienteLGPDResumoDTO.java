package com.agendasim.dto.lgpd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteLGPDResumoDTO {
    private boolean existe;
    private int totalAgendamentos;
    private TiposDadosDTO tiposDados;
    private List<EmpresaResumoDTO> empresas;
    private PeriodoResumoDTO periodo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TiposDadosDTO {
        private boolean nome;
        private boolean telefone;
        private boolean email;
        private boolean endereco;
        private boolean cpf;
        private boolean observacoes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmpresaResumoDTO {
        private Long id;
        private String nome;
        private int totalAgendamentos;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PeriodoResumoDTO {
        private String primeiroAgendamento;
        private String ultimoAgendamento;
    }
}
