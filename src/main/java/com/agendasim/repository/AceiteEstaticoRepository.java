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

    Optional<AceiteEstatico> findByUsuarioIdAndTipoDocumentoAndVersao(Long usuarioId, String tipoDocumento, String versao);

    List<AceiteEstatico> findByUsuarioIdAndTipoDocumentoOrderByDataAceiteDesc(Long usuarioId, String tipoDocumento);

    @Query("SELECT COUNT(a) > 0 FROM AceiteEstatico a WHERE a.usuarioId = :usuarioId AND a.tipoDocumento = :tipoDocumento AND a.versao = :versao AND a.aceito = true")
    boolean existsAceiteByUsuarioIdAndTipoAndVersao(@Param("usuarioId") Long usuarioId, @Param("tipoDocumento") String tipoDocumento, @Param("versao") String versao);
}