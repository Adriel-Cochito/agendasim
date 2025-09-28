package com.agendasim.repository;

import com.agendasim.model.Consentimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsentimentoRepository extends JpaRepository<Consentimento, Long> {

    /**
     * Busca consentimento ativo de um usuário para um tipo específico
     */
    @Query("SELECT c FROM Consentimento c WHERE c.usuarioId = :usuarioId AND c.tipoConsentimento = :tipo AND c.consentido = true AND c.dataRevogacao IS NULL")
    Optional<Consentimento> findAtivoByUsuarioIdAndTipo(@Param("usuarioId") Long usuarioId, @Param("tipo") String tipo);

    /**
     * Busca todos os consentimentos ativos de um usuário
     */
    @Query("SELECT c FROM Consentimento c WHERE c.usuarioId = :usuarioId AND c.consentido = true AND c.dataRevogacao IS NULL ORDER BY c.dataConsentimento DESC")
    List<Consentimento> findAtivosByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca todos os consentimentos de um usuário (incluindo revogados)
     */
    List<Consentimento> findByUsuarioIdOrderByDataConsentimentoDesc(Long usuarioId);

    /**
     * Verifica se usuário deu consentimento para um tipo específico
     */
    @Query("SELECT COUNT(c) > 0 FROM Consentimento c WHERE c.usuarioId = :usuarioId AND c.tipoConsentimento = :tipo AND c.consentido = true AND c.dataRevogacao IS NULL")
    boolean existsConsentimentoAtivo(@Param("usuarioId") Long usuarioId, @Param("tipo") String tipo);

    /**
     * Busca consentimentos por tipo
     */
    List<Consentimento> findByTipoConsentimentoOrderByDataConsentimentoDesc(String tipoConsentimento);

    /**
     * Busca consentimentos por finalidade
     */
    List<Consentimento> findByFinalidadeOrderByDataConsentimentoDesc(String finalidade);

    /**
     * Conta consentimentos ativos por tipo
     */
    @Query("SELECT c.tipoConsentimento, COUNT(c) FROM Consentimento c WHERE c.consentido = true AND c.dataRevogacao IS NULL GROUP BY c.tipoConsentimento")
    List<Object[]> countAtivosByTipo();

    /**
     * Busca consentimentos em um período específico
     */
    @Query("SELECT c FROM Consentimento c WHERE c.dataConsentimento BETWEEN :inicio AND :fim ORDER BY c.dataConsentimento DESC")
    List<Consentimento> findByPeriodoConsentimento(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    /**
     * Busca consentimentos revogados em um período
     */
    @Query("SELECT c FROM Consentimento c WHERE c.dataRevogacao BETWEEN :inicio AND :fim ORDER BY c.dataRevogacao DESC")
    List<Consentimento> findRevogadosByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    /**
     * Busca consentimentos por IP
     */
    List<Consentimento> findByIpAddressOrderByDataConsentimentoDesc(String ipAddress);

    /**
     * Busca estatísticas de consentimento
     */
    @Query("SELECT c.tipoConsentimento, " +
           "COUNT(c) as total, " +
           "SUM(CASE WHEN c.consentido = true THEN 1 ELSE 0 END) as consentidos, " +
           "SUM(CASE WHEN c.dataRevogacao IS NOT NULL THEN 1 ELSE 0 END) as revogados " +
           "FROM Consentimento c GROUP BY c.tipoConsentimento")
    List<Object[]> findEstatisticasConsentimento();

    /**
     * Busca usuários que não deram consentimento para um tipo específico
     */
    @Query("SELECT DISTINCT p.id FROM Profissional p WHERE p.id NOT IN " +
           "(SELECT c.usuarioId FROM Consentimento c WHERE c.tipoConsentimento = :tipo AND c.consentido = true AND c.dataRevogacao IS NULL)")
    List<Long> findUsuariosSemConsentimento(@Param("tipo") String tipo);

    /**
     * Busca consentimentos que precisam ser renovados (mais de 1 ano)
     */
    @Query("SELECT c FROM Consentimento c WHERE c.consentido = true AND c.dataRevogacao IS NULL AND c.dataConsentimento < :dataLimite")
    List<Consentimento> findNecessitamRenovacao(@Param("dataLimite") LocalDateTime dataLimite);
    
    /**
     * Exclui consentimentos por usuário
     */
    void deleteByUsuarioId(Long usuarioId);
}
