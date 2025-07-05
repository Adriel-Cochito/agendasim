package com.agendasim.service;

import com.agendasim.dto.DisponibilidadeDTO;
import com.agendasim.enums.TipoDisponibilidade;
import com.agendasim.model.Disponibilidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DisponibilidadeServiceImpl implements DisponibilidadeService {

    @Autowired
    private DisponibilidadeDTO disponibilidadeDAO;

    @Override
    @Transactional
    public Disponibilidade salvar(Disponibilidade disponibilidade) {
        switch (disponibilidade.getTipo()) {
            case GRADE:
                disponibilidade.setDataHoraInicio(null);
                disponibilidade.setDataHoraFim(null);
                break;

            case BLOQUEIO:
            case LIBERADO:
                disponibilidade.setDiasSemana(null);
                disponibilidade.setHoraInicio(null);
                disponibilidade.setHoraFim(null);
                break;
        }

        if (disponibilidade.getTipo() == TipoDisponibilidade.BLOQUEIO) {
            return disponibilidadeDAO.salvar(disponibilidade);
        }

        if (disponibilidadeDAO.existeConflito(disponibilidade)) {
            throw new RuntimeException("Conflito de horário com um bloqueio existente.");
        }

        return disponibilidadeDAO.salvar(disponibilidade);
    }

    @Override
    @Transactional
    public Disponibilidade atualizar(Long id, Disponibilidade disponibilidade) {
        Disponibilidade atual = buscarPorId(id);
        disponibilidade.setId(atual.getId()); // Garante atualização
        return disponibilidadeDAO.atualizar(disponibilidade);
    }

    @Override
    public Disponibilidade buscarPorId(Long id) {
        Disponibilidade d = disponibilidadeDAO.buscarPorId(id);
        if (d == null)
            throw new RuntimeException("Disponibilidade não encontrada");
        return d;
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        disponibilidadeDAO.deletar(id);
    }

    @Override
    public List<Disponibilidade> listarPorEmpresa(Long empresaId) {
        return disponibilidadeDAO.listarPorEmpresa(empresaId);
    }

    @Override
    public List<Disponibilidade> listarPorEmpresaEProfissional(Long empresaId, Long profissionalId) {
        return disponibilidadeDAO.listarPorEmpresaEProfissional(empresaId, profissionalId);
    }

    @Override
    public List<Disponibilidade> listarPorEmpresaProfissionalEData(Long empresaId, Long profissionalId, LocalDate data) {
        return disponibilidadeDAO.listarPorEmpresaProfissionalEData(empresaId, profissionalId, data);
    }


}
