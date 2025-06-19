package com.agendasim.dao;

import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Disponibilidade;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DisponibilidadeDAOImpl implements DisponibilidadeDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Disponibilidade salvar(Disponibilidade disponibilidade) {
        return entityManager.merge(disponibilidade); // merge permite salvar e atualizar
    }

    @Override
    public Disponibilidade atualizar(Disponibilidade disponibilidade) {
        return entityManager.merge(disponibilidade);
    }

    @Override
    public Disponibilidade buscarPorId(Long id) {
        return entityManager.find(Disponibilidade.class, id);
    }

    @Override
    public void deletar(Long id) {
        Disponibilidade d = buscarPorId(id);
        if (d != null) {
            entityManager.remove(d);
        } else {
            throw new RecursoNaoEncontradoException("Disponibilidade com ID " + id + " não encontrado");
        }
        
    }

    @Override
    public List<Disponibilidade> listarPorEmpresaEProfissional(Long empresaId, Long profissionalId) {
        String jpql = "SELECT d FROM Disponibilidade d WHERE d.empresa.id = :empresaId AND d.profissional.id = :profissionalId";
        return entityManager.createQuery(jpql, Disponibilidade.class)
                .setParameter("empresaId", empresaId)
                .setParameter("profissionalId", profissionalId)
                .getResultList();
    }

    @Override
    public boolean existeConflito(Disponibilidade disponibilidade) {
        String jpql = "SELECT COUNT(d) FROM Disponibilidade d " +
                      "WHERE d.profissional.id = :profissionalId " +
                      "AND d.empresa.id = :empresaId " +
                      "AND d.tipo = 'BLOQUEIO' " +
                      "AND d.dataHoraInicio = :dataHoraInicio";

        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("profissionalId", disponibilidade.getProfissional().getId())
                .setParameter("empresaId", disponibilidade.getEmpresa().getId())
                .setParameter("dataHoraInicio", disponibilidade.getDataHoraInicio())
                .getSingleResult();

        return count > 0;
    }
}
