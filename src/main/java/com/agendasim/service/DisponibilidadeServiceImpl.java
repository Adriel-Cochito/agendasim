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
    private DisponibilidadeDTO disponibilidadeDTO;

    @Override
    @Transactional
    public Disponibilidade salvar(Disponibilidade disponibilidade) {
        switch (disponibilidade.getTipo()) {
            case GRADE:
            case BLOQUEIO_GRADE:
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

        if (disponibilidade.getTipo() == TipoDisponibilidade.BLOQUEIO || disponibilidade.getTipo() == TipoDisponibilidade.BLOQUEIO_GRADE) {
            return disponibilidadeDTO.salvar(disponibilidade);
        }

        if (disponibilidadeDTO.existeConflito(disponibilidade)) {
            String mensagem = switch (disponibilidade.getTipo()) {
                case GRADE -> "Conflito de horário com disponibilidade existente para os dias selecionados.";
                case LIBERADO -> "Conflito de horário com disponibilidade existente.";
                default -> "Conflito de horário com um bloqueio existente.";
            };
            throw new RuntimeException(mensagem);
        }

        return disponibilidadeDTO.salvar(disponibilidade);
    }

    @Override
    @Transactional
    public Disponibilidade atualizar(Long id, Disponibilidade disponibilidade) {
        Disponibilidade atual = buscarPorId(id);
        disponibilidade.setId(atual.getId()); // Garante atualização
        return disponibilidadeDTO.atualizar(disponibilidade);
    }

    @Override
    public Disponibilidade buscarPorId(Long id) {
        Disponibilidade d = disponibilidadeDTO.buscarPorId(id);
        if (d == null)
            throw new RuntimeException("Disponibilidade não encontrada");
        return d;
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        disponibilidadeDTO.deletar(id);
    }

    @Override
    public List<Disponibilidade> listarPorEmpresa(Long empresaId) {
        return disponibilidadeDTO.listarPorEmpresa(empresaId);
    }

    @Override
    public List<Disponibilidade> listarPorEmpresaEProfissional(Long empresaId, Long profissionalId) {
        return disponibilidadeDTO.listarPorEmpresaEProfissional(empresaId, profissionalId);
    }

    @Override
    public List<Disponibilidade> listarPorEmpresaProfissionalEData(Long empresaId, Long profissionalId, LocalDate data) {
        return disponibilidadeDTO.listarPorEmpresaProfissionalEData(empresaId, profissionalId, data);
    }


}
