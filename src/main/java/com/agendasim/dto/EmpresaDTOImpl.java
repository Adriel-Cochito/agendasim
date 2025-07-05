package com.agendasim.dto;

import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Empresa;
import com.agendasim.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmpresaDTOImpl implements EmpresaDTO {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    @Override
    public Empresa salvar(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    @Override
    public Empresa buscarPorId(Long id) {
        return empresaRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Empresa com ID " + id + " não encontrado"));
    }

    @Override
    public void excluir(Long id) {
    if (!empresaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Empresa com ID " + id + " não encontrado");
        }
        empresaRepository.deleteById(id);
    }

    @Override
    public Empresa atualizar(Long id, Empresa empresa) {

        Empresa existente = empresaRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Empresa com ID " + id + " não encontrado"));
        if (existente != null) {
            existente.setNome(empresa.getNome());
            existente.setEmail(empresa.getEmail());
            existente.setTelefone(empresa.getTelefone());
            existente.setAtivo(empresa.getAtivo());
            existente.setCnpj(empresa.getCnpj());
            return empresaRepository.save(existente);
        }
        return null;
    }
}
