package com.agendasim.controller;

import com.agendasim.model.Profissional;
import com.agendasim.service.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalService profissionalService;

    @GetMapping
    public List<Profissional> listarTodos(@RequestParam Long empresaId) {
        return profissionalService.listarPorEmpresa(empresaId);
    }

    @GetMapping("/{id}")
    public Profissional buscarPorId(@PathVariable Long id) {
        return profissionalService.buscarPorId(id);
    }

    @PostMapping
    public Profissional salvar(@Valid @RequestBody Profissional profissional) {
        return profissionalService.salvar(profissional);
    }

    @PutMapping("/{id}")
    public Profissional atualizar(@PathVariable Long id, @Valid @RequestBody Profissional profissional) {
        return profissionalService.atualizar(id, profissional);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        profissionalService.excluir(id);
    }
}
