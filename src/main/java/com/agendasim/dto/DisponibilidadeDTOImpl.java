package com.agendasim.dto;

import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Disponibilidade;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DisponibilidadeDTOImpl implements DisponibilidadeDTO {

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
    public List<Disponibilidade> listarPorEmpresaProfissionalEData(Long empresaId, Long profissionalId, LocalDate data) {
        int diaSemana = data.getDayOfWeek().getValue(); // 1=Seg, ..., 7=Dom
        if (diaSemana == 7) diaSemana = 0; // H2 usa 0=Dom

        String jpql = """
            SELECT d FROM Disponibilidade d
            WHERE d.empresa.id = :empresaId
            AND d.profissional.id = :profissionalId
            AND (
                (d.tipo = 'GRADE' AND :diaSemana MEMBER OF d.diasSemana)
                OR
                (d.tipo IN ('BLOQUEIO', 'LIBERADO') AND CAST(d.dataHoraInicio AS date) = :data)
            )
        """;

        return entityManager.createQuery(jpql, Disponibilidade.class)
                .setParameter("empresaId", empresaId)
                .setParameter("profissionalId", profissionalId)
                .setParameter("data", data)
                .setParameter("diaSemana", diaSemana)
                .getResultList();
    }




    @Override
    public List<Disponibilidade> listarPorEmpresa(Long empresaId) {
        String jpql = "SELECT d FROM Disponibilidade d WHERE d.empresa.id = :empresaId";
        return entityManager.createQuery(jpql, Disponibilidade.class)
                .setParameter("empresaId", empresaId)
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
