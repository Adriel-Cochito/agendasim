package com.agendasim.service;

import com.agendasim.dto.AgendaAdminDTO;
import com.agendasim.dto.AgendaClienteDTO;
import com.agendasim.model.Agenda;

import java.time.LocalDate;
import java.util.List;

public interface AgendaService {
    List<Agenda> listarTodos();
    Agenda criar(Agenda agenda, Long empresaId);
    Agenda buscarPorId(Long id);
    void excluir(Long id);
    Agenda atualizar(Long id, Agenda agenda, Long empresaId);
    List<Agenda> listarPorEmpresa(Long empresaId);
    List<Agenda> listarPorServico(Long servicoId, Long empresaId);

    List<AgendaAdminDTO> listarParaAdmin(Long empresaId);
    List<AgendaClienteDTO> listarParaCliente(Long empresaId, Long servicoId, Long profissionalId);
    List<AgendaClienteDTO> listarParaClienteData(Long empresaId, Long servicoId, Long profissionalId, LocalDate data);
    List<AgendaAdminDTO> listarPorEmpresaProfissionalEData(Long empresaId, Long profissionalId, LocalDate data);
    
}
