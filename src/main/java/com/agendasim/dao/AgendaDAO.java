package com.agendasim.dao;

import com.agendasim.model.Agenda;

import java.util.List;

public interface AgendaDAO {
    List<Agenda> listarTodos();
    Agenda salvar(Agenda agenda);
    Agenda buscarPorId(Long id);
    void excluir(Long id);
    Agenda atualizar(Long id, Agenda agenda);
    List<Agenda> listarPorEmpresa(Long empresaId);
    List<Agenda> listarPorServico(Long servicoId);
}
