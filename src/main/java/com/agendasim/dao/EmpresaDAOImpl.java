package com.agendasim.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.agendasim.model.Empresa;
import com.agendasim.repository.EmpresaRepository;

import java.util.List;

@Component
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
}
