package com.agendasim.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.agendasim.dao.AgendaClienteDAO;
import com.agendasim.model.Disponibilidade;
import com.agendasim.model.Servico;
import com.agendasim.service.AgendaService;
import com.agendasim.service.DisponibilidadeService;
import com.agendasim.service.ServicoService;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private DisponibilidadeService disponibilidadeService;

    @Autowired
    private AgendaService agendaService;
    
    // LISTAR (sempre requer empresaId)
    @GetMapping("/servicos")
    public ResponseEntity<List<Servico>> listarPorEmpresa(@RequestParam Long empresaId) {
        return ResponseEntity.ok(servicoService.listarPorEmpresa(empresaId));
    }

    @GetMapping("/disponibilidade/profissional/data")
    public ResponseEntity<List<Disponibilidade>> listarByProfissionalAndData(
            @RequestParam Long empresaId,
            @RequestParam Long profissionalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        List<Disponibilidade> lista = disponibilidadeService
                .listarPorEmpresaProfissionalEData(empresaId, profissionalId, data);
        
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/agenda")
    public ResponseEntity<List<AgendaClienteDAO>> listarParaCliente(
            @RequestParam Long empresaId,
            @RequestParam Long servicoId,
            @RequestParam Long profissionalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(agendaService.listarParaClienteData(empresaId, servicoId, profissionalId, data));
    }

}
