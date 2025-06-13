package com.agendasim.dao;

import com.agendasim.model.Empresa;
import com.agendasim.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmpresaDAOImpl implements EmpresaDAO {

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
        return empresaRepository.findById(id).orElse(null);
    }

    @Override
    public void excluir(Long id) {
        empresaRepository.deleteById(id);
    }

    @Override
    public Empresa atualizar(Long id, Empresa empresa) {
        Empresa existente = empresaRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setNome(empresa.getNome());
            existente.setEmail(empresa.getEmail());
            existente.setTelefone(empresa.getTelefone());
            existente.setCnpj(empresa.getCnpj());
            return empresaRepository.save(existente);
        }
        return null;
    }
}
