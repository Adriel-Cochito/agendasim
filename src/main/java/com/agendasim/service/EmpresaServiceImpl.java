package com.agendasim.service;

import com.agendasim.dto.CriarEmpresaComOwnerDTO;
import com.agendasim.dto.EmpresaComOwnerResponseDTO;
import com.agendasim.exception.EmailJaCadastradoException;
import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Empresa;
import com.agendasim.model.Profissional;
import com.agendasim.repository.EmpresaRepository;
import com.agendasim.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    public Empresa criar(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

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

        Empresa empresaCriada = empresaRepository.save(empresa);

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

    public Empresa buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Empresa id=" + id + " não encontrada"));
    }

    public void excluir(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Empresa com ID " + id + " não encontrada");
        }
        empresaRepository.deleteById(id);
    }

    public Empresa atualizar(Long id, Empresa empresa) {
        Empresa existente = buscarPorId(id);
        
        existente.setNome(empresa.getNome());
        existente.setEmail(empresa.getEmail());
        existente.setTelefone(empresa.getTelefone());
        existente.setCnpj(empresa.getCnpj());
        existente.setAtivo(empresa.getAtivo());
        
        return empresaRepository.save(existente);
    }
}