package com.agendasim.controller;

import com.agendasim.dto.CriarEmpresaComOwnerDTO;
import com.agendasim.dto.EmpresaComOwnerResponseDTO;
import com.agendasim.model.Empresa;
import com.agendasim.service.EmpresaService;
import jakarta.validation.Valid;
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

    @PostMapping("/com-owner")
    public EmpresaComOwnerResponseDTO criarComOwner(@Valid @RequestBody CriarEmpresaComOwnerDTO dto) {
        return empresaService.criarEmpresaComOwner(dto);
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