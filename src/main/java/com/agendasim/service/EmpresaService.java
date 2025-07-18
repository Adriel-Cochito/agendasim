package com.agendasim.service;

import com.agendasim.dto.CriarEmpresaComOwnerDTO;
import com.agendasim.dto.EmpresaComOwnerResponseDTO;
import com.agendasim.model.Empresa;
import java.util.List;

public interface EmpresaService {
    List<Empresa> listarTodas();
    Empresa criar(Empresa empresa);
    Empresa buscarPorId(Long id);
    void excluir(Long id);
    Empresa atualizar(Long id, Empresa empresa);
    EmpresaComOwnerResponseDTO criarEmpresaComOwner(CriarEmpresaComOwnerDTO dto);
}