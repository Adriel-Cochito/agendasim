package com.agendasim.service;

import com.agendasim.dto.ProfissionalDTO;
import com.agendasim.dto.ProfissionalPatchDTO;
import com.agendasim.model.Profissional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfissionalServiceImpl implements ProfissionalService {

    @Autowired
    private ProfissionalDTO profissionalDTO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Profissional salvar(Profissional profissional) {
        profissional.setSenha(passwordEncoder.encode(profissional.getSenha()));
        return profissionalDTO.salvar(profissional);
    }

    @Override
    public List<Profissional> listarTodos() {
        return profissionalDTO.listarTodos();
    }

    @Override
    public Profissional buscarPorId(Long id) {
        return profissionalDTO.buscarPorId(id);
    }

    @Override
    public void excluir(Long id) {
        profissionalDTO.excluir(id);
    }

    @Override
    public Profissional atualizar(Long id, Profissional profissional) {
        return profissionalDTO.atualizar(id, profissional);
    }

    @Override
    public List<Profissional> listarPorEmpresa(Long empresaId) {
        return profissionalDTO.listarPorEmpresa(empresaId);
    }

    @Override
    public Profissional atualizarParcial(Long id, ProfissionalPatchDTO patchDTO) {
        // Se senha for enviada, criptografar
        if (patchDTO.getSenha() != null) {
            patchDTO.setSenha(passwordEncoder.encode(patchDTO.getSenha()));
        }
        return profissionalDTO.atualizarParcial(id, patchDTO);
    }

}
