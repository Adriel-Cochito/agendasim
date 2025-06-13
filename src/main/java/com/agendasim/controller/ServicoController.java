package com.agendasim.controller;

import com.agendasim.model.Servico;
import com.agendasim.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @GetMapping
    public List<Servico> listarTodos() {
        return servicoService.listarTodos();
    }

    @PostMapping
    public Servico criar(@RequestBody Servico servico) {
        return servicoService.criar(servico);
    }

    @GetMapping("/{id}")
    public Servico buscarPorId(@PathVariable Long id) {
        return servicoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Servico atualizar(@PathVariable Long id, @RequestBody Servico servico) {
        return servicoService.atualizar(id, servico);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        servicoService.excluir(id);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<Servico> listarPorEmpresa(@PathVariable Long empresaId) {
        return servicoService.listarPorEmpresa(empresaId);
    }
}
