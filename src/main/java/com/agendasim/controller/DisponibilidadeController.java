package com.agendasim.controller;

import com.agendasim.model.Disponibilidade;
import com.agendasim.service.DisponibilidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disponibilidade")
public class DisponibilidadeController {

    @Autowired
    private DisponibilidadeService disponibilidadeService;

        @GetMapping("/{id}")
    public ResponseEntity<Disponibilidade> buscar(@PathVariable Long id, @RequestParam Long empresaId) {
        return ResponseEntity.ok(disponibilidadeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Disponibilidade> salvar(@Valid @RequestBody Disponibilidade disponibilidade) {
        return ResponseEntity.ok(disponibilidadeService.salvar(disponibilidade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        disponibilidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Disponibilidade>> listar(@RequestParam Long empresaId,
                                                         @RequestParam Long profissionalId) {
        return ResponseEntity.ok(disponibilidadeService.listarPorEmpresaEProfissional(empresaId, profissionalId));
    }
}
