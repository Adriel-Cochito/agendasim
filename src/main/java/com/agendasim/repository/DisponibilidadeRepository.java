package com.agendasim.repository;

import com.agendasim.model.Disponibilidade;
import com.agendasim.enums.TipoDisponibilidade;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
