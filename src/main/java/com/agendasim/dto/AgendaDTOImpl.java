package com.agendasim.dto;

import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Agenda;
import com.agendasim.repository.AgendaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Repository
public class AgendaDTOImpl implements AgendaDTO {

    @Autowired
    private AgendaRepository agendaRepository;

    @PersistenceContext
    private EntityManager entityManager;

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

    @Override
    public List<Agenda> listarPorEmpresaEServicoEProfissional(Long empresaId, Long servicoId, Long profissionalId) {
        String jpql = "SELECT a FROM Agenda a WHERE a.empresa.id = :empresaId AND a.servico.id = :servicoId AND a.profissional.id = :profissionalId";
        TypedQuery<Agenda> query = entityManager.createQuery(jpql, Agenda.class);
        query.setParameter("empresaId", empresaId);
        query.setParameter("servicoId", servicoId);
        query.setParameter("profissionalId", profissionalId);

        return query.getResultList();
    }

    @Override
    public List<Agenda> listarPorEmpresaEServicoEProfissionalData(Long empresaId, Long servicoId, Long profissionalId,
            LocalDate data) {
        // Converte LocalDate para Instant considerando UTC
        Instant inicioDoDia = data.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant fimDoDia = data.atTime(23, 59, 59).toInstant(ZoneOffset.UTC);

        String jpql = "SELECT a FROM Agenda a WHERE a.empresa.id = :empresaId " +
                "AND a.servico.id = :servicoId " +
                "AND a.profissional.id = :profissionalId " +
                "AND a.dataHora BETWEEN :inicio AND :fim";

        TypedQuery<Agenda> query = entityManager.createQuery(jpql, Agenda.class);
        query.setParameter("empresaId", empresaId);
        query.setParameter("servicoId", servicoId);
        query.setParameter("profissionalId", profissionalId);
        query.setParameter("inicio", inicioDoDia);
        query.setParameter("fim", fimDoDia);

        return query.getResultList();
    }

    @Override
    public List<Agenda> listarPorEmpresaProfissionalEData(Long empresaId, Long profissionalId,
            LocalDate data) {
        // Converte LocalDate para Instant considerando UTC
        Instant inicioDoDia = data.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant fimDoDia = data.atTime(23, 59, 59).toInstant(ZoneOffset.UTC);

        String jpql = "SELECT a FROM Agenda a WHERE a.empresa.id = :empresaId " +
                "AND a.profissional.id = :profissionalId " +
                "AND a.dataHora BETWEEN :inicio AND :fim";

        TypedQuery<Agenda> query = entityManager.createQuery(jpql, Agenda.class);
        query.setParameter("empresaId", empresaId);
        query.setParameter("profissionalId", profissionalId);
        query.setParameter("inicio", inicioDoDia);
        query.setParameter("fim", fimDoDia);

        return query.getResultList();
    }
}