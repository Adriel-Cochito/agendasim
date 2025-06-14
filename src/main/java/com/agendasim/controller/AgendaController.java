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

    // LISTAR agendas de uma empresa
    @GetMapping
    public ResponseEntity<List<Agenda>> listarPorEmpresa(@RequestParam Long empresaId) {
        return ResponseEntity.ok(agendaService.listarPorEmpresa(empresaId));
    }

    // CRIAR
    @PostMapping
    public ResponseEntity<Agenda> criar(@RequestParam Long empresaId,
                                        @Valid @RequestBody Agenda agenda) {
        agenda.setEmpresaId(empresaId);
        return ResponseEntity.ok(agendaService.criar(agenda));
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Agenda> buscar(@PathVariable Long id,
                                         @RequestParam Long empresaId) {
        return ResponseEntity.ok(agendaService.buscarPorId(id));
    }

    // ATUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Agenda> atualizar(@PathVariable Long id,
                                            @RequestParam Long empresaId,
                                            @Valid @RequestBody Agenda agenda) {
        agenda.setEmpresaId(empresaId);
        return ResponseEntity.ok(agendaService.atualizar(id, agenda));
    }

    // EXCLUIR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id,
                                        @RequestParam Long empresaId) {
        agendaService.excluir(id);   // idem: implementar verificação se desejar
        return ResponseEntity.noContent().build();
    }

    // LISTAR agendas de um serviço específico (dentro da empresa)
    @GetMapping("/servico/{servicoId}")
    public ResponseEntity<List<Agenda>> listarPorServico(@PathVariable Long servicoId,
                                                         @RequestParam Long empresaId) {
        return ResponseEntity.ok(agendaService.listarPorServico(servicoId, empresaId));
    }
}

