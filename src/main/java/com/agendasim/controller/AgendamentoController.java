package com.agendasim.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.agendasim.model.Servico;
import com.agendasim.service.ServicoService;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private ServicoService servicoService;
    
    // LISTAR (sempre requer empresaId)
    @GetMapping("/servicos")
    public ResponseEntity<List<Servico>> listarPorEmpresa(@RequestParam Long empresaId) {
        return ResponseEntity.ok(servicoService.listarPorEmpresa(empresaId));
    }

}
