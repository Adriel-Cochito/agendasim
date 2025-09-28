package com.agendasim.service;

import com.agendasim.model.LogAuditoria;
import com.agendasim.repository.LogAuditoriaRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditoriaService {

    private final LogAuditoriaRepository logAuditoriaRepository;

    /**
     * Registra uma ação no log de auditoria
     */
    @Transactional
    public void logarAcao(Long usuarioId, String acao, String tabelaAfetada, Long registroId, 
                         String dadosAnteriores, String dadosNovos) {
        
        LogAuditoria logAuditoria = new LogAuditoria();
        logAuditoria.setUsuarioId(usuarioId);
        logAuditoria.setAcao(acao);
        logAuditoria.setTabelaAfetada(tabelaAfetada);
        logAuditoria.setRegistroId(registroId);
        logAuditoria.setDadosAnteriores(dadosAnteriores);
        logAuditoria.setDadosNovos(dadosNovos);
        logAuditoria.setIpAddress(obterIpAddress());
        logAuditoria.setUserAgent(obterUserAgent());
        logAuditoria.setSessaoId(obterSessaoId());
        logAuditoria.setTraceId(gerarTraceId());
        logAuditoria.setNivelRisco(determinarNivelRisco(acao));
        logAuditoria.setDataOperacao(LocalDateTime.now());
        
        logAuditoriaRepository.save(logAuditoria);
        
        log.info("Ação auditada: {} - Usuário: {} - Tabela: {} - Registro: {}", 
                acao, usuarioId, tabelaAfetada, registroId);
    }

    /**
     * Registra uma ação sem dados específicos
     */
    @Transactional
    public void logarAcao(Long usuarioId, String acao) {
        logarAcao(usuarioId, acao, null, null, null, null);
    }

    /**
     * Registra uma ação com dados de mudança
     */
    @Transactional
    public void logarMudanca(Long usuarioId, String acao, String tabelaAfetada, Long registroId, 
                           Object dadosAnteriores, Object dadosNovos) {
        
        String dadosAntStr = dadosAnteriores != null ? dadosAnteriores.toString() : null;
        String dadosNovStr = dadosNovos != null ? dadosNovos.toString() : null;
        
        logarAcao(usuarioId, acao, tabelaAfetada, registroId, dadosAntStr, dadosNovStr);
    }

    /**
     * Busca logs de um usuário específico
     */
    @Transactional(readOnly = true)
    public Page<LogAuditoria> buscarLogsUsuario(Long usuarioId, Pageable pageable) {
        return logAuditoriaRepository.findByUsuarioIdOrderByDataOperacaoDesc(usuarioId, pageable);
    }

    /**
     * Busca logs por ação
     */
    @Transactional(readOnly = true)
    public Page<LogAuditoria> buscarLogsPorAcao(String acao, Pageable pageable) {
        return logAuditoriaRepository.findByAcaoOrderByDataOperacaoDesc(acao, pageable);
    }

    /**
     * Busca logs de ações críticas
     */
    @Transactional(readOnly = true)
    public Page<LogAuditoria> buscarAcoesCriticas(Pageable pageable) {
        return logAuditoriaRepository.findAcoesCriticas(pageable);
    }

    /**
     * Busca logs de acesso a dados pessoais
     */
    @Transactional(readOnly = true)
    public Page<LogAuditoria> buscarAcessosDadosPessoais(Pageable pageable) {
        return logAuditoriaRepository.findAcessosDadosPessoais(pageable);
    }

    /**
     * Busca logs de violações de segurança
     */
    @Transactional(readOnly = true)
    public Page<LogAuditoria> buscarViolacoesSeguranca(Pageable pageable) {
        return logAuditoriaRepository.findViolacoesSeguranca(pageable);
    }

    /**
     * Busca logs de LGPD
     */
    @Transactional(readOnly = true)
    public Page<LogAuditoria> buscarLogsLGPD(Pageable pageable) {
        return logAuditoriaRepository.findLogsLGPD(pageable);
    }

    /**
     * Busca logs em um período específico
     */
    @Transactional(readOnly = true)
    public Page<LogAuditoria> buscarLogsPorPeriodo(LocalDateTime inicio, LocalDateTime fim, Pageable pageable) {
        return logAuditoriaRepository.findByPeriodo(inicio, fim, pageable);
    }

    /**
     * Busca logs de um registro específico
     */
    @Transactional(readOnly = true)
    public List<LogAuditoria> buscarLogsRegistro(String tabela, Long registroId) {
        return logAuditoriaRepository.findByRegistro(tabela, registroId);
    }

    /**
     * Busca estatísticas de auditoria
     */
    @Transactional(readOnly = true)
    public List<Object[]> buscarEstatisticasPorAcao() {
        return logAuditoriaRepository.countByAcao();
    }

    /**
     * Busca estatísticas por nível de risco
     */
    @Transactional(readOnly = true)
    public List<Object[]> buscarEstatisticasPorNivelRisco() {
        return logAuditoriaRepository.countByNivelRisco();
    }

    /**
     * Busca estatísticas por período
     */
    @Transactional(readOnly = true)
    public List<Object[]> buscarEstatisticasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return logAuditoriaRepository.findEstatisticasPorPeriodo(inicio, fim);
    }

    /**
     * Busca usuários inativos
     */
    @Transactional(readOnly = true)
    public List<Long> buscarUsuariosInativos(LocalDateTime dataLimite) {
        return logAuditoriaRepository.findUsuariosInativos(dataLimite);
    }

    /**
     * Busca logs por trace ID
     */
    @Transactional(readOnly = true)
    public List<LogAuditoria> buscarLogsPorTraceId(String traceId) {
        return logAuditoriaRepository.findByTraceIdOrderByDataOperacaoAsc(traceId);
    }

    // Métodos auxiliares privados

    private String obterIpAddress() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String xForwardedFor = request.getHeader("X-Forwarded-For");
                if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                    return xForwardedFor.split(",")[0].trim();
                }
                return request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.warn("Erro ao obter IP address: {}", e.getMessage());
        }
        return "unknown";
    }

    private String obterUserAgent() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return request.getHeader("User-Agent");
            }
        } catch (Exception e) {
            log.warn("Erro ao obter User-Agent: {}", e.getMessage());
        }
        return "unknown";
    }

    private String obterSessaoId() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return request.getSession().getId();
            }
        } catch (Exception e) {
            log.warn("Erro ao obter Session ID: {}", e.getMessage());
        }
        return "unknown";
    }

    private String gerarTraceId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private LogAuditoria.NivelRisco determinarNivelRisco(String acao) {
        return switch (acao) {
            case "DADOS_EXCLUIDOS", "DADOS_ANONIMIZADOS", "ACESSO_NEGADO", "TENTATIVA_NAO_AUTORIZADA" -> LogAuditoria.NivelRisco.CRITICO;
            case "DADOS_ACESSADOS", "DADOS_MODIFICADOS", "DADOS_EXPORTADOS", "CONSENTIMENTO_REVOGADO" -> LogAuditoria.NivelRisco.ALTO;
            case "CONSENTIMENTO_DADO", "TERMOS_ACEITOS", "POLITICA_ACEITA" -> LogAuditoria.NivelRisco.MEDIO;
            default -> LogAuditoria.NivelRisco.BAIXO;
        };
    }
}
