package com.agendasim.service;

import com.agendasim.dto.ProfissionalDTO;
import com.agendasim.model.Profissional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfissionalServiceImpl implements ProfissionalService {

    @Autowired
    private ProfissionalDTO profissionalDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Profissional salvar(Profissional profissional) {
        profissional.setSenha(passwordEncoder.encode(profissional.getSenha()));
        return profissionalDAO.salvar(profissional);
    }


    @Override
    public List<Profissional> listarTodos() {
        return profissionalDAO.listarTodos();
    }


    @Override
    public Profissional buscarPorId(Long id) {
        return profissionalDAO.buscarPorId(id);
    }

    @Override
    public void excluir(Long id) {
        profissionalDAO.excluir(id);
    }

    @Override
    public Profissional atualizar(Long id, Profissional profissional) {
        return profissionalDAO.atualizar(id, profissional);
    }

    @Override
    public List<Profissional> listarPorEmpresa(Long empresaId) {
        return profissionalDAO.listarPorEmpresa(empresaId);
    }
}
