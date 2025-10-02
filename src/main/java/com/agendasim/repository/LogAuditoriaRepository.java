package com.agendasim.repository;

import com.agendasim.model.LogAuditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {

    /**
     * Busca logs de um usuário específico
     */
    Page<LogAuditoria> findByUsuarioIdOrderByDataOperacaoDesc(Long usuarioId, Pageable pageable);

    /**
     * Busca logs por ação específica
     */
    Page<LogAuditoria> findByAcaoOrderByDataOperacaoDesc(String acao, Pageable pageable);

    /**
     * Busca logs por tabela afetada
     */
    Page<LogAuditoria> findByTabelaAfetadaOrderByDataOperacaoDesc(String tabelaAfetada, Pageable pageable);

    /**
     * Busca logs por nível de risco
     */
    Page<LogAuditoria> findByNivelRiscoOrderByDataOperacaoDesc(LogAuditoria.NivelRisco nivelRisco, Pageable pageable);

    /**
     * Busca logs em um período específico
     */
    @Query("SELECT l FROM LogAuditoria l WHERE l.dataOperacao BETWEEN :inicio AND :fim ORDER BY l.dataOperacao DESC")
    Page<LogAuditoria> findByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim, Pageable pageable);

    /**
     * Busca logs por IP
     */
    Page<LogAuditoria> findByIpAddressOrderByDataOperacaoDesc(String ipAddress, Pageable pageable);

    /**
     * Busca logs por sessão
     */
    Page<LogAuditoria> findBySessaoIdOrderByDataOperacaoDesc(String sessaoId, Pageable pageable);

    /**
     * Busca logs por trace ID
     */
    List<LogAuditoria> findByTraceIdOrderByDataOperacaoAsc(String traceId);

    /**
     * Busca logs de ações críticas
     */
    @Query("SELECT l FROM LogAuditoria l WHERE l.nivelRisco IN ('ALTO', 'CRITICO') ORDER BY l.dataOperacao DESC")
    Page<LogAuditoria> findAcoesCriticas(Pageable pageable);

    /**
     * Busca logs de acesso a dados pessoais
     */
    @Query("SELECT l FROM LogAuditoria l WHERE l.acao IN ('DADOS_ACESSADOS', 'DADOS_MODIFICADOS', 'DADOS_EXCLUIDOS', 'DADOS_EXPORTADOS') ORDER BY l.dataOperacao DESC")
    Page<LogAuditoria> findAcessosDadosPessoais(Pageable pageable);

    /**
     * Busca logs de violações de segurança
     */
    @Query("SELECT l FROM LogAuditoria l WHERE l.acao IN ('ACESSO_NEGADO', 'TENTATIVA_NAO_AUTORIZADA', 'LOGIN_FALHADO') ORDER BY l.dataOperacao DESC")
    Page<LogAuditoria> findViolacoesSeguranca(Pageable pageable);

    /**
     * Conta logs por ação
     */
    @Query("SELECT l.acao, COUNT(l) FROM LogAuditoria l GROUP BY l.acao ORDER BY COUNT(l) DESC")
    List<Object[]> countByAcao();

    /**
     * Conta logs por nível de risco
     */
    @Query("SELECT l.nivelRisco, COUNT(l) FROM LogAuditoria l GROUP BY l.nivelRisco")
    List<Object[]> countByNivelRisco();

    /**
     * Busca logs de um registro específico
     */
    @Query("SELECT l FROM LogAuditoria l WHERE l.tabelaAfetada = :tabela AND l.registroId = :registroId ORDER BY l.dataOperacao DESC")
    List<LogAuditoria> findByRegistro(@Param("tabela") String tabela, @Param("registroId") Long registroId);

    /**
     * Busca logs de auditoria de LGPD
     */
    @Query("SELECT l FROM LogAuditoria l WHERE l.acao IN " +
           "('CONSENTIMENTO_DADO', 'CONSENTIMENTO_REVOGADO', 'TERMOS_ACEITOS', 'POLITICA_ACEITA', " +
           "'DADOS_ACESSADOS', 'DADOS_MODIFICADOS', 'DADOS_EXCLUIDOS', 'DADOS_EXPORTADOS', 'DADOS_ANONIMIZADOS') " +
           "ORDER BY l.dataOperacao DESC")
    Page<LogAuditoria> findLogsLGPD(Pageable pageable);

    /**
     * Busca estatísticas de auditoria por período
     */
    @Query("SELECT DATE(l.dataOperacao) as data, COUNT(l) as total, " +
           "SUM(CASE WHEN l.nivelRisco = 'CRITICO' THEN 1 ELSE 0 END) as criticos, " +
           "SUM(CASE WHEN l.nivelRisco = 'ALTO' THEN 1 ELSE 0 END) as altos " +
           "FROM LogAuditoria l WHERE l.dataOperacao BETWEEN :inicio AND :fim " +
           "GROUP BY DATE(l.dataOperacao) ORDER BY data DESC")
    List<Object[]> findEstatisticasPorPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    /**
     * Busca logs de usuários inativos (sem atividade recente)
     */
    @Query("SELECT DISTINCT l.usuarioId FROM LogAuditoria l WHERE l.usuarioId IS NOT NULL " +
           "AND l.usuarioId NOT IN " +
           "(SELECT DISTINCT l2.usuarioId FROM LogAuditoria l2 WHERE l2.dataOperacao > :dataLimite)")
    List<Long> findUsuariosInativos(@Param("dataLimite") LocalDateTime dataLimite);
}
