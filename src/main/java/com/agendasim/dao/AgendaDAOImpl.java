package com.agendasim.dao;

import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Agenda;
import com.agendasim.repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AgendaDAOImpl implements AgendaDAO {
    @Autowired
    private AgendaRepository agendaRepository;

    @Override
    public List<Agenda> listarTodos() {
        return agendaRepository.findAll();
    }

    @Override
    public Agenda salvar(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    @Override
    public void excluir(Long id) {
        if (!agendaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Agenda não encontrada com ID: " + id);
        }
        agendaRepository.deleteById(id);
    }

    @Override
    public Agenda atualizar(Long id, Agenda agenda) {
        Agenda existente = buscarPorId(id);

        existente.setNomeCliente(agenda.getNomeCliente());
        existente.setTelefoneCliente(agenda.getTelefoneCliente());
        existente.setDataHora(agenda.getDataHora());
        existente.setStatus(agenda.getStatus());
        existente.setServico(agenda.getServico());
        existente.setProfissional(agenda.getProfissional());
        existente.setEmpresa(agenda.getEmpresa());

        return agendaRepository.save(existente);
    }

    @Override
    public Agenda buscarPorId(Long id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agenda id=" + id + " não encontrada"));
    }

    @Override
    public List<Agenda> listarPorServico(Long servicoId, Long empresaId) {
        return agendaRepository.findByServicoIdAndEmpresaId(servicoId, empresaId);
    }

    @Override
    public List<Agenda> listarPorEmpresa(Long empresaId) {
        return agendaRepository.findByEmpresaId(empresaId);
    }
}