package com.agendasim.dto;

import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Profissional;
import com.agendasim.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProfissionalDTOImpl implements ProfissionalDTO {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Override
    public List<Profissional> listarTodos() {
        return profissionalRepository.findAll();
    }

    @Override
    public Profissional salvar(Profissional profissional) {
        return profissionalRepository.save(profissional);
    }

    @Override
    public Profissional buscarPorId(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional id=" + id + " não encontrado"));
    }

    @Override
    public void excluir(Long id) {
        if (!profissionalRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Profissional não encontrado com ID: " + id);
        }
        profissionalRepository.deleteById(id);
    }

    @Override
    public Profissional atualizar(Long id, Profissional profissional) {
        Profissional existente = buscarPorId(id);

        existente.setNome(profissional.getNome());
        existente.setEmail(profissional.getEmail());
        existente.setAtivo(profissional.getAtivo());
        existente.setGoogleAccessToken(profissional.getGoogleAccessToken());
        existente.setGoogleRefreshToken(profissional.getGoogleRefreshToken());
        existente.setEmpresaId(profissional.getEmpresaId());

        return profissionalRepository.save(existente);
    }

    @Override
    public List<Profissional> listarPorEmpresa(Long empresaId) {
        return profissionalRepository.findByEmpresaId(empresaId);
    }

    @Override
    public Profissional atualizarParcial(Long id, ProfissionalPatchDTO patchDTO) {
        Profissional existente = buscarPorId(id);

        if (patchDTO.getNome() != null) {
            existente.setNome(patchDTO.getNome());
        }
        if (patchDTO.getEmail() != null) {
            existente.setEmail(patchDTO.getEmail());
        }
        if (patchDTO.getSenha() != null) {
            existente.setSenha(patchDTO.getSenha());
        }
        if (patchDTO.getPerfil() != null) {
            existente.setPerfil(patchDTO.getPerfil());
        }
        if (patchDTO.getGoogleAccessToken() != null) {
            existente.setGoogleAccessToken(patchDTO.getGoogleAccessToken());
        }
        if (patchDTO.getGoogleRefreshToken() != null) {
            existente.setGoogleRefreshToken(patchDTO.getGoogleRefreshToken());
        }
        if (patchDTO.getAtivo() != null) {
            existente.setAtivo(patchDTO.getAtivo());
        }
        if (patchDTO.getEmpresaId() != null) {
            existente.setEmpresaId(patchDTO.getEmpresaId());
        }

        return profissionalRepository.save(existente);
    }

}
