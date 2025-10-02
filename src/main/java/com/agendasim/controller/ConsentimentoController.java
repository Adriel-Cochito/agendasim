package com.agendasim.controller;

import com.agendasim.dto.lgpd.ConsentimentoDTO;
import com.agendasim.model.Consentimento;
import com.agendasim.service.ConsentimentoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lgpd/consentimentos")
@RequiredArgsConstructor
@Slf4j
public class ConsentimentoController {

    private final ConsentimentoService consentimentoService;

    /**
     * Registra um novo consentimento
     */
    @PostMapping
    public ResponseEntity<Consentimento> registrarConsentimento(
            @RequestBody ConsentimentoDTO.CriarConsentimentoDTO dto,
            @AuthenticationPrincipal UserDetails user,
            HttpServletRequest request) {
        try {
            Long usuarioId = getUserId(user);
            String ipAddress = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            
            Consentimento consentimento = consentimentoService.registrarConsentimento(
                usuarioId, dto, ipAddress, userAgent);
            
            return ResponseEntity.ok(consentimento);
        } catch (Exception e) {
            log.error("Erro ao registrar consentimento: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Atualiza um consentimento existente
     */
    @PutMapping("/{tipo}")
    public ResponseEntity<Consentimento> atualizarConsentimento(
            @PathVariable String tipo,
            @RequestBody ConsentimentoDTO.AtualizarConsentimentoDTO dto,
            @AuthenticationPrincipal UserDetails user,
            HttpServletRequest request) {
        try {
            Long usuarioId = getUserId(user);
            String ipAddress = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            
            Consentimento consentimento = consentimentoService.atualizarConsentimento(
                usuarioId, tipo, dto, ipAddress, userAgent);
            
            return ResponseEntity.ok(consentimento);
        } catch (Exception e) {
            log.error("Erro ao atualizar consentimento: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Revoga um consentimento
     */
    @DeleteMapping("/{tipo}")
    public ResponseEntity<Map<String, String>> revogarConsentimento(
            @PathVariable String tipo,
            @AuthenticationPrincipal UserDetails user) {
        try {
            Long usuarioId = getUserId(user);
            consentimentoService.revogarConsentimento(usuarioId, tipo);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Consentimento revogado com sucesso");
            response.put("tipo", tipo);
            response.put("timestamp", LocalDateTime.now().toString());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erro ao revogar consentimento: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Busca consentimentos ativos do usuário
     */
    @GetMapping("/meus")
    public ResponseEntity<List<ConsentimentoDTO.ConsentimentoResumoDTO>> buscarMeusConsentimentos(
            @AuthenticationPrincipal UserDetails user) {
        try {
            Long usuarioId = getUserId(user);
            List<ConsentimentoDTO.ConsentimentoResumoDTO> consentimentos = 
                consentimentoService.buscarConsentimentosAtivos(usuarioId);
            
            return ResponseEntity.ok(consentimentos);
        } catch (Exception e) {
            log.error("Erro ao buscar consentimentos: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca todos os consentimentos do usuário (incluindo revogados)
     */
    @GetMapping("/historico")
    public ResponseEntity<List<Consentimento>> buscarHistoricoConsentimentos(
            @AuthenticationPrincipal UserDetails user) {
        try {
            Long usuarioId = getUserId(user);
            List<Consentimento> consentimentos = consentimentoService.buscarConsentimentosUsuario(usuarioId);
            
            return ResponseEntity.ok(consentimentos);
        } catch (Exception e) {
            log.error("Erro ao buscar histórico de consentimentos: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Verifica se usuário deu consentimento para um tipo específico
     */
    @GetMapping("/verificar/{tipo}")
    public ResponseEntity<Map<String, Object>> verificarConsentimento(
            @PathVariable String tipo,
            @AuthenticationPrincipal UserDetails user) {
        try {
            Long usuarioId = getUserId(user);
            boolean consentiu = consentimentoService.verificarConsentimento(usuarioId, tipo);
            
            Map<String, Object> response = new HashMap<>();
            response.put("tipo", tipo);
            response.put("consentiu", consentiu);
            response.put("timestamp", LocalDateTime.now().toString());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erro ao verificar consentimento: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca estatísticas de consentimento (apenas para admins)
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<List<Object[]>> buscarEstatisticas() {
        try {
            List<Object[]> estatisticas = consentimentoService.buscarEstatisticasConsentimento();
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            log.error("Erro ao buscar estatísticas de consentimento: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca usuários que não deram consentimento para um tipo específico (apenas para admins)
     */
    @GetMapping("/usuarios-sem-consentimento/{tipo}")
    public ResponseEntity<List<Long>> buscarUsuariosSemConsentimento(@PathVariable String tipo) {
        try {
            List<Long> usuarios = consentimentoService.buscarUsuariosSemConsentimento(tipo);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            log.error("Erro ao buscar usuários sem consentimento: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca consentimentos que precisam ser renovados (apenas para admins)
     */
    @GetMapping("/para-renovacao")
    public ResponseEntity<List<Consentimento>> buscarConsentimentosParaRenovacao() {
        try {
            List<Consentimento> consentimentos = consentimentoService.buscarConsentimentosParaRenovacao();
            return ResponseEntity.ok(consentimentos);
        } catch (Exception e) {
            log.error("Erro ao buscar consentimentos para renovação: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca consentimentos por período (apenas para admins)
     */
    @GetMapping("/por-periodo")
    public ResponseEntity<List<Consentimento>> buscarConsentimentosPorPeriodo(
            @RequestParam String inicio,
            @RequestParam String fim) {
        try {
            LocalDateTime dataInicio = LocalDateTime.parse(inicio);
            LocalDateTime dataFim = LocalDateTime.parse(fim);
            
            List<Consentimento> consentimentos = consentimentoService.buscarConsentimentosPorPeriodo(dataInicio, dataFim);
            return ResponseEntity.ok(consentimentos);
        } catch (Exception e) {
            log.error("Erro ao buscar consentimentos por período: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtém tipos de consentimento disponíveis
     */
    @GetMapping("/tipos")
    public ResponseEntity<Map<String, String>> obterTiposConsentimento() {
        Map<String, String> tipos = new HashMap<>();
        
        for (Consentimento.TipoConsentimento tipo : Consentimento.TipoConsentimento.values()) {
            tipos.put(tipo.name(), tipo.getDescricao());
        }
        
        return ResponseEntity.ok(tipos);
    }

    // Métodos auxiliares privados

    private Long getUserId(UserDetails user) {
        // Implementar lógica para obter ID do usuário
        // Por enquanto, retornando 1 como exemplo
        return 1L;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
