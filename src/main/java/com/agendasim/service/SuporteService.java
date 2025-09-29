package com.agendasim.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.agendasim.dto.suporte.AtualizarChamadoSuporteDTO;
import com.agendasim.dto.suporte.AvaliacaoChamadoDTO;
import com.agendasim.dto.suporte.ChamadoSuporteResponseDTO;
import com.agendasim.dto.suporte.ComentarioChamadoDTO;
import com.agendasim.dto.suporte.CriarChamadoSuporteDTO;
import com.agendasim.dto.suporte.EstatisticasSuporteDTO;
import com.agendasim.enums.PrioridadeSuporte;
import com.agendasim.enums.StatusChamado;
import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.ChamadoSuporte;
import com.agendasim.model.Empresa;
import com.agendasim.repository.ChamadoSuporteRepository;
import com.agendasim.repository.EmpresaRepository;

@Service
@Transactional
public class SuporteService {
    
    @Autowired
    private ChamadoSuporteRepository chamadoSuporteRepository;
    
    @Autowired
    private EmpresaRepository empresaRepository;
    
    private static final String UPLOAD_DIR = "uploads/suporte/";
    
    public ChamadoSuporteResponseDTO criarChamado(CriarChamadoSuporteDTO dto, String emailUsuario, Long empresaId) {
        ChamadoSuporte chamado = new ChamadoSuporte();
        chamado.setTitulo(dto.getTitulo());
        chamado.setDescricao(dto.getDescricao());
        chamado.setCategoria(dto.getCategoria());
        chamado.setSubcategoria(dto.getSubcategoria());
        chamado.setPrioridade(dto.getPrioridade());
        chamado.setEmailUsuario(dto.getEmailUsuario());
        chamado.setNomeUsuario(dto.getNomeUsuario());
        chamado.setPaginaErro(dto.getPaginaErro());
        chamado.setStatus(StatusChamado.ABERTO);
        
        // Associar empresa se ID for fornecido
        if (empresaId != null) {
            Optional<Empresa> empresa = empresaRepository.findById(empresaId);
            if (empresa.isPresent()) {
                chamado.setEmpresa(empresa.get());
            }
        }
        
        // Processar anexos se houver
        if (dto.getAnexos() != null && !dto.getAnexos().isEmpty()) {
            chamado.setAnexos(dto.getAnexos());
        }
        
        ChamadoSuporte chamadoSalvo = chamadoSuporteRepository.save(chamado);
        return converterParaResponseDTO(chamadoSalvo);
    }
    
    public Page<ChamadoSuporteResponseDTO> listarChamados(String emailUsuario, String categoria, 
                                                         StatusChamado status, PrioridadeSuporte prioridade,
                                                         LocalDateTime dataInicio, LocalDateTime dataFim,
                                                         Long empresaId, Pageable pageable) {
        Page<ChamadoSuporte> chamados = chamadoSuporteRepository.findChamadosComFiltros(
                emailUsuario, status, prioridade, categoria, dataInicio, dataFim, empresaId, pageable);
        
        return chamados.map(this::converterParaResponseDTO);
    }
    
    public ChamadoSuporteResponseDTO buscarChamadoPorId(Long id, String emailUsuario, Long empresaId) {
        Optional<ChamadoSuporte> chamado;
        
        // Verificar se é um usuário logado (tem empresa)
        if (empresaId != null) {
            chamado = chamadoSuporteRepository.findByIdAndEmpresaId(id, empresaId);
        } else {
            chamado = chamadoSuporteRepository.findByIdAndEmailUsuario(id, emailUsuario);
        }
        
        if (chamado.isEmpty()) {
            throw new RecursoNaoEncontradoException("Chamado não encontrado");
        }
        
        return converterParaResponseDTO(chamado.get());
    }
    
    public ChamadoSuporteResponseDTO atualizarChamado(Long id, AtualizarChamadoSuporteDTO dto, String emailUsuario, Long empresaId) {
        ChamadoSuporte chamado = buscarChamadoEntity(id, emailUsuario, empresaId);
        
        if (dto.getTitulo() != null) {
            chamado.setTitulo(dto.getTitulo());
        }
        if (dto.getDescricao() != null) {
            chamado.setDescricao(dto.getDescricao());
        }
        if (dto.getCategoria() != null) {
            chamado.setCategoria(dto.getCategoria());
        }
        if (dto.getSubcategoria() != null) {
            chamado.setSubcategoria(dto.getSubcategoria());
        }
        if (dto.getPrioridade() != null) {
            chamado.setPrioridade(dto.getPrioridade());
        }
        if (dto.getStatus() != null) {
            chamado.setStatus(dto.getStatus());
        }
        if (dto.getPaginaErro() != null) {
            chamado.setPaginaErro(dto.getPaginaErro());
        }
        if (dto.getRespostaSuporte() != null) {
            chamado.setRespostaSuporte(dto.getRespostaSuporte());
            chamado.setDataResposta(LocalDateTime.now());
        }
        if (dto.getUsuarioSuporte() != null) {
            chamado.setUsuarioSuporte(dto.getUsuarioSuporte());
        }
        
        ChamadoSuporte chamadoAtualizado = chamadoSuporteRepository.save(chamado);
        return converterParaResponseDTO(chamadoAtualizado);
    }
    
    public void fecharChamado(Long id, String emailUsuario, Long empresaId) {
        ChamadoSuporte chamado = buscarChamadoEntity(id, emailUsuario, empresaId);
        chamado.setStatus(StatusChamado.FECHADO);
        chamadoSuporteRepository.save(chamado);
    }
    
    public void reabrirChamado(Long id, String emailUsuario, Long empresaId) {
        ChamadoSuporte chamado = buscarChamadoEntity(id, emailUsuario, empresaId);
        chamado.setStatus(StatusChamado.ABERTO);
        chamadoSuporteRepository.save(chamado);
    }
    
    public void adicionarComentario(Long id, ComentarioChamadoDTO dto, String emailUsuario, Long empresaId) {
        ChamadoSuporte chamado = buscarChamadoEntity(id, emailUsuario, empresaId);
        
        // Adicionar comentário à descrição (simplificado)
        String comentarioComTimestamp = "\n\n--- Comentário adicionado em " + LocalDateTime.now() + " ---\n" + dto.getComentario();
        chamado.setDescricao(chamado.getDescricao() + comentarioComTimestamp);
        
        chamadoSuporteRepository.save(chamado);
    }
    
    public void avaliarAtendimento(Long id, AvaliacaoChamadoDTO dto, String emailUsuario, Long empresaId) {
        ChamadoSuporte chamado = buscarChamadoEntity(id, emailUsuario, empresaId);
        chamado.setAvaliacaoNota(dto.getNota());
        chamado.setAvaliacaoComentario(dto.getComentario());
        chamadoSuporteRepository.save(chamado);
    }
    
    public String uploadAnexo(Long id, MultipartFile arquivo, String emailUsuario, Long empresaId) throws IOException {
        ChamadoSuporte chamado = buscarChamadoEntity(id, emailUsuario, empresaId);
        
        // Criar diretório se não existir
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Gerar nome único para o arquivo
        String nomeOriginal = arquivo.getOriginalFilename();
        String extensao = nomeOriginal != null ? nomeOriginal.substring(nomeOriginal.lastIndexOf(".")) : "";
        String nomeArquivo = UUID.randomUUID().toString() + extensao;
        
        // Salvar arquivo
        Path filePath = uploadPath.resolve(nomeArquivo);
        Files.copy(arquivo.getInputStream(), filePath);
        
        // Adicionar URL do anexo ao chamado
        String urlAnexo = "/api/suporte/chamados/" + id + "/anexos/" + nomeArquivo;
        chamado.getAnexos().add(urlAnexo);
        chamadoSuporteRepository.save(chamado);
        
        return urlAnexo;
    }
    
