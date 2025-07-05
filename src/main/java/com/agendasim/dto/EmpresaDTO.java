package com.agendasim.dto;

import com.agendasim.model.Empresa;
import java.util.List;

public interface EmpresaDTO {
    List<Empresa> listarTodas();
    Empresa salvar(Empresa empresa);
    Empresa buscarPorId(Long id);
    void excluir(Long id);
    Empresa atualizar(Long id, Empresa empresa);
}
