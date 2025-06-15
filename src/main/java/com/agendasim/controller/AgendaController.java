package com.agendasim.controller;

import com.agendasim.model.Agenda;
import com.agendasim.service.AgendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendas")
public class AgendaController {
    @Autowired
    private AgendaService agendaService;

    @GetMapping
    public ResponseEntity<List<Agenda>> listarPorEmpresa(@RequestParam Long empresaId) {
        return ResponseEntity.ok(agendaService.listarPorEmpresa(empresaId));
    }

    @PostMapping
    public ResponseEntity<Agenda> criar(@RequestParam Long empresaId,
                                        @Valid @RequestBody Agenda agenda) {
        return ResponseEntity.ok(agendaService.criar(agenda, empresaId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Agenda> buscar(@PathVariable Long id, @RequestParam Long empresaId) {
        return ResponseEntity.ok(agendaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agenda> atualizar(@PathVariable Long id,
                                            @RequestParam Long empresaId,
                                            @Valid @RequestBody Agenda agenda) {
        return ResponseEntity.ok(agendaService.atualizar(id, agenda, empresaId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, @RequestParam Long empresaId) {
        agendaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/servico/{servicoId}")
    public ResponseEntity<List<Agenda>> listarPorServico(@PathVariable Long servicoId, @RequestParam Long empresaId) {
        return ResponseEntity.ok(agendaService.listarPorServico(servicoId, empresaId));
    }
}