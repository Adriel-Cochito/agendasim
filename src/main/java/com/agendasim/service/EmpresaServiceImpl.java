package com.agendasim.service;

import com.agendasim.dao.EmpresaDAO;
import com.agendasim.model.Empresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    @Autowired
    private EmpresaDAO empresaDAO;

    @Override
    public List<Empresa> listarTodas() {
        return empresaDAO.listarTodas();
    }

    @Override
    public Empresa criar(Empresa empresa) {
        return empresaDAO.salvar(empresa);
    }

    @Override
    public Empresa buscarPorId(Long id) {
        return empresaDAO.buscarPorId(id);
    }

    @Override
    public void excluir(Long id) {
        empresaDAO.excluir(id);
    }

    @Override
    public Empresa atualizar(Long id, Empresa empresa) {
        return empresaDAO.atualizar(id, empresa);
    }
}
