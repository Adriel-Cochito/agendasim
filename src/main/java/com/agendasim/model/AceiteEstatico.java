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
@Table(name = "aceites_estaticos")
public class AceiteEstatico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O ID do usuário é obrigatório")
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @NotBlank(message = "O tipo de documento é obrigatório")
    @Column(name = "tipo_documento", nullable = false, length = 50)
    private String tipoDocumento; // Ex: TERMO, POLITICA

    @NotBlank(message = "A versão do documento é obrigatória")
    @Column(nullable = false, length = 20)
    private String versao;

    @NotNull(message = "O status de aceite é obrigatório")
    @Column(nullable = false)
    private Boolean aceito;

    @Column(name = "data_aceite")
    private LocalDateTime dataAceite;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 1000)
    private String userAgent;

    @Column(name = "data_criacao", updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
        if (this.aceito && this.dataAceite == null) {
            this.dataAceite = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (this.aceito && this.dataAceite == null) {
            this.dataAceite = LocalDateTime.now();
        }
    }
}