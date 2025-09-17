package com.agendasim.service;

import com.agendasim.exception.IntegridadeReferencialException;
import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Servico;
import com.agendasim.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    public Servico criar(Servico servico) {
        return servicoRepository.save(servico);
    }

    public Servico buscarPorId(Long id, Long empresaId) {
        return servicoRepository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Serviço id=" + id + " não encontrado para empresa id=" + empresaId));
    }

    public void excluir(Long id) {
        if (!servicoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Serviço com ID " + id + " não encontrado");
        }
        
        try {
            servicoRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new IntegridadeReferencialException(
                "Não é possível excluir este serviço pois ele possui agendamentos associados", 
                ex
            );
        }
    }

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

    public List<Servico> listarPorEmpresa(Long empresaId) {
        return servicoRepository.findByEmpresaId(empresaId);
    }
}
