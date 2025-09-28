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
@RequestMapping("/api/consentimentos")
@RequiredArgsConstructor
@Slf4j
public class ConsentimentoPublicController {

    private final ConsentimentoService consentimentoService;

    /**
     * Busca os consentimentos do usuário logado
     */
    @GetMapping("/meus")
    public ResponseEntity<List<Consentimento>> buscarMeusConsentimentos(
            @AuthenticationPrincipal UserDetails user) {
        try {
            Long usuarioId = getUserId(user);
            List<Consentimento> consentimentos = consentimentoService.buscarConsentimentosUsuario(usuarioId);
            return ResponseEntity.ok(consentimentos);
        } catch (Exception e) {
            log.error("Erro ao buscar consentimentos: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

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
            response.put("timestamp", LocalDateTime.now().toString());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erro ao revogar consentimento: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
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
