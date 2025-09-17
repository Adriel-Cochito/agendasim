package com.agendasim.service;

import com.agendasim.dto.EmpresaDTO;
import com.agendasim.dto.CriarEmpresaComOwnerDTO;
import com.agendasim.dto.EmpresaComOwnerResponseDTO;
import com.agendasim.exception.EmailJaCadastradoException;
import com.agendasim.model.Empresa;
import com.agendasim.model.Profissional;
import com.agendasim.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    @Autowired
    private EmpresaDTO empresaDTO;

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private ProfissionalRepository profissionalRepository;

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
        
        // 1. VALIDAÇÃO PRÉVIA: Verificar se o email do profissional já existe
        // Esta verificação acontece ANTES de criar a empresa
        validarEmailUnico(dto.getEmailProfissional());

        // 2. Criar a empresa (somente após validação do email)
        Empresa empresa = new Empresa();
        empresa.setNome(dto.getNomeEmpresa());
        empresa.setEmail(dto.getEmailEmpresa());
        empresa.setTelefone(dto.getTelefoneEmpresa());
        empresa.setCnpj(dto.getCnpjEmpresa());
        empresa.setAtivo(dto.getAtivoEmpresa());

        Empresa empresaCriada = empresaDTO.salvar(empresa);

        // 3. Criar o profissional owner usando o ID da empresa criada
        Profissional profissional = new Profissional();
        profissional.setNome(dto.getNomeProfissional());
        profissional.setEmail(dto.getEmailProfissional());
        profissional.setSenha(dto.getSenhaProfissional());
        profissional.setPerfil(dto.getPerfilProfissional());
        profissional.setGoogleAccessToken(dto.getGoogleAccessToken());
        profissional.setGoogleRefreshToken(dto.getGoogleRefreshToken());
        profissional.setEmpresaId(empresaCriada.getId());
        profissional.setAtivo(dto.getAtivoProfissional());

        try {
            Profissional profissionalCriado = profissionalService.salvar(profissional);
            return new EmpresaComOwnerResponseDTO(empresaCriada, profissionalCriado);
        } catch (DataIntegrityViolationException ex) {
            // Como já validamos antes, isso não deveria acontecer
            // Mas mantemos como segurança adicional
            throw new EmailJaCadastradoException(dto.getEmailProfissional(), 
                "Erro de concorrência: Email foi cadastrado por outro processo");
        }
    }

    /**
     * Valida se o email não está em uso por outro profissional
     * @param email Email a ser validado
     * @throws EmailJaCadastradoException se o email já estiver em uso
     */
    private void validarEmailUnico(String email) {
        Optional<Profissional> profissionalExistente = profissionalRepository.findByEmail(email);
        if (profissionalExistente.isPresent()) {
            throw new EmailJaCadastradoException(email);
        }
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