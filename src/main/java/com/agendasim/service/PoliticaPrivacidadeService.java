package com.agendasim.service;

import com.agendasim.dto.lgpd.AceitePoliticaDTO;
import com.agendasim.model.AceiteEstatico;
import com.agendasim.repository.AceiteEstaticoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PoliticaPrivacidadeService {

    private final AceiteEstaticoRepository aceiteEstaticoRepository;
    private final AuditoriaService auditoriaService;

    /**
     * Registra aceite de política estática por usuário
     */
    @Transactional
    public void registrarAceiteEstatico(Long usuarioId, String versao, Boolean aceito, String ipAddress, String userAgent) {
        log.info("Registrando aceite de política estática: usuarioId={}, versao={}, aceito={}", usuarioId, versao, aceito);

        // Verificar se já existe aceite para esta versão
        var aceiteExistente = aceiteEstaticoRepository.findByUsuarioIdAndTipoDocumentoAndVersao(usuarioId, "POLITICA", versao);

        if (aceiteExistente.isPresent()) {
            // Atualizar aceite existente
            AceiteEstatico aceite = aceiteExistente.get();
            aceite.setAceito(aceito);
            aceite.setDataAceite(aceito ? LocalDateTime.now() : null);
            aceite.setIpAddress(ipAddress);
            aceite.setUserAgent(userAgent);
            aceiteEstaticoRepository.save(aceite);
        } else {
            // Criar novo aceite
            AceiteEstatico aceite = new AceiteEstatico();
            aceite.setUsuarioId(usuarioId);
            aceite.setTipoDocumento("POLITICA");
            aceite.setVersao(versao);
            aceite.setAceito(aceito);
            aceite.setDataAceite(aceito ? LocalDateTime.now() : null);
            aceite.setIpAddress(ipAddress);
            aceite.setUserAgent(userAgent);
            aceiteEstaticoRepository.save(aceite);
        }

        // Log de auditoria
        auditoriaService.logarAcao(usuarioId, aceito ? "POLITICA_ACEITA" : "POLITICA_REJEITADA", "aceites_estaticos", null, null,
            "Política estática " + (aceito ? "aceita" : "rejeitada") + ": versão " + versao);
    }

    /**
     * Busca aceites de políticas estáticas do usuário
     */
    @Transactional(readOnly = true)
    public List<AceitePoliticaDTO> buscarAceitesEstaticosUsuario(Long usuarioId) {
        log.info("Buscando aceites de políticas estáticas do usuário: {}", usuarioId);

        List<AceiteEstatico> aceites = aceiteEstaticoRepository.findByUsuarioIdAndTipoDocumentoOrderByDataAceiteDesc(usuarioId, "POLITICA");

        return aceites.stream()
            .map(aceite -> {
                AceitePoliticaDTO dto = new AceitePoliticaDTO();
                dto.setId(aceite.getId());
                dto.setPoliticaId(1L); // ID fixo para documentos estáticos
                dto.setVersao(aceite.getVersao());
                dto.setTitulo("Política de Privacidade");
                dto.setAceito(aceite.getAceito());
                dto.setDataAceite(aceite.getDataAceite());
                dto.setVersaoAceita(aceite.getVersao());
                dto.setIpAddress(aceite.getIpAddress());
                dto.setUserAgent(aceite.getUserAgent());
                dto.setDataCriacao(aceite.getDataCriacao());
                return dto;
            })
            .collect(Collectors.toList());
    }

    /**
     * Verifica se usuário aceitou versão específica de política estática
     */
    @Transactional(readOnly = true)
    public boolean verificarAceiteEstaticoUsuario(Long usuarioId, String versao) {
        return aceiteEstaticoRepository.existsAceiteByUsuarioIdAndTipoAndVersao(usuarioId, "POLITICA", versao);
    }
}