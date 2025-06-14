package com.agendasim.controller;

import com.agendasim.model.Servico;
import com.agendasim.service.ServicoService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    // LISTAR (sempre requer empresaId)
    @GetMapping
    public ResponseEntity<List<Servico>> listarPorEmpresa(@RequestParam Long empresaId) {
        return ResponseEntity.ok(servicoService.listarPorEmpresa(empresaId));
    }

    // CRIAR – empresaId vem no query‑param, atribuímos antes de salvar
    @PostMapping
    public ResponseEntity<Servico> criar(@RequestParam Long empresaId,
                                         @Valid @RequestBody Servico servico) {
        servico.setEmpresaId(empresaId);
        return ResponseEntity.ok(servicoService.criar(servico));
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscar(@PathVariable Long id,
                                          @RequestParam Long empresaId) {
        return ResponseEntity.ok(servicoService.buscarPorId(id, empresaId));
    }

    // ATUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizar(@PathVariable Long id,
                                             @RequestParam Long empresaId,
                                             @Valid @RequestBody Servico servico) {
        servico.setEmpresaId(empresaId);
        return ResponseEntity.ok(servicoService.atualizar(id, servico));
    }

    // EXCLUIR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id,
                                        @RequestParam Long empresaId) {
        servicoService.excluir(id);   // opcional: crie o método que cheque empresa
        return ResponseEntity.noContent().build();
    }
}

