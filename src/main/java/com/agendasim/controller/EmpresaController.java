package com.agendasim.controller;

import com.agendasim.dto.CriarEmpresaComOwnerDTO;
import com.agendasim.dto.EmpresaComOwnerResponseDTO;
import com.agendasim.model.Empresa;
import com.agendasim.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        // O GlobalExceptionHandler vai capturar automaticamente a EmailJaCadastradoException
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

    @GetMapping("/verificar-nome-unico/{nomeUnico}")
    public Map<String, Boolean> verificarNomeUnicoDisponivel(@PathVariable String nomeUnico) {
        boolean disponivel = empresaService.isNomeUnicoDisponivel(nomeUnico);
        return Map.of("disponivel", disponivel);
    }

    @GetMapping("/nome-unico/{nomeUnico}")
    public Empresa buscarPorNomeUnico(@PathVariable String nomeUnico) {
        return empresaService.buscarPorNomeUnico(nomeUnico);
    }
}