package com.agendasim.service;

import com.agendasim.dto.AgendaAdminDTO;
import com.agendasim.dto.AgendaClienteDTO;
import com.agendasim.dto.AgendaDTO;
import com.agendasim.exception.ConflitoAgendamentoException;
import com.agendasim.mapper.AgendaMapper;
import com.agendasim.model.Agenda;
import com.agendasim.model.Empresa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AgendaServiceImpl implements AgendaService {
    @Autowired
    private AgendaDTO agendaDTO;

    @Override
    public List<Agenda> listarTodos() {
        return agendaDTO.listarTodos();
    }

    @Override
    public Agenda criar(Agenda agenda, Long empresaId) {
        Empresa empresa = new Empresa();
        empresa.setId(empresaId);
        agenda.setEmpresa(empresa);

        // Validar conflitos de horário antes de salvar
        if (agendaDTO.existeConflitoAgendamento(agenda)) {
            throw new ConflitoAgendamentoException("Não é possível criar o agendamento: conflito de horário com outro agendamento ou bloqueio existente.");
        }

        return agendaDTO.salvar(agenda);
    }


    @Override
    public Agenda buscarPorId(Long id) {
        return agendaDTO.buscarPorId(id);
    }

    @Override
    public void excluir(Long id) {
        agendaDTO.excluir(id);
    }

    @Override
    public Agenda atualizar(Long id, Agenda agenda, Long empresaId) {
        // Aqui você define a empresa no objeto agenda
        Empresa empresa = new Empresa();
        empresa.setId(empresaId);
        agenda.setEmpresa(empresa);
        agenda.setId(id); // Definir o ID para a validação

        // Validar conflitos de horário antes de atualizar
        if (agendaDTO.existeConflitoAgendamento(agenda)) {
            throw new ConflitoAgendamentoException("Não é possível atualizar o agendamento: conflito de horário com outro agendamento ou bloqueio existente.");
        }

        return agendaDTO.atualizar(id, agenda);
    }


    @Override
    public List<Agenda> listarPorEmpresa(Long empresaId) {
        return agendaDTO.listarPorEmpresa(empresaId);
    }

    @Override
    public List<Agenda> listarPorServico(Long servicoId, Long empresaId) {
        return agendaDTO.listarPorServico(servicoId, empresaId);
    }

    @Override
    public List<AgendaAdminDTO> listarParaAdmin(Long empresaId) {
        return AgendaMapper.toAdminDAOList(agendaDTO.listarPorEmpresa(empresaId));
    }

    @Override
    public List<AgendaClienteDTO> listarParaCliente(Long empresaId, Long servicoId, Long profissionalId) {
        return AgendaMapper.toClienteDAOList(
            agendaDTO.listarPorEmpresaEServicoEProfissional(empresaId, servicoId, profissionalId)
        );
    }

    @Override
    public List<AgendaClienteDTO> listarParaClienteData(Long empresaId, Long servicoId, Long profissionalId, LocalDate data) {
        return AgendaMapper.toClienteDAOList(
            agendaDTO.listarPorEmpresaEServicoEProfissionalData(empresaId, servicoId, profissionalId, data)
        );
    }

    @Override
    public List<AgendaAdminDTO> listarPorEmpresaProfissionalEData(Long empresaId, Long profissionalId, LocalDate data) {
        return AgendaMapper.toAdminDAOList(agendaDTO.listarPorEmpresaProfissionalEData(empresaId, profissionalId, data));
    }


}