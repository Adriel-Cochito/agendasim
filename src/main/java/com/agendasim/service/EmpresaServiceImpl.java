package com.agendasim.service;

import com.agendasim.dto.EmpresaDTO;
import com.agendasim.dto.CriarEmpresaComOwnerDTO;
import com.agendasim.dto.EmpresaComOwnerResponseDTO;
import com.agendasim.model.Empresa;
import com.agendasim.model.Profissional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    @Autowired
    private EmpresaDTO empresaDTO;

    @Autowired
    private ProfissionalService profissionalService;

    @Override
    public List<Empresa> listarTodas() {
        return empresaDTO.listarTodas();
    }

    @Override
    public Empresa criar(Empresa empresa) {
        return empresaDTO.salvar(empresa);
    }

    @Override
    @Transactional
    public EmpresaComOwnerResponseDTO criarEmpresaComOwner(CriarEmpresaComOwnerDTO dto) {
        // 1. Criar a empresa primeiro
        Empresa empresa = new Empresa();
        empresa.setNome(dto.getNomeEmpresa());
        empresa.setEmail(dto.getEmailEmpresa());
        empresa.setTelefone(dto.getTelefoneEmpresa());
        empresa.setCnpj(dto.getCnpjEmpresa());
        empresa.setAtivo(dto.getAtivoEmpresa());

        Empresa empresaCriada = empresaDTO.salvar(empresa);

        // 2. Criar o profissional owner usando o ID da empresa criada
        Profissional profissional = new Profissional();
        profissional.setNome(dto.getNomeProfissional());
        profissional.setEmail(dto.getEmailProfissional());
        profissional.setSenha(dto.getSenhaProfissional());
        profissional.setPerfil(dto.getPerfilProfissional());
        profissional.setGoogleAccessToken(dto.getGoogleAccessToken());
        profissional.setGoogleRefreshToken(dto.getGoogleRefreshToken());
        profissional.setEmpresaId(empresaCriada.getId()); // Usa o ID da empresa criada
        profissional.setAtivo(dto.getAtivoProfissional());

        Profissional profissionalCriado = profissionalService.salvar(profissional);

        return new EmpresaComOwnerResponseDTO(empresaCriada, profissionalCriado);
    }

    @Override
    public Empresa buscarPorId(Long id) {
        return empresaDTO.buscarPorId(id);
    }

    @Override
    public void excluir(Long id) {
        empresaDTO.excluir(id);
    }

    @Override
    public Empresa atualizar(Long id, Empresa empresa) {
        return empresaDTO.atualizar(id, empresa);
    }
}