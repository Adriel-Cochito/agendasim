package com.agendasim.service;

import com.agendasim.model.Servico;

import java.util.List;

public interface ServicoService {
    List<Servico> listarTodos();
    Servico criar(Servico servico);
    Servico buscarPorId(Long id, Long empresaId);
    void excluir(Long id);
    Servico atualizar(Long id, Servico servico);
    List<Servico> listarPorEmpresa(Long empresaId);
}
