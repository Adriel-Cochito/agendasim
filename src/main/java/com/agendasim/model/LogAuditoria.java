package com.agendasim.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "log_auditoria")
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @NotBlank(message = "A ação é obrigatória")
    @Column(nullable = false, length = 100)
    private String acao;

    @Column(name = "tabela_afetada", length = 50)
    private String tabelaAfetada;

    @Column(name = "registro_id")
    private Long registroId;

    @Column(name = "dados_anteriores", columnDefinition = "TEXT")
    private String dadosAnteriores;

    @Column(name = "dados_novos", columnDefinition = "TEXT")
    private String dadosNovos;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 1000)
    private String userAgent;

    @Column(name = "sessao_id", length = 100)
    private String sessaoId;

    @Column(name = "trace_id", length = 50)
    private String traceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_risco")
    private NivelRisco nivelRisco = NivelRisco.BAIXO;

    @Column(name = "data_operacao", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dataOperacao;

    @PrePersist
    protected void onCreate() {
        this.dataOperacao = LocalDateTime.now();
    }

    public enum NivelRisco {
        BAIXO("Baixo Risco"),
        MEDIO("Médio Risco"),
        ALTO("Alto Risco"),
        CRITICO("Crítico");

        private final String descricao;

        NivelRisco(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public enum TipoAcao {
        // Ações de Autenticação
        LOGIN("Login realizado"),
        LOGOUT("Logout realizado"),
        LOGIN_FALHADO("Tentativa de login falhada"),
        
        // Ações de Dados Pessoais
        DADOS_ACESSADOS("Dados pessoais acessados"),
        DADOS_MODIFICADOS("Dados pessoais modificados"),
        DADOS_EXCLUIDOS("Dados pessoais excluídos"),
        DADOS_EXPORTADOS("Dados pessoais exportados"),
        DADOS_ANONIMIZADOS("Dados pessoais anonimizados"),
        
        // Ações de Consentimento
        CONSENTIMENTO_DADO("Consentimento concedido"),
        CONSENTIMENTO_REVOGADO("Consentimento revogado"),
        CONSENTIMENTO_MODIFICADO("Consentimento modificado"),
        
        // Ações de Termos e Políticas
        TERMOS_ACEITOS("Termos de uso aceitos"),
        POLITICA_ACEITA("Política de privacidade aceita"),
        TERMOS_ATUALIZADOS("Termos de uso atualizados"),
        POLITICA_ATUALIZADA("Política de privacidade atualizada"),
        
        // Ações de Sistema
        CONFIGURACAO_ALTERADA("Configuração do sistema alterada"),
        USUARIO_CRIADO("Usuário criado"),
        USUARIO_ATIVADO("Usuário ativado"),
        USUARIO_DESATIVADO("Usuário desativado"),
        
        // Ações de Segurança
        ACESSO_NEGADO("Acesso negado"),
        TENTATIVA_NAO_AUTORIZADA("Tentativa de acesso não autorizada"),
        DADOS_SENSIVEIS_ACESSADOS("Dados sensíveis acessados");

        private final String descricao;

        TipoAcao(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}
