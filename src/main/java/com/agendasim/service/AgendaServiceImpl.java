package com.agendasim.service;

import com.agendasim.dto.AgendaAdminDTO;
import com.agendasim.dto.AgendaClienteDTO;
import com.agendasim.dto.AgendaDTO;
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
    private AgendaDTO agendaDAO;

    @Override
    public List<Agenda> listarTodos() {
        return agendaDAO.listarTodos();
    }

    @Override
    public Agenda criar(Agenda agenda, Long empresaId) {
        Empresa empresa = new Empresa();
        empresa.setId(empresaId);
        agenda.setEmpresa(empresa);

        return agendaDAO.salvar(agenda);
    }


    @Override
    public Agenda buscarPorId(Long id) {
        return agendaDAO.buscarPorId(id);
    }

    @Override
    public void excluir(Long id) {
        agendaDAO.excluir(id);
    }

    @Override
    public Agenda atualizar(Long id, Agenda agenda, Long empresaId) {
        // Aqui você define a empresa no objeto agenda
        Empresa empresa = new Empresa();
        empresa.setId(empresaId);
        agenda.setEmpresa(empresa);

        return agendaDAO.atualizar(id, agenda);
    }


    @Override
    public List<Agenda> listarPorEmpresa(Long empresaId) {
        return agendaDAO.listarPorEmpresa(empresaId);
    }

    @Override
    public List<Agenda> listarPorServico(Long servicoId, Long empresaId) {
        return agendaDAO.listarPorServico(servicoId, empresaId);
    }

    @Override
    public List<AgendaAdminDTO> listarParaAdmin(Long empresaId) {
        return AgendaMapper.toAdminDAOList(agendaDAO.listarPorEmpresa(empresaId));
    }

    @Override
    public List<AgendaClienteDTO> listarParaCliente(Long empresaId, Long servicoId, Long profissionalId) {
        return AgendaMapper.toClienteDAOList(
            agendaDAO.listarPorEmpresaEServicoEProfissional(empresaId, servicoId, profissionalId)
        );
    }

    @Override
    public List<AgendaClienteDTO> listarParaClienteData(Long empresaId, Long servicoId, Long profissionalId, LocalDate data) {
        return AgendaMapper.toClienteDAOList(
            agendaDAO.listarPorEmpresaEServicoEProfissionalData(empresaId, servicoId, profissionalId, data)
        );
    }

}