package com.agendasim.repository;

import com.agendasim.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    List<Profissional> findByEmpresaId(Long empresaId);

    Optional<Profissional> findByEmail(String email);

    // Contagem de profissionais ativos - baseado na estrutura real
    @Query("SELECT COUNT(p) FROM Profissional p WHERE p.empresaId = :empresaId AND p.ativo = true")
    Long countByEmpresaIdAndAtivoTrue(@Param("empresaId") Long empresaId);

    // Profissionais sem disponibilidade configurada - versão simplificada
    @Query(value = "SELECT p.id, p.nome FROM profissionais p WHERE p.empresa_id = :empresaId AND p.ativo = true AND p.id NOT IN (SELECT DISTINCT profissional_id FROM disponibilidades WHERE empresa_id = :empresaId AND profissional_id IS NOT NULL)", nativeQuery = true)
    List<Object[]> findProfissionaisSemDisponibilidade(@Param("empresaId") Long empresaId);

}