    public byte[] downloadAnexo(Long id, String nomeArquivo, String emailUsuario, Long empresaId) throws IOException {
        ChamadoSuporte chamado = buscarChamadoEntity(id, emailUsuario, empresaId);
        
        // Verificar se o anexo pertence ao chamado
        String urlEsperada = "/api/suporte/chamados/" + id + "/anexos/" + nomeArquivo;
        if (!chamado.getAnexos().contains(urlEsperada)) {
            throw new RecursoNaoEncontradoException("Anexo não encontrado");
        }
        
        Path filePath = Paths.get(UPLOAD_DIR + nomeArquivo);
        if (!Files.exists(filePath)) {
            throw new RecursoNaoEncontradoException("Arquivo não encontrado no servidor");
        }
        
        return Files.readAllBytes(filePath);
    }
    
    public EstatisticasSuporteDTO obterEstatisticas(String emailUsuario, Long empresaId) {
        Long totalChamados = empresaId != null ? 
            chamadoSuporteRepository.countByEmpresaId(empresaId) : 
            chamadoSuporteRepository.countByEmailUsuario(emailUsuario);
        
        Long chamadosAbertos = chamadoSuporteRepository.countByStatus(StatusChamado.ABERTO);
        Long chamadosResolvidos = chamadoSuporteRepository.countByStatus(StatusChamado.RESOLVIDO);
        
        Double tempoMedioResolucao = chamadoSuporteRepository.calcularTempoMedioResolucao();
        Double avaliacaoMedia = chamadoSuporteRepository.calcularAvaliacaoMedia();
        
        return new EstatisticasSuporteDTO(totalChamados, chamadosAbertos, chamadosResolvidos, 
                                         tempoMedioResolucao, avaliacaoMedia);
    }
    
    private ChamadoSuporte buscarChamadoEntity(Long id, String emailUsuario, Long empresaId) {
        Optional<ChamadoSuporte> chamado;
        
        // Verificar se é um usuário logado (tem empresa)
        if (empresaId != null) {
            chamado = chamadoSuporteRepository.findByIdAndEmpresaId(id, empresaId);
        } else {
            chamado = chamadoSuporteRepository.findByIdAndEmailUsuario(id, emailUsuario);
        }
        
        if (chamado.isEmpty()) {
            throw new RecursoNaoEncontradoException("Chamado não encontrado");
        }
        
        return chamado.get();
    }
    
    private ChamadoSuporteResponseDTO converterParaResponseDTO(ChamadoSuporte chamado) {
        ChamadoSuporteResponseDTO dto = new ChamadoSuporteResponseDTO();
        dto.setId(chamado.getId());
        dto.setTitulo(chamado.getTitulo());
        dto.setDescricao(chamado.getDescricao());
        dto.setCategoria(chamado.getCategoria());
        dto.setSubcategoria(chamado.getSubcategoria());
        dto.setPrioridade(chamado.getPrioridade());
        dto.setStatus(chamado.getStatus());
        dto.setEmailUsuario(chamado.getEmailUsuario());
        dto.setNomeUsuario(chamado.getNomeUsuario());
        dto.setPaginaErro(chamado.getPaginaErro());
        dto.setAnexos(chamado.getAnexos());
        dto.setRespostaSuporte(chamado.getRespostaSuporte());
        dto.setUsuarioSuporte(chamado.getUsuarioSuporte());
        dto.setDataResposta(chamado.getDataResposta());
        dto.setAvaliacaoNota(chamado.getAvaliacaoNota());
        dto.setAvaliacaoComentario(chamado.getAvaliacaoComentario());
        dto.setDataCriacao(chamado.getDataCriacao());
        dto.setDataAtualizacao(chamado.getDataAtualizacao());
        
        if (chamado.getEmpresa() != null) {
            dto.setEmpresaId(chamado.getEmpresa().getId());
            dto.setNomeEmpresa(chamado.getEmpresa().getNome());
        }
        
        return dto;
    }
}
