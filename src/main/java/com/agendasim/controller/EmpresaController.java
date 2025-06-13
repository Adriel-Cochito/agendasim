package com.agendasim.controller;

import com.agendasim.model.Empresa;
import com.agendasim.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public List<Empresa> listar() {
        return empresaService.listarTodas();
    }

    @PostMapping
    public Empresa criar(@RequestBody Empresa empresa) {
        return empresaService.criar(empresa);
    }

    @GetMapping("/{id}")
    public Empresa buscarPorId(@PathVariable Long id) {
        return empresaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Empresa atualizar(@PathVariable Long id, @RequestBody Empresa empresa) {
        return empresaService.atualizar(id, empresa);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        empresaService.excluir(id);
    }
}
