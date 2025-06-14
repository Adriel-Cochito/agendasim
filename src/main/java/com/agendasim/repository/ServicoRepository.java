package com.agendasim.repository;

import com.agendasim.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByEmpresaId(Long empresaId);

    Optional<Servico> findByIdAndEmpresaId(Long id, Long empresaId);   // ★ novo
}
