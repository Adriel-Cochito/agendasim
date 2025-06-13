package com.agendasim.controller;

import com.agendasim.dao.EmpresaDAO;
import com.agendasim.model.Empresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaDAO empresaDAO;

    @GetMapping
    public List<Empresa> listar() {
        return empresaDAO.listarTodas();
    }

    @PostMapping
    public Empresa criar(@RequestBody Empresa empresa) {
        return empresaDAO.salvar(empresa);
    }

    @GetMapping("/{id}")
    public Empresa buscarPorId(@PathVariable Long id) {
        return empresaDAO.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Empresa atualizar(@PathVariable Long id, @RequestBody Empresa empresa) {
        return empresaDAO.atualizar(id, empresa);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        empresaDAO.excluir(id);
    }
}
