package com.agendasim.service;

import com.agendasim.dto.ServicoDTO;
import com.agendasim.model.Servico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoServiceImpl implements ServicoService {

    @Autowired
    private ServicoDTO servicoDTO;

    @Override
    public List<Servico> listarTodos() {
        return servicoDTO.listarTodos();
    }

    @Override
    public Servico criar(Servico servico) {
        return servicoDTO.salvar(servico);
    }

    @Override
    public Servico buscarPorId(Long id, Long empresaId) {
        return servicoDTO.buscarPorId(id, empresaId);
    }


    @Override
    public void excluir(Long id) {
        servicoDTO.excluir(id);
    }

    @Override
    public Servico atualizar(Long id, Servico servico) {
        return servicoDTO.atualizar(id, servico);
    }

    @Override
    public List<Servico> listarPorEmpresa(Long empresaId) {
        return servicoDTO.listarPorEmpresa(empresaId);
    }
}
