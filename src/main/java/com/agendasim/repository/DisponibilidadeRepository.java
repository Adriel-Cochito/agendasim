package com.agendasim.repository;

import com.agendasim.model.Disponibilidade;
import com.agendasim.enums.TipoDisponibilidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DisponibilidadeRepository extends JpaRepository<Disponibilidade, Long> {
    List<Disponibilidade> findByEmpresaIdAndProfissionalId(Long empresaId, Long profissionalId);
    List<Disponibilidade> findByTipoAndEmpresaIdAndProfissionalId(TipoDisponibilidade tipo, Long empresaId, Long profissionalId);
    List<Disponibilidade> findByProfissionalIdAndDataHoraInicioLessThanEqualAndDataHoraFimGreaterThanEqual(
        Long profissionalId, LocalDateTime fim, LocalDateTime inicio);
}
