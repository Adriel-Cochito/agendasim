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
    public ResponseEntity<List<Agenda>> listarTodos() {
        return ResponseEntity.ok(agendaService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<Agenda> criar(@Valid @RequestBody Agenda agenda) {
        Agenda criada = agendaService.criar(agenda);
        return ResponseEntity.ok(criada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agenda> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendaService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        agendaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agenda> atualizar(@PathVariable Long id, @Valid @RequestBody Agenda agenda) {
        return ResponseEntity.ok(agendaService.atualizar(id, agenda));
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<Agenda>> listarPorEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(agendaService.listarPorEmpresa(empresaId));
    }

    @GetMapping("/servico/{servicoId}")
    public ResponseEntity<List<Agenda>> listarPorServico(@PathVariable Long servicoId) {
        return ResponseEntity.ok(agendaService.listarPorServico(servicoId));
    }
}
