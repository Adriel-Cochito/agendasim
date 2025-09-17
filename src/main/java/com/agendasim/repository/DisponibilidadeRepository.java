package com.agendasim.repository;

import com.agendasim.model.Disponibilidade;
import com.agendasim.enums.TipoDisponibilidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DisponibilidadeRepository extends JpaRepository<Disponibilidade, Long> {
    List<Disponibilidade> findByEmpresaIdAndProfissionalId(Long empresaId, Long profissionalId);

    List<Disponibilidade> findByTipoAndEmpresaIdAndProfissionalId(TipoDisponibilidade tipo, Long empresaId,
            Long profissionalId);

    List<Disponibilidade> findByProfissionalIdAndDataHoraInicioLessThanEqualAndDataHoraFimGreaterThanEqual(
            Long profissionalId, LocalDateTime fim, LocalDateTime inicio);

    // Query para contar disponibilidades ativas (GRADE e LIBERADO, excluindo BLOQUEIO e BLOQUEIO_GRADE)
    @Query("SELECT COUNT(d) FROM Disponibilidade d WHERE d.empresa.id = :empresaId AND d.tipo IN ('GRADE', 'LIBERADO')")
    Long countDisponibilidadesAtivas(@Param("empresaId") Long empresaId);

    // Queries de conflito de bloqueio
    @Query("SELECT COUNT(d) FROM Disponibilidade d WHERE d.empresa.id = :empresaId AND d.profissional.id = :profissionalId AND d.tipo IN ('BLOQUEIO', 'BLOQUEIO_GRADE')")
    Long countConflitoBloqueio(@Param("empresaId") Long empresaId, @Param("profissionalId") Long profissionalId);

    // Query para listar disponibilidades por empresa, profissional e data
    @Query("SELECT d FROM Disponibilidade d WHERE d.empresa.id = :empresaId AND d.profissional.id = :profissionalId")
    List<Disponibilidade> findByEmpresaProfissionalEData(@Param("empresaId") Long empresaId, @Param("profissionalId") Long profissionalId);

    // Query para verificar conflito de disponibilidade
    @Query("SELECT COUNT(d) FROM Disponibilidade d WHERE d.profissional.id = :profissionalId AND d.empresa.id = :empresaId AND d.tipo IN ('BLOQUEIO', 'BLOQUEIO_GRADE') AND d.id != :disponibilidadeId AND SIZE(d.diasSemana) > 0")
    Long countConflitoDisponibilidadeGrade(@Param("profissionalId") Long profissionalId, @Param("empresaId") Long empresaId, @Param("disponibilidadeId") Long disponibilidadeId);

    @Query("SELECT COUNT(d) FROM Disponibilidade d WHERE d.profissional.id = :profissionalId AND d.empresa.id = :empresaId AND d.tipo IN ('BLOQUEIO', 'BLOQUEIO_GRADE') AND d.id != :disponibilidadeId AND d.dataHoraInicio IS NOT NULL AND d.dataHoraFim IS NOT NULL AND (d.dataHoraInicio < :dataHoraFim AND d.dataHoraFim > :dataHoraInicio)")
    Long countConflitoDisponibilidadeIntervalo(@Param("profissionalId") Long profissionalId, @Param("empresaId") Long empresaId, @Param("disponibilidadeId") Long disponibilidadeId, @Param("dataHoraInicio") LocalDateTime dataHoraInicio, @Param("dataHoraFim") LocalDateTime dataHoraFim);
}
