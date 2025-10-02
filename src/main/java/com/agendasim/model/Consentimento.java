package com.agendasim.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "consentimentos")
public class Consentimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O ID do usuário é obrigatório")
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @NotBlank(message = "O tipo de consentimento é obrigatório")
    @Column(name = "tipo_consentimento", nullable = false, length = 50)
    private String tipoConsentimento;

    @NotBlank(message = "A finalidade é obrigatória")
    @Column(nullable = false, length = 100)
    private String finalidade;

    @NotNull(message = "O status de consentimento é obrigatório")
    @Column(nullable = false)
    private Boolean consentido;

    @Column(name = "data_consentimento")
    private LocalDateTime dataConsentimento;

    @Column(name = "data_revogacao")
    private LocalDateTime dataRevogacao;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 1000)
    private String userAgent;

    @Column(name = "versao_politica")
    private String versaoPolitica;

    @Column(name = "data_criacao", updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
        if (this.consentido && this.dataConsentimento == null) {
            this.dataConsentimento = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
        if (this.consentido && this.dataConsentimento == null) {
            this.dataConsentimento = LocalDateTime.now();
        }
        if (!this.consentido && this.dataRevogacao == null) {
            this.dataRevogacao = LocalDateTime.now();
        }
    }

    public enum TipoConsentimento {
        MARKETING("Comunicações de Marketing"),
        ANALYTICS("Análise de Dados"),
        COOKIES("Cookies e Tecnologias Similares"),
        TERCEIROS("Compartilhamento com Terceiros"),
        PESQUISAS("Pesquisas e Feedback"),
        NOTIFICACOES("Notificações Push"),
        LOCALIZACAO("Dados de Localização"),
        BIOMETRICOS("Dados Biométricos");

        private final String descricao;

        TipoConsentimento(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}
