package com.agendasim.service;

import com.agendasim.model.Profissional;

import java.util.List;

public interface ProfissionalService {
    List<Profissional> listarTodos();
    Profissional salvar(Profissional profissional);
    Profissional buscarPorId(Long id);
    void excluir(Long id);
    Profissional atualizar(Long id, Profissional profissional);
    List<Profissional> listarPorEmpresa(Long empresaId);
}
