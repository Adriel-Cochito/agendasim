package com.agendasim.controller;

import com.agendasim.model.LogAuditoria;
import com.agendasim.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lgpd/auditoria")
@RequiredArgsConstructor
@Slf4j
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    /**
     * Busca logs de auditoria do usuário autenticado
     */
    @GetMapping("/meus-logs")
    public ResponseEntity<Page<LogAuditoria>> buscarMeusLogs(
            @AuthenticationPrincipal UserDetails user,
            @PageableDefault(size = 20) Pageable pageable) {
        try {
            Long usuarioId = getUserId(user);
            Page<LogAuditoria> logs = auditoriaService.buscarLogsUsuario(usuarioId, pageable);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            log.error("Erro ao buscar logs do usuário: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca logs por ação específica (apenas para admins)
     */
    @GetMapping("/por-acao/{acao}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Page<LogAuditoria>> buscarLogsPorAcao(
            @PathVariable String acao,
            @PageableDefault(size = 20) Pageable pageable) {
        try {
            Page<LogAuditoria> logs = auditoriaService.buscarLogsPorAcao(acao, pageable);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            log.error("Erro ao buscar logs por ação: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca ações críticas (apenas para admins)
     */
    @GetMapping("/acoes-criticas")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Page<LogAuditoria>> buscarAcoesCriticas(
            @PageableDefault(size = 20) Pageable pageable) {
        try {
            Page<LogAuditoria> logs = auditoriaService.buscarAcoesCriticas(pageable);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            log.error("Erro ao buscar ações críticas: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca acessos a dados pessoais (apenas para admins)
     */
    @GetMapping("/acessos-dados-pessoais")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Page<LogAuditoria>> buscarAcessosDadosPessoais(
            @PageableDefault(size = 20) Pageable pageable) {
        try {
            Page<LogAuditoria> logs = auditoriaService.buscarAcessosDadosPessoais(pageable);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            log.error("Erro ao buscar acessos a dados pessoais: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca violações de segurança (apenas para admins)
     */
    @GetMapping("/violacoes-seguranca")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Page<LogAuditoria>> buscarViolacoesSeguranca(
            @PageableDefault(size = 20) Pageable pageable) {
        try {
            Page<LogAuditoria> logs = auditoriaService.buscarViolacoesSeguranca(pageable);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            log.error("Erro ao buscar violações de segurança: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca logs de LGPD (apenas para admins)
     */
    @GetMapping("/logs-lgpd")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Page<LogAuditoria>> buscarLogsLGPD(
            @PageableDefault(size = 20) Pageable pageable) {
        try {
            Page<LogAuditoria> logs = auditoriaService.buscarLogsLGPD(pageable);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            log.error("Erro ao buscar logs de LGPD: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca logs por período (apenas para admins)
     */
    @GetMapping("/por-periodo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Page<LogAuditoria>> buscarLogsPorPeriodo(
            @RequestParam String inicio,
            @RequestParam String fim,
            @PageableDefault(size = 20) Pageable pageable) {
        try {
            LocalDateTime dataInicio = LocalDateTime.parse(inicio);
            LocalDateTime dataFim = LocalDateTime.parse(fim);
            
            Page<LogAuditoria> logs = auditoriaService.buscarLogsPorPeriodo(dataInicio, dataFim, pageable);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            log.error("Erro ao buscar logs por período: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Busca logs de um registro específico (apenas para admins)
     */
    @GetMapping("/registro/{tabela}/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<List<LogAuditoria>> buscarLogsRegistro(
            @PathVariable String tabela,
            @PathVariable Long id) {
        try {
            List<LogAuditoria> logs = auditoriaService.buscarLogsRegistro(tabela, id);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            log.error("Erro ao buscar logs do registro: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca estatísticas de auditoria por ação (apenas para admins)
     */
    @GetMapping("/estatisticas/por-acao")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<List<Object[]>> buscarEstatisticasPorAcao() {
        try {
            List<Object[]> estatisticas = auditoriaService.buscarEstatisticasPorAcao();
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            log.error("Erro ao buscar estatísticas por ação: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca estatísticas por nível de risco (apenas para admins)
     */
    @GetMapping("/estatisticas/por-nivel-risco")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<List<Object[]>> buscarEstatisticasPorNivelRisco() {
        try {
            List<Object[]> estatisticas = auditoriaService.buscarEstatisticasPorNivelRisco();
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            log.error("Erro ao buscar estatísticas por nível de risco: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca estatísticas por período (apenas para admins)
     */
    @GetMapping("/estatisticas/por-periodo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<List<Object[]>> buscarEstatisticasPorPeriodo(
            @RequestParam String inicio,
            @RequestParam String fim) {
        try {
            LocalDateTime dataInicio = LocalDateTime.parse(inicio);
            LocalDateTime dataFim = LocalDateTime.parse(fim);
            
            List<Object[]> estatisticas = auditoriaService.buscarEstatisticasPorPeriodo(dataInicio, dataFim);
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            log.error("Erro ao buscar estatísticas por período: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Busca usuários inativos (apenas para admins)
     */
    @GetMapping("/usuarios-inativos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<List<Long>> buscarUsuariosInativos(
            @RequestParam(defaultValue = "30") int diasInatividade) {
        try {
            LocalDateTime dataLimite = LocalDateTime.now().minusDays(diasInatividade);
            List<Long> usuarios = auditoriaService.buscarUsuariosInativos(dataLimite);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            log.error("Erro ao buscar usuários inativos: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca logs por trace ID (apenas para admins)
     */
    @GetMapping("/trace/{traceId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<List<LogAuditoria>> buscarLogsPorTraceId(@PathVariable String traceId) {
        try {
            List<LogAuditoria> logs = auditoriaService.buscarLogsPorTraceId(traceId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            log.error("Erro ao buscar logs por trace ID: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obtém resumo de auditoria (apenas para admins)
     */
    @GetMapping("/resumo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Map<String, Object>> obterResumoAuditoria() {
        try {
            // Implementar lógica para obter resumo
            Map<String, Object> resumo = new HashMap<>();
            resumo.put("totalLogs", 0);
            resumo.put("logsCriticos", 0);
            resumo.put("logsAltos", 0);
            resumo.put("acessosDadosPessoais", 0);
            resumo.put("violacoesSeguranca", 0);
            resumo.put("timestamp", LocalDateTime.now().toString());
            
            return ResponseEntity.ok(resumo);
        } catch (Exception e) {
            log.error("Erro ao obter resumo de auditoria: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Método auxiliar para obter ID do usuário
    private Long getUserId(UserDetails user) {
        // Implementar lógica para obter ID do usuário
        // Por enquanto, retornando 1 como exemplo
        return 1L;
    }
}
