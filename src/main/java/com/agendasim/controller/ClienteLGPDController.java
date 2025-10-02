package com.agendasim.controller;

import com.agendasim.service.LGPDService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/lgpd/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteLGPDController {
    
    private final LGPDService lgpdService;
    
    /**
     * Anonimizar todos os agendamentos de um cliente específico
     * Busca por nome e telefone em todas as empresas
     */
    @PostMapping("/anonimizar")
    public ResponseEntity<?> anonimizarCliente(
            @RequestParam String nomeCliente,
            @RequestParam String telefoneCliente) {
        try {
            int agendamentosAnonimizados = lgpdService.anonimizarTodosAgendamentosCliente(nomeCliente, telefoneCliente);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Cliente anonimizado com sucesso",
                "agendamentosAnonimizados", agendamentosAnonimizados,
                "nomeCliente", nomeCliente,
                "telefoneCliente", telefoneCliente
            ));
        } catch (Exception e) {
            log.error("Erro ao anonimizar cliente: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao anonimizar cliente: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Verificar quantos agendamentos um cliente tem
     */
    @GetMapping("/verificar")
    public ResponseEntity<?> verificarCliente(
            @RequestParam String nomeCliente,
            @RequestParam String telefoneCliente) {
        try {
            int totalAgendamentos = lgpdService.contarAgendamentosCliente(nomeCliente, telefoneCliente);
            boolean jaAnonimizado = lgpdService.verificarClienteJaAnonimizado(nomeCliente, telefoneCliente);
            
            return ResponseEntity.ok(Map.of(
                "nomeCliente", nomeCliente,
                "telefoneCliente", telefoneCliente,
                "totalAgendamentos", totalAgendamentos,
                "jaAnonimizado", jaAnonimizado
            ));
        } catch (Exception e) {
            log.error("Erro ao verificar cliente: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao verificar cliente: " + e.getMessage()
            ));
        }
    }
}
