package com.agendasim.service;

import com.agendasim.dto.ProfissionalPatchDTO;
import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Profissional;
import com.agendasim.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Profissional salvar(Profissional profissional) {
        profissional.setSenha(passwordEncoder.encode(profissional.getSenha()));
        return profissionalRepository.save(profissional);
    }

    public List<Profissional> listarTodos() {
        return profissionalRepository.findAll();
    }

    public Profissional buscarPorId(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional id=" + id + " não encontrado"));
    }

    public void excluir(Long id) {
        if (!profissionalRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Profissional com ID " + id + " não encontrado");
        }
        profissionalRepository.deleteById(id);
    }

    public Profissional atualizar(Long id, Profissional profissional) {
        Profissional existente = buscarPorId(id);
        
        existente.setNome(profissional.getNome());
        existente.setEmail(profissional.getEmail());
        existente.setPerfil(profissional.getPerfil());
        existente.setAtivo(profissional.getAtivo());
        existente.setEmpresaId(profissional.getEmpresaId());
        existente.setGoogleAccessToken(profissional.getGoogleAccessToken());
        existente.setGoogleRefreshToken(profissional.getGoogleRefreshToken());
        
        // Se senha for fornecida, criptografar
        if (profissional.getSenha() != null && !profissional.getSenha().isEmpty()) {
            existente.setSenha(passwordEncoder.encode(profissional.getSenha()));
        }
        
        return profissionalRepository.save(existente);
    }

    public List<Profissional> listarPorEmpresa(Long empresaId) {
        return profissionalRepository.findByEmpresaId(empresaId);
    }

    public Profissional atualizarParcial(Long id, ProfissionalPatchDTO patchDTO) {
        Profissional existente = buscarPorId(id);
        
        if (patchDTO.getNome() != null) {
            existente.setNome(patchDTO.getNome());
        }
        if (patchDTO.getEmail() != null) {
            existente.setEmail(patchDTO.getEmail());
        }
        if (patchDTO.getPerfil() != null) {
            existente.setPerfil(patchDTO.getPerfil());
        }
        if (patchDTO.getAtivo() != null) {
            existente.setAtivo(patchDTO.getAtivo());
        }
        if (patchDTO.getGoogleAccessToken() != null) {
            existente.setGoogleAccessToken(patchDTO.getGoogleAccessToken());
        }
        if (patchDTO.getGoogleRefreshToken() != null) {
            existente.setGoogleRefreshToken(patchDTO.getGoogleRefreshToken());
        }
        
        // Se senha for enviada, criptografar
        if (patchDTO.getSenha() != null && !patchDTO.getSenha().isEmpty()) {
            existente.setSenha(passwordEncoder.encode(patchDTO.getSenha()));
        }
        
        return profissionalRepository.save(existente);
    }
}
