package com.agendasim.dto;

import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Servico;
import com.agendasim.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class ServicoDTOImpl implements ServicoDTO {

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
    public Servico buscarPorId(Long id, Long empresaId) {
        return servicoRepository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Serviço id=" + id + " não encontrado para empresa id=" + empresaId));
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

        // Atualiza campos básicos
        existente.setTitulo(servico.getTitulo());
        existente.setDescricao(servico.getDescricao());
        existente.setPreco(servico.getPreco());
        existente.setAtivo(servico.getAtivo());
        existente.setDuracao(servico.getDuracao());
        existente.setEmpresaId(servico.getEmpresaId());

        // Atualiza os profissionais associados
        existente.setProfissionais(servico.getProfissionais());

        return servicoRepository.save(existente);
    }


    @Override
    public List<Servico> listarPorEmpresa(Long empresaId) {
        return servicoRepository.findByEmpresaId(empresaId);
    }
}
