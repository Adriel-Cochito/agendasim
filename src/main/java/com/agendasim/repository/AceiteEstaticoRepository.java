package com.agendasim.repository;

import com.agendasim.model.AceiteEstatico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AceiteEstaticoRepository extends JpaRepository<AceiteEstatico, Long> {

    /**
     * Busca aceite de um usuário para um tipo e versão específicos
     */
    Optional<AceiteEstatico> findByUsuarioIdAndTipoDocumentoAndVersao(Long usuarioId, String tipoDocumento, String versao);

    /**
     * Busca todos os aceites de um usuário
     */
    List<AceiteEstatico> findByUsuarioIdOrderByDataAceiteDesc(Long usuarioId);

    /**
     * Busca aceites de um usuário por tipo de documento
     */
    List<AceiteEstatico> findByUsuarioIdAndTipoDocumentoOrderByDataAceiteDesc(Long usuarioId, String tipoDocumento);

    /**
     * Verifica se usuário aceitou um tipo e versão específicos
     */
    @Query("SELECT COUNT(a) > 0 FROM AceiteEstatico a WHERE a.usuarioId = :usuarioId AND a.tipoDocumento = :tipoDocumento AND a.versao = :versao AND a.aceito = true")
    boolean existsAceiteByUsuarioIdAndTipoAndVersao(@Param("usuarioId") Long usuarioId, @Param("tipoDocumento") String tipoDocumento, @Param("versao") String versao);

    /**
     * Busca aceite mais recente de um usuário para um tipo de documento
     */
    @Query("SELECT a FROM AceiteEstatico a WHERE a.usuarioId = :usuarioId AND a.tipoDocumento = :tipoDocumento ORDER BY a.dataAceite DESC")
    List<AceiteEstatico> findMaisRecenteByUsuarioIdAndTipo(@Param("usuarioId") Long usuarioId, @Param("tipoDocumento") String tipoDocumento);

    /**
     * Exclui aceites por usuário
     */
    void deleteByUsuarioId(Long usuarioId);
}
