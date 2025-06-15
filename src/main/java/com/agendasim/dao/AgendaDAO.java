package com.agendasim.dao;

import com.agendasim.model.Agenda;
import java.util.List;

public interface AgendaDAO {
    List<Agenda> listarTodos();
    Agenda salvar(Agenda agenda);
    void excluir(Long id);
    Agenda atualizar(Long id, Agenda agenda);
    List<Agenda> listarPorEmpresa(Long empresaId);
    Agenda buscarPorId(Long id);
    List<Agenda> listarPorServico(Long servicoId, Long empresaId);
}