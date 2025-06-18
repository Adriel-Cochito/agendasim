package com.agendasim.service;

import com.agendasim.model.Disponibilidade;

import java.util.List;

public interface DisponibilidadeService {
    Disponibilidade salvar(Disponibilidade disponibilidade);
    void deletar(Long id);
    List<Disponibilidade> listarPorProfissional(Long profissionalId);
    List<Disponibilidade> listarPorEmpresaEProfissional(Long empresaId, Long profissionalId);
}
