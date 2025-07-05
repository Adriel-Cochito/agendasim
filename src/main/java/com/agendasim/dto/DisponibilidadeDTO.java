package com.agendasim.dto;

import com.agendasim.model.Disponibilidade;

import java.time.LocalDate;
import java.util.List;

public interface DisponibilidadeDTO {
    Disponibilidade salvar(Disponibilidade disponibilidade);
    Disponibilidade atualizar(Disponibilidade disponibilidade);
    void deletar(Long id);
    Disponibilidade buscarPorId(Long id);
    List<Disponibilidade> listarPorEmpresa(Long empresaId);
    List<Disponibilidade> listarPorEmpresaEProfissional(Long empresaId, Long profissionalId);
    List<Disponibilidade> listarPorEmpresaProfissionalEData(Long empresaId, Long profissionalId, LocalDate data);
    boolean existeConflito(Disponibilidade disponibilidade);
}
