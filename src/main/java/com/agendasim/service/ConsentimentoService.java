package com.agendasim.service;

import com.agendasim.dto.lgpd.ConsentimentoDTO;
import com.agendasim.model.Consentimento;
import com.agendasim.repository.ConsentimentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsentimentoService {

    private final ConsentimentoRepository consentimentoRepository;
    private final AuditoriaService auditoriaService;

    /**
     * Registra um novo consentimento
     */
    @Transactional
    public Consentimento registrarConsentimento(Long usuarioId, ConsentimentoDTO.CriarConsentimentoDTO dto, String ipAddress, String userAgent) {
        log.info("Registrando consentimento para usuário {}: tipo={}, finalidade={}, consentido={}", 
                usuarioId, dto.getTipoConsentimento(), dto.getFinalidade(), dto.getConsentido());
        
        // Revogar consentimento anterior se existir
        revogarConsentimentoAnterior(usuarioId, dto.getTipoConsentimento());
        
        Consentimento consentimento = new Consentimento();
        consentimento.setUsuarioId(usuarioId);
        consentimento.setTipoConsentimento(dto.getTipoConsentimento());
        consentimento.setFinalidade(dto.getFinalidade());
        consentimento.setConsentido(dto.getConsentido());
        consentimento.setIpAddress(ipAddress);
        consentimento.setUserAgent(userAgent);
        consentimento.setVersaoPolitica(obterVersaoPoliticaAtual());
        
        Consentimento salvo = consentimentoRepository.save(consentimento);
        
        // Log de auditoria
        String acao = dto.getConsentido() ? "CONSENTIMENTO_DADO" : "CONSENTIMENTO_NEGADO";
        auditoriaService.logarAcao(usuarioId, acao, "consentimentos", salvo.getId(), null, dto.toString());
        
        return salvo;
    }

    /**
     * Atualiza um consentimento existente
     */
    @Transactional
    public Consentimento atualizarConsentimento(Long usuarioId, String tipoConsentimento, 
                                              ConsentimentoDTO.AtualizarConsentimentoDTO dto, 
                                              String ipAddress, String userAgent) {
        log.info("Atualizando consentimento para usuário {}: tipo={}, consentido={}", 
                usuarioId, tipoConsentimento, dto.getConsentido());
        
        Optional<Consentimento> consentimentoOpt = consentimentoRepository.findAtivoByUsuarioIdAndTipo(usuarioId, tipoConsentimento);
        
        if (consentimentoOpt.isEmpty()) {
            throw new RuntimeException("Consentimento não encontrado");
        }
        
        Consentimento consentimento = consentimentoOpt.get();
        Boolean consentidoAnterior = consentimento.getConsentido();
        
        consentimento.setConsentido(dto.getConsentido());
        consentimento.setIpAddress(ipAddress);
        consentimento.setUserAgent(userAgent);
        
        if (!dto.getConsentido() && consentidoAnterior) {
            consentimento.setDataRevogacao(LocalDateTime.now());
        } else if (dto.getConsentido() && !consentidoAnterior) {
            consentimento.setDataConsentimento(LocalDateTime.now());
            consentimento.setDataRevogacao(null);
        }
        
        Consentimento salvo = consentimentoRepository.save(consentimento);
        
        // Log de auditoria
        String acao = dto.getConsentido() ? "CONSENTIMENTO_DADO" : "CONSENTIMENTO_REVOGADO";
        auditoriaService.logarAcao(usuarioId, acao, "consentimentos", salvo.getId(), 
                                 consentidoAnterior.toString(), dto.getConsentido().toString());
        
        return salvo;
    }

    /**
     * Revoga um consentimento
     */
    @Transactional
    public void revogarConsentimento(Long usuarioId, String tipoConsentimento) {
        log.info("Revogando consentimento para usuário {}: tipo={}", usuarioId, tipoConsentimento);
        
        Optional<Consentimento> consentimentoOpt = consentimentoRepository.findAtivoByUsuarioIdAndTipo(usuarioId, tipoConsentimento);
        
        if (consentimentoOpt.isEmpty()) {
            throw new RuntimeException("Consentimento não encontrado");
        }
        
        Consentimento consentimento = consentimentoOpt.get();
        consentimento.setConsentido(false);
        consentimento.setDataRevogacao(LocalDateTime.now());
        
        consentimentoRepository.save(consentimento);
        
        // Log de auditoria
        auditoriaService.logarAcao(usuarioId, "CONSENTIMENTO_REVOGADO", "consentimentos", consentimento.getId(), 
                                 "true", "false");
    }

    /**
     * Busca consentimentos ativos de um usuário
     */
    @Transactional(readOnly = true)
    public List<ConsentimentoDTO.ConsentimentoResumoDTO> buscarConsentimentosAtivos(Long usuarioId) {
        List<Consentimento> consentimentos = consentimentoRepository.findAtivosByUsuarioId(usuarioId);
        
        return consentimentos.stream()
            .map(this::mapearParaResumo)
            .toList();
    }

    /**
     * Busca todos os consentimentos de um usuário
     */
    @Transactional(readOnly = true)
    public List<Consentimento> buscarConsentimentosUsuario(Long usuarioId) {
        return consentimentoRepository.findByUsuarioIdOrderByDataConsentimentoDesc(usuarioId);
    }

    /**
     * Verifica se usuário deu consentimento para um tipo específico
     */
    @Transactional(readOnly = true)
    public boolean verificarConsentimento(Long usuarioId, String tipoConsentimento) {
        return consentimentoRepository.existsConsentimentoAtivo(usuarioId, tipoConsentimento);
    }

    /**
     * Busca estatísticas de consentimento
     */
    @Transactional(readOnly = true)
    public List<Object[]> buscarEstatisticasConsentimento() {
        return consentimentoRepository.findEstatisticasConsentimento();
    }

    /**
     * Busca usuários que não deram consentimento para um tipo específico
     */
    @Transactional(readOnly = true)
    public List<Long> buscarUsuariosSemConsentimento(String tipoConsentimento) {
        return consentimentoRepository.findUsuariosSemConsentimento(tipoConsentimento);
    }

    /**
     * Busca consentimentos que precisam ser renovados
     */
    @Transactional(readOnly = true)
    public List<Consentimento> buscarConsentimentosParaRenovacao() {
        LocalDateTime dataLimite = LocalDateTime.now().minusYears(1);
        return consentimentoRepository.findNecessitamRenovacao(dataLimite);
    }

    /**
     * Busca consentimentos por período
     */
    @Transactional(readOnly = true)
    public List<Consentimento> buscarConsentimentosPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return consentimentoRepository.findByPeriodoConsentimento(inicio, fim);
    }

    /**
     * Busca consentimentos revogados por período
     */
    @Transactional(readOnly = true)
    public List<Consentimento> buscarConsentimentosRevogadosPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return consentimentoRepository.findRevogadosByPeriodo(inicio, fim);
    }

    // Métodos auxiliares privados

    private void revogarConsentimentoAnterior(Long usuarioId, String tipoConsentimento) {
        Optional<Consentimento> consentimentoAnterior = consentimentoRepository.findAtivoByUsuarioIdAndTipo(usuarioId, tipoConsentimento);
        
        if (consentimentoAnterior.isPresent()) {
            Consentimento anterior = consentimentoAnterior.get();
            anterior.setConsentido(false);
            anterior.setDataRevogacao(LocalDateTime.now());
            consentimentoRepository.save(anterior);
        }
    }

    private String obterVersaoPoliticaAtual() {
        // Implementar busca da versão atual da política de privacidade
        return "1.0";
    }

    private ConsentimentoDTO.ConsentimentoResumoDTO mapearParaResumo(Consentimento consentimento) {
        ConsentimentoDTO.ConsentimentoResumoDTO resumo = new ConsentimentoDTO.ConsentimentoResumoDTO();
        resumo.setTipoConsentimento(consentimento.getTipoConsentimento());
        resumo.setDescricaoTipo(obterDescricaoTipo(consentimento.getTipoConsentimento()));
        resumo.setConsentido(consentimento.getConsentido());
        resumo.setDataConsentimento(consentimento.getDataConsentimento());
        resumo.setDataRevogacao(consentimento.getDataRevogacao());
        resumo.setPodeRevogar(consentimento.getConsentido() && consentimento.getDataRevogacao() == null);
        
        return resumo;
    }

    private String obterDescricaoTipo(String tipoConsentimento) {
        try {
            Consentimento.TipoConsentimento tipo = Consentimento.TipoConsentimento.valueOf(tipoConsentimento);
            return tipo.getDescricao();
        } catch (IllegalArgumentException e) {
            return tipoConsentimento;
        }
    }
}
