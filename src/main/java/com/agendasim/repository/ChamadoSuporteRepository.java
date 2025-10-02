package com.agendasim.repository;

import com.agendasim.enums.PrioridadeSuporte;
import com.agendasim.enums.StatusChamado;
import com.agendasim.model.ChamadoSuporte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChamadoSuporteRepository extends JpaRepository<ChamadoSuporte, Long> {
    
    // Buscar chamados por email do usuário
    List<ChamadoSuporte> findByEmailUsuarioOrderByDataCriacaoDesc(String emailUsuario);
    
    // Buscar chamados por empresa
    List<ChamadoSuporte> findByEmpresaIdOrderByDataCriacaoDesc(Long empresaId);
    
    // Buscar chamados por status
    List<ChamadoSuporte> findByStatusOrderByDataCriacaoDesc(StatusChamado status);
    
    // Buscar chamados por prioridade
    List<ChamadoSuporte> findByPrioridadeOrderByDataCriacaoDesc(PrioridadeSuporte prioridade);
    
    // Buscar chamados por categoria
    List<ChamadoSuporte> findByCategoriaOrderByDataCriacaoDesc(String categoria);
    
    // Buscar chamados por período
    List<ChamadoSuporte> findByDataCriacaoBetweenOrderByDataCriacaoDesc(LocalDateTime dataInicio, LocalDateTime dataFim);
    
    // Buscar chamados com filtros combinados
    @Query("SELECT c FROM ChamadoSuporte c WHERE " +
           "(:emailUsuario IS NULL OR c.emailUsuario = :emailUsuario) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:prioridade IS NULL OR c.prioridade = :prioridade) AND " +
           "(:categoria IS NULL OR c.categoria = :categoria) AND " +
           "(:dataInicio IS NULL OR c.dataCriacao >= :dataInicio) AND " +
           "(:dataFim IS NULL OR c.dataCriacao <= :dataFim) AND " +
           "(:empresaId IS NULL OR c.empresa.id = :empresaId)")
    Page<ChamadoSuporte> findChamadosComFiltros(
            @Param("emailUsuario") String emailUsuario,
            @Param("status") StatusChamado status,
            @Param("prioridade") PrioridadeSuporte prioridade,
            @Param("categoria") String categoria,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("empresaId") Long empresaId,
            Pageable pageable);
    
    // Contar chamados por status
    Long countByStatus(StatusChamado status);
    
    // Contar chamados por prioridade
    Long countByPrioridade(PrioridadeSuporte prioridade);
    
    // Contar chamados por categoria
    Long countByCategoria(String categoria);
    
    // Contar chamados por empresa
    Long countByEmpresaId(Long empresaId);
    
    // Contar chamados por email do usuário
    Long countByEmailUsuario(String emailUsuario);
    
    // Buscar chamados abertos por prioridade (para dashboard)
    @Query("SELECT c FROM ChamadoSuporte c WHERE c.status IN ('ABERTO', 'EM_ANDAMENTO') ORDER BY " +
           "CASE c.prioridade " +
           "WHEN 'CRITICA' THEN 1 " +
           "WHEN 'ALTA' THEN 2 " +
           "WHEN 'MEDIA' THEN 3 " +
           "WHEN 'BAIXA' THEN 4 " +
           "END, c.dataCriacao ASC")
    List<ChamadoSuporte> findChamadosAbertosPorPrioridade();
    
    // Buscar chamados por usuário do suporte
    List<ChamadoSuporte> findByUsuarioSuporteOrderByDataCriacaoDesc(String usuarioSuporte);
    
    // Buscar chamados sem resposta há mais de X horas
    @Query("SELECT c FROM ChamadoSuporte c WHERE c.status = 'ABERTO' AND c.dataCriacao < :dataLimite")
    List<ChamadoSuporte> findChamadosSemResposta(@Param("dataLimite") LocalDateTime dataLimite);
    
    // Buscar chamados resolvidos no período
    @Query("SELECT c FROM ChamadoSuporte c WHERE c.status = 'RESOLVIDO' AND c.dataResposta BETWEEN :dataInicio AND :dataFim")
    List<ChamadoSuporte> findChamadosResolvidosNoPeriodo(@Param("dataInicio") LocalDateTime dataInicio, 
                                                         @Param("dataFim") LocalDateTime dataFim);
    
    // Calcular tempo médio de resolução
    @Query("SELECT AVG(TIMESTAMPDIFF(HOUR, c.dataCriacao, c.dataResposta)) FROM ChamadoSuporte c WHERE c.status = 'RESOLVIDO'")
    Double calcularTempoMedioResolucao();
    
    // Calcular avaliação média
    @Query("SELECT AVG(c.avaliacaoNota) FROM ChamadoSuporte c WHERE c.avaliacaoNota IS NOT NULL")
    Double calcularAvaliacaoMedia();
    
    // Buscar chamado por ID e email (para validação de acesso)
    Optional<ChamadoSuporte> findByIdAndEmailUsuario(Long id, String emailUsuario);
    
    // Buscar chamado por ID e empresa (para validação de acesso)
    Optional<ChamadoSuporte> findByIdAndEmpresaId(Long id, Long empresaId);
}
