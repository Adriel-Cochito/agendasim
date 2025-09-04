package com.agendasim.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    private String descricao;

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser maior que zero")
    private Double preco;

    /**
     * Duração do serviço em minutos.
     * Campo essencial para controle de agenda, usado para:
     * - Calcular horários disponíveis dos profissionais
     * - Evitar sobreposição de agendamentos
     * - Planejar a agenda de forma eficiente
     */
    @NotNull(message = "A duração é obrigatória")
    @Positive(message = "A duração deve ser maior que zero")
    private Integer duracao;

    @NotNull(message = "O ID da empresa é obrigatório")
    private Long empresaId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "servico_profissional",
        joinColumns = @JoinColumn(name = "servico_id"),
        inverseJoinColumns = @JoinColumn(name = "profissional_id")
    )
    private List<Profissional> profissionais = new ArrayList<>();


    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "created_at", updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
