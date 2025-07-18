package com.agendasim.dto;

import com.agendasim.model.Agenda;

import java.time.LocalDate;
import java.util.List;

public interface AgendaDTO {
    List<Agenda> listarTodos();
    Agenda salvar(Agenda agenda);
    void excluir(Long id);
    Agenda atualizar(Long id, Agenda agenda);
    List<Agenda> listarPorEmpresa(Long empresaId);
    Agenda buscarPorId(Long id);
    List<Agenda> listarPorServico(Long servicoId, Long empresaId);
    List<Agenda> listarPorEmpresaEServicoEProfissional(Long empresaId, Long servicoId, Long profissionalId);
    List<Agenda> listarPorEmpresaEServicoEProfissionalData(Long empresaId, Long servicoId, Long profissionalId, LocalDate data);
    List<Agenda> listarPorEmpresaProfissionalEData(Long empresaId, Long profissionalId, LocalDate data);
}