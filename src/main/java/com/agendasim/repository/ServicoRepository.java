package com.agendasim.repository;

import com.agendasim.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByEmpresaId(Long empresaId); // Ex: listar serviços de uma empresa
}
