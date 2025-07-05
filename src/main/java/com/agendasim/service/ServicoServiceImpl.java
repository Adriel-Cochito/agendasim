package com.agendasim.service;

import com.agendasim.dto.ServicoDTO;
import com.agendasim.model.Servico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoServiceImpl implements ServicoService {

    @Autowired
    private ServicoDTO servicoDAO;

    @Override
    public List<Servico> listarTodos() {
        return servicoDAO.listarTodos();
    }

    @Override
    public Servico criar(Servico servico) {
        return servicoDAO.salvar(servico);
    }

    @Override
    public Servico buscarPorId(Long id, Long empresaId) {
        return servicoDAO.buscarPorId(id, empresaId);
    }


    @Override
    public void excluir(Long id) {
        servicoDAO.excluir(id);
    }

    @Override
    public Servico atualizar(Long id, Servico servico) {
        return servicoDAO.atualizar(id, servico);
    }

    @Override
    public List<Servico> listarPorEmpresa(Long empresaId) {
        return servicoDAO.listarPorEmpresa(empresaId);
    }
}
