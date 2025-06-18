package com.agendasim.dao;

import com.agendasim.model.Disponibilidade;

import java.util.List;

public interface DisponibilidadeDAO {
    Disponibilidade salvar(Disponibilidade disponibilidade);
    Disponibilidade atualizar(Disponibilidade disponibilidade);
    void deletar(Long id);
    Disponibilidade buscarPorId(Long id);
    List<Disponibilidade> listarPorEmpresaEProfissional(Long empresaId, Long profissionalId);
    boolean existeConflito(Disponibilidade disponibilidade);
}
