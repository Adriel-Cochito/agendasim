package com.agendasim.controller;

import com.agendasim.dto.lgpd.BuscarClienteLGPDRequest;
import com.agendasim.dto.lgpd.ClienteLGPDResumoDTO;
import com.agendasim.service.ClienteLGPDService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agendamentos/lgpd")
@RequiredArgsConstructor
@Slf4j
public class AgendamentoPublicoLGPDController {

    private final ClienteLGPDService clienteLGPDService;

    /**
     * Busca resumo de dados LGPD do cliente
     * Endpoint público para consulta de dados pessoais
     */
    @PostMapping("/resumo")
    public ResponseEntity<ClienteLGPDResumoDTO> buscarResumoDadosCliente(
            @RequestBody BuscarClienteLGPDRequest request) {
        try {
            log.info("Recebida solicitação de resumo LGPD para: {} - {}", 
                request.getNome(), request.getTelefone());
            
            ClienteLGPDResumoDTO resumo = clienteLGPDService.buscarResumoDadosCliente(request);
            
            log.info("Resumo LGPD gerado com sucesso. Existe: {}, Total agendamentos: {}", 
                resumo.isExiste(), resumo.getTotalAgendamentos());
            
            return ResponseEntity.ok(resumo);
        } catch (Exception e) {
            log.error("Erro ao buscar resumo LGPD para cliente: {} - {}", 
                request.getNome(), request.getTelefone(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Anonimiza dados do cliente
     * Endpoint público para anonimização de dados pessoais
     */
    @PostMapping("/anonimizar")
    public ResponseEntity<Void> anonimizarDadosCliente(
            @RequestBody BuscarClienteLGPDRequest request) {
        try {
            log.info("Recebida solicitação de anonimização LGPD para: {} - {}", 
                request.getNome(), request.getTelefone());
            
            clienteLGPDService.anonimizarDadosCliente(request);
            
            log.info("Dados anonimizados com sucesso para: {} - {}", 
                request.getNome(), request.getTelefone());
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Erro ao anonimizar dados LGPD para cliente: {} - {}", 
                request.getNome(), request.getTelefone(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
