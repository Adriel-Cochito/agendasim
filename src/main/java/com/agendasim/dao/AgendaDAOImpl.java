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
    public Agenda buscarPorId(Long id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agenda não encontrada com ID: " + id));
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
        existente.setEmpresaId(agenda.getEmpresaId());
        existente.setServicoId(agenda.getServicoId());
        existente.setDataHora(agenda.getDataHora());
        existente.setStatus(agenda.getStatus());

        return agendaRepository.save(existente);
    }

    @Override
    public List<Agenda> listarPorEmpresa(Long empresaId) {
        return agendaRepository.findByEmpresaId(empresaId);
    }

    @Override
    public List<Agenda> listarPorServico(Long servicoId) {
        return agendaRepository.findByServicoId(servicoId);
    }
}
