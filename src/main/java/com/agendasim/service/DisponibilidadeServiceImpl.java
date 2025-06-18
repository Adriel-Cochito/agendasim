package com.agendasim.service;

import com.agendasim.model.Disponibilidade;
import com.agendasim.repository.DisponibilidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisponibilidadeServiceImpl implements DisponibilidadeService {

    @Autowired
    private DisponibilidadeRepository disponibilidadeRepository;

    @Override
    public Disponibilidade salvar(Disponibilidade disponibilidade) {
        return disponibilidadeRepository.save(disponibilidade);
    }

    @Override
    public void deletar(Long id) {
        disponibilidadeRepository.deleteById(id);
    }

    @Override
    public List<Disponibilidade> listarPorProfissional(Long profissionalId) {
        return disponibilidadeRepository.findByEmpresaIdAndProfissionalId(null, profissionalId); // ou outro filtro
    }

    @Override
    public List<Disponibilidade> listarPorEmpresaEProfissional(Long empresaId, Long profissionalId) {
        return disponibilidadeRepository.findByEmpresaIdAndProfissionalId(empresaId, profissionalId);
    }
}
