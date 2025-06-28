package com.agendasim.repository;

import com.agendasim.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    List<Profissional> findByEmpresaId(Long empresaId);
    Optional<Profissional> findByEmail(String email);

}
