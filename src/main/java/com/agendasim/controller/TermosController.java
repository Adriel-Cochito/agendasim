package com.agendasim.controller;

import com.agendasim.dto.lgpd.AceiteTermoDTO;
import com.agendasim.service.TermosService;
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
@RequestMapping("/api/lgpd/termos")
@RequiredArgsConstructor
@Slf4j
public class TermosController {

    private final TermosService termosService;

    /**
     * Registra aceite de termo pelo usuário (documentos estáticos)
     */
    @PostMapping("/aceitar")
    public ResponseEntity<Map<String, String>> aceitarTermo(
            @RequestParam String versao,
            @RequestParam(defaultValue = "true") Boolean aceito,
            @AuthenticationPrincipal UserDetails user,
            HttpServletRequest request) {
        try {
            Long usuarioId = getUserId(user);
            String ipAddress = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            
            termosService.registrarAceiteEstatico(usuarioId, versao, aceito, ipAddress, userAgent);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Termo aceito com sucesso");
            response.put("timestamp", LocalDateTime.now().toString());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erro ao aceitar termo: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Verifica se usuário aceitou a versão atual
     */
    @GetMapping("/verificar-aceite")
    public ResponseEntity<Map<String, Object>> verificarAceite(@AuthenticationPrincipal UserDetails user) {
        try {
            Long usuarioId = getUserId(user);
            boolean aceitou = termosService.verificarAceiteEstaticoUsuario(usuarioId, "1.0"); // Versão atual fixa
            
            Map<String, Object> response = new HashMap<>();
            response.put("aceitou", aceitou);
            response.put("timestamp", LocalDateTime.now().toString());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erro ao verificar aceite: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca todos os aceites de termos estáticos do usuário
     */
    @GetMapping("/meus-aceites")
    public ResponseEntity<List<AceiteTermoDTO>> buscarMeusAceites(@AuthenticationPrincipal UserDetails user) {
        try {
            Long usuarioId = getUserId(user);
            List<AceiteTermoDTO> aceites = termosService.buscarAceitesEstaticosUsuario(usuarioId);
            
            return ResponseEntity.ok(aceites);
        } catch (Exception e) {
            log.error("Erro ao buscar aceites do usuário: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
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