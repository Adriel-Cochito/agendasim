package com.agendasim.service;

import com.agendasim.model.Agenda;
import java.util.List;

public interface AgendaService {
    List<Agenda> listarTodos();
    Agenda criar(Agenda agenda, Long empresaId);
    Agenda buscarPorId(Long id);
    void excluir(Long id);
    Agenda atualizar(Long id, Agenda agenda, Long empresaId);
    List<Agenda> listarPorEmpresa(Long empresaId);
    List<Agenda> listarPorServico(Long servicoId, Long empresaId);
}