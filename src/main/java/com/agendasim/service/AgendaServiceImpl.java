package com.agendasim.service;

import com.agendasim.dao.AgendaDAO;
import com.agendasim.model.Agenda;
import com.agendasim.model.Empresa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AgendaServiceImpl implements AgendaService {
    @Autowired
    private AgendaDAO agendaDAO;

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
}