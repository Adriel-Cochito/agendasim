package com.agendasim.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.agendasim.dto.suporte.AtualizarChamadoSuporteDTO;
import com.agendasim.dto.suporte.AvaliacaoChamadoDTO;
import com.agendasim.dto.suporte.ChamadoSuporteResponseDTO;
import com.agendasim.dto.suporte.ComentarioChamadoDTO;
import com.agendasim.dto.suporte.CriarChamadoSuporteDTO;
import com.agendasim.dto.suporte.EstatisticasSuporteDTO;
import com.agendasim.enums.PrioridadeSuporte;
import com.agendasim.enums.StatusChamado;
import com.agendasim.service.SuporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/suporte")
@Tag(name = "Suporte", description = "API para gerenciamento de chamados de suporte")
public class SuporteController {
    
    @Autowired
    private SuporteService suporteService;
    
    @PostMapping(value = "/chamados", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Criar novo chamado de suporte")
    public ResponseEntity<ChamadoSuporteResponseDTO> criarChamado(
            @Valid @ModelAttribute CriarChamadoSuporteDTO dto,
            @RequestParam(required = false) Long empresaId,
            Authentication authentication) {
        
        String emailUsuario = authentication.getName();
        ChamadoSuporteResponseDTO chamado = suporteService.criarChamado(dto, emailUsuario, empresaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(chamado);
    }
    
    @GetMapping("/chamados")
    @Operation(summary = "Listar chamados de suporte com filtros")
    public ResponseEntity<Page<ChamadoSuporteResponseDTO>> listarChamados(
            @Parameter(description = "Categoria do chamado") @RequestParam(required = false) String categoria,
            @Parameter(description = "Status do chamado") @RequestParam(required = false) StatusChamado status,
            @Parameter(description = "Prioridade do chamado") @RequestParam(required = false) PrioridadeSuporte prioridade,
            @Parameter(description = "Data de início (formato: yyyy-MM-ddTHH:mm:ss)") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @Parameter(description = "Data de fim (formato: yyyy-MM-ddTHH:mm:ss)") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
            @RequestParam(required = false) Long empresaId,
            Pageable pageable,
            Authentication authentication) {
        
        String emailUsuario = authentication.getName();
        Page<ChamadoSuporteResponseDTO> chamados = suporteService.listarChamados(
                emailUsuario, categoria, status, prioridade, dataInicio, dataFim, empresaId, pageable);
        return ResponseEntity.ok(chamados);
    }
    
    @GetMapping("/chamados/{id}")
    @Operation(summary = "Buscar chamado por ID")
    public ResponseEntity<ChamadoSuporteResponseDTO> buscarChamado(
            @Parameter(description = "ID do chamado") @PathVariable Long id,
            @RequestParam(required = false) Long empresaId,
            Authentication authentication) {
        
        String emailUsuario = authentication.getName();
        ChamadoSuporteResponseDTO chamado = suporteService.buscarChamadoPorId(id, emailUsuario, empresaId);
        return ResponseEntity.ok(chamado);
    }
    
    @PutMapping("/chamados/{id}")
    @Operation(summary = "Atualizar chamado de suporte")
    public ResponseEntity<ChamadoSuporteResponseDTO> atualizarChamado(
            @Parameter(description = "ID do chamado") @PathVariable Long id,
            @Valid @RequestBody AtualizarChamadoSuporteDTO dto,
            Authentication authentication) {
        
        String emailUsuario = authentication.getName();
        ChamadoSuporteResponseDTO chamado = suporteService.atualizarChamado(id, dto, emailUsuario, null);
        return ResponseEntity.ok(chamado);
    }
    
    @PatchMapping("/chamados/{id}/fechar")
    @Operation(summary = "Fechar chamado de suporte")
    public ResponseEntity<Void> fecharChamado(
            @Parameter(description = "ID do chamado") @PathVariable Long id,
            Authentication authentication) {
        
        String emailUsuario = authentication.getName();
        suporteService.fecharChamado(id, emailUsuario, null);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/chamados/{id}/reabrir")
    @Operation(summary = "Reabrir chamado de suporte")
    public ResponseEntity<Void> reabrirChamado(
            @Parameter(description = "ID do chamado") @PathVariable Long id,
            Authentication authentication) {
        
        String emailUsuario = authentication.getName();
        suporteService.reabrirChamado(id, emailUsuario, null);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/chamados/{id}/comentarios")
    @Operation(summary = "Adicionar comentário ao chamado")
    public ResponseEntity<Void> adicionarComentario(
            @Parameter(description = "ID do chamado") @PathVariable Long id,
            @Valid @RequestBody ComentarioChamadoDTO dto,
            Authentication authentication) {
        
        String emailUsuario = authentication.getName();
        suporteService.adicionarComentario(id, dto, emailUsuario, null);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/chamados/{id}/avaliacao")
    @Operation(summary = "Avaliar atendimento do chamado")
    public ResponseEntity<Void> avaliarAtendimento(
            @Parameter(description = "ID do chamado") @PathVariable Long id,
            @Valid @RequestBody AvaliacaoChamadoDTO dto,
            Authentication authentication) {
        
        String emailUsuario = authentication.getName();
        suporteService.avaliarAtendimento(id, dto, emailUsuario, null);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping(value = "/chamados/{id}/anexos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload de anexo para o chamado")
    public ResponseEntity<String> uploadAnexo(
            @Parameter(description = "ID do chamado") @PathVariable Long id,
            @Parameter(description = "Arquivo para upload") @RequestParam("arquivo") MultipartFile arquivo,
            Authentication authentication) throws IOException {
        
        String emailUsuario = authentication.getName();
        String urlAnexo = suporteService.uploadAnexo(id, arquivo, emailUsuario, null);
        return ResponseEntity.ok(urlAnexo);
    }
    
    @GetMapping("/chamados/{id}/anexos/{nomeArquivo}")
    @Operation(summary = "Download de anexo do chamado")
    public ResponseEntity<byte[]> downloadAnexo(
            @Parameter(description = "ID do chamado") @PathVariable Long id,
            @Parameter(description = "Nome do arquivo") @PathVariable String nomeArquivo,
            Authentication authentication) throws IOException {
        
        String emailUsuario = authentication.getName();
        byte[] arquivo = suporteService.downloadAnexo(id, nomeArquivo, emailUsuario, null);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(arquivo);
    }
    
    @GetMapping("/estatisticas")
    @Operation(summary = "Obter estatísticas de suporte do usuário")
    public ResponseEntity<EstatisticasSuporteDTO> obterEstatisticas(Authentication authentication) {
        String emailUsuario = authentication.getName();
        EstatisticasSuporteDTO estatisticas = suporteService.obterEstatisticas(emailUsuario, null);
        return ResponseEntity.ok(estatisticas);
    }
    
    // Endpoints públicos (sem autenticação) para usuários não logados
    @PostMapping(value = "/publico/chamados", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Criar chamado de suporte público (sem autenticação)")
    public ResponseEntity<ChamadoSuporteResponseDTO> criarChamadoPublico(
            @Valid @ModelAttribute CriarChamadoSuporteDTO dto) {
        
        ChamadoSuporteResponseDTO chamado = suporteService.criarChamado(dto, dto.getEmailUsuario(), null);
        return ResponseEntity.status(HttpStatus.CREATED).body(chamado);
    }
    
    @GetMapping("/publico/chamados")
    @Operation(summary = "Listar chamados públicos por email")
    public ResponseEntity<Page<ChamadoSuporteResponseDTO>> listarChamadosPublicos(
            @Parameter(description = "Email do usuário") @RequestParam String email,
            @Parameter(description = "Categoria do chamado") @RequestParam(required = false) String categoria,
            @Parameter(description = "Status do chamado") @RequestParam(required = false) StatusChamado status,
            @Parameter(description = "Prioridade do chamado") @RequestParam(required = false) PrioridadeSuporte prioridade,
            @Parameter(description = "Data de início (formato: yyyy-MM-ddTHH:mm:ss)") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @Parameter(description = "Data de fim (formato: yyyy-MM-ddTHH:mm:ss)") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
            Pageable pageable) {
        
        Page<ChamadoSuporteResponseDTO> chamados = suporteService.listarChamados(
                email, categoria, status, prioridade, dataInicio, dataFim, null, pageable);
        return ResponseEntity.ok(chamados);
    }
    
    @GetMapping("/publico/chamados/{id}")
    @Operation(summary = "Buscar chamado público por ID e email")
    public ResponseEntity<ChamadoSuporteResponseDTO> buscarChamadoPublico(
            @Parameter(description = "ID do chamado") @PathVariable Long id,
            @Parameter(description = "Email do usuário") @RequestParam String email) {
        
        ChamadoSuporteResponseDTO chamado = suporteService.buscarChamadoPorId(id, email, null);
        return ResponseEntity.ok(chamado);
    }
}
