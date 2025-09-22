package com.agendasim.model;

import com.agendasim.enums.TipoDisponibilidade;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table(name = "disponibilidades")
public class Disponibilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoDisponibilidade tipo;

    // Para BLOQUEIO e LIBERADO
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;

    // Para GRADE e BLOQUEIO_GRADE
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "disponibilidade_dias_semana", joinColumns = @JoinColumn(name = "disponibilidade_id"))
    @Column(name = "dia_semana")
    private List<Integer> diasSemana; // 0=Dom, 1=Seg, ..., 6=Sáb

    private LocalTime horaInicio; // apenas para GRADE e BLOQUEIO_GRADE
    private LocalTime horaFim; // apenas para GRADE e BLOQUEIO_GRADE

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profissional_id")
    @NotNull
    private Profissional profissional;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empresa_id")
    @NotNull
    private Empresa empresa;

    private String observacao;

    // ============================
    // Validações condicionais
    // ============================

    @AssertTrue(message = "Para GRADE e BLOQUEIO_GRADE, deve informar diasSemana, horaInicio e horaFim, e deixar dataHoraInicio/Fim nulos")
    public boolean isGradeValida() {
        if (tipo != TipoDisponibilidade.GRADE && tipo != TipoDisponibilidade.BLOQUEIO_GRADE)
            return true;

        // Se diasSemana estiver null aqui, assume que ainda está sendo populado e não
        // valida agora
        if (diasSemana == null)
            return true;

        return !diasSemana.isEmpty()
                && horaInicio != null
                && horaFim != null
                && horaInicio.isBefore(horaFim)
                && dataHoraInicio == null
                && dataHoraFim == null;
    }

    @AssertTrue(message = "Para BLOQUEIO ou LIBERADO, dataHoraInicio e dataHoraFim são obrigatórios, e grade deve estar nula")
    public boolean isPontoValido() {
        if (tipo == TipoDisponibilidade.BLOQUEIO || tipo == TipoDisponibilidade.LIBERADO) {
            return dataHoraInicio != null
                    && dataHoraFim != null
                    && (diasSemana == null || diasSemana.isEmpty())
                    && horaInicio == null
                    && horaFim == null;
        }
        return true;
    }

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
