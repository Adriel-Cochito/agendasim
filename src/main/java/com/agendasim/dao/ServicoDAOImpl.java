package com.agendasim.dao;

import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Servico;
import com.agendasim.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class ServicoDAOImpl implements ServicoDAO {

    @Autowired
    private ServicoRepository servicoRepository;

    @Override
    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    @Override
    public Servico salvar(Servico servico) {
        return servicoRepository.save(servico);
    }

    @Override
    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id).orElse(null);
    }

    @Override
    public void excluir(Long id) {
        if (!servicoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Serviço com ID " + id + " não encontrado");
        }
        servicoRepository.deleteById(id);
    }


    @Override
    public Servico atualizar(Long id, Servico servico) {
    Servico existente = servicoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço com ID " + id + " não encontrado"));
        if (existente != null) {
            existente.setTitulo(servico.getTitulo());
            existente.setDescricao(servico.getDescricao());
            existente.setPreco(servico.getPreco());
            existente.setEmpresaId(servico.getEmpresaId());
            return servicoRepository.save(existente);
        }
        return null;
    }

    @Override
    public List<Servico> listarPorEmpresa(Long empresaId) {
        return servicoRepository.findByEmpresaId(empresaId);
    }
}
