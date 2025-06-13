package com.agendasim.dao;

import java.util.List;

import com.agendasim.model.Empresa;

public interface EmpresaDAO {
    
    List<Empresa> listarTodas();

    Empresa salvar(Empresa empresa);

    Empresa buscarPorId(Long id);

    void excluir(Long id);
}
