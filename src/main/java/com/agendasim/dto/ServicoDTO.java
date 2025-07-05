package com.agendasim.dto;

import com.agendasim.model.Servico;

import java.util.List;

public interface ServicoDTO {
    List<Servico> listarTodos();
    Servico salvar(Servico servico);
    Servico buscarPorId(Long id, Long empresaId);       // ★ alterado
    void excluir(Long id);
    Servico atualizar(Long id, Servico servico);
    List<Servico> listarPorEmpresa(Long idEmpresa);
}
