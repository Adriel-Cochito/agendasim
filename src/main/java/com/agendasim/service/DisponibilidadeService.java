package com.agendasim.service;

import com.agendasim.model.Disponibilidade;

import java.util.List;

public interface DisponibilidadeService {
    Disponibilidade salvar(Disponibilidade disponibilidade);
    Disponibilidade atualizar(Long id, Disponibilidade disponibilidade);
    Disponibilidade buscarPorId(Long id);
    void deletar(Long id);
    List<Disponibilidade> listarPorEmpresa(Long empresaId);
    List<Disponibilidade> listarPorEmpresaEProfissional(Long empresaId, Long profissionalId);

}

