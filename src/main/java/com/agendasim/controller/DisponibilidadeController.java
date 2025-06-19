package com.agendasim.controller;

import com.agendasim.model.Disponibilidade;
import com.agendasim.service.DisponibilidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disponibilidades")
public class DisponibilidadeController {

    @Autowired
    private DisponibilidadeService disponibilidadeService;

    @PostMapping
    public ResponseEntity<Disponibilidade> salvar(@Valid @RequestBody Disponibilidade disponibilidade) {
        return ResponseEntity.ok(disponibilidadeService.salvar(disponibilidade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Disponibilidade> atualizar(@PathVariable Long id, @Valid @RequestBody Disponibilidade disponibilidade) {
        return ResponseEntity.ok(disponibilidadeService.atualizar(id, disponibilidade));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disponibilidade> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(disponibilidadeService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        disponibilidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Disponibilidade>> listar(@RequestParam Long empresaId) {
        return ResponseEntity.ok(disponibilidadeService.listarPorEmpresa(empresaId));
    }

        @GetMapping("/profissional")
    public ResponseEntity<List<Disponibilidade>> listarByProfissional(@RequestParam Long empresaId,
                                                         @RequestParam Long profissionalId) {
        return ResponseEntity.ok(disponibilidadeService.listarPorEmpresaEProfissional(empresaId, profissionalId));
    }
}

