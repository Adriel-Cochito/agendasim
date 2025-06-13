package com.agendasim.dao;

import com.agendasim.model.Empresa;
import java.util.List;

public interface EmpresaDAO {
    List<Empresa> listarTodas();
    Empresa salvar(Empresa empresa);
    Empresa buscarPorId(Long id);
    void excluir(Long id);
    Empresa atualizar(Long id, Empresa empresa);
}
