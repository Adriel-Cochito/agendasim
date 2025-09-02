package com.agendasim.repository;

import com.agendasim.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByEmpresaId(Long empresaId);

    Optional<Servico> findByIdAndEmpresaId(Long id, Long empresaId); // ★ novo

    // Contagem de serviços ativos
    @Query("SELECT COUNT(s) FROM Servico s WHERE s.empresaId = :empresaId AND s.ativo = true")
    Long countByEmpresaIdAndAtivoTrue(@Param("empresaId") Long empresaId);

}
