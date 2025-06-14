package com.agendasim.repository;

import com.agendasim.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByEmpresaId(Long empresaId);
    List<Agenda> findByServicoId(Long servicoId);
}
