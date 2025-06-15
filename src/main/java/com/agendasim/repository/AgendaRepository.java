package com.agendasim.repository;

import com.agendasim.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByServicoId(Long servicoId);
    List<Agenda> findByEmpresaId(Long empresaId);
    List<Agenda> findByServicoIdAndEmpresaId(Long servicoId, Long empresaId);
    Optional<Agenda> findByIdAndEmpresaId(Long id, Long empresaId);
}