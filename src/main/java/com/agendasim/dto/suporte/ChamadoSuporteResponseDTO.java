package com.agendasim.dto.suporte;

import com.agendasim.enums.PrioridadeSuporte;
import com.agendasim.enums.StatusChamado;

import java.time.LocalDateTime;
import java.util.List;

public class ChamadoSuporteResponseDTO {
    
    private Long id;
    private String titulo;
    private String descricao;
    private String categoria;
    private String subcategoria;
    private PrioridadeSuporte prioridade;
    private StatusChamado status;
    private String emailUsuario;
    private String nomeUsuario;
    private String paginaErro;
    private List<String> anexos;
    private String respostaSuporte;
    private String usuarioSuporte;
    private LocalDateTime dataResposta;
    private Integer avaliacaoNota;
    private String avaliacaoComentario;
    private Long empresaId;
    private String nomeEmpresa;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    // Construtores
    public ChamadoSuporteResponseDTO() {}
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getSubcategoria() {
        return subcategoria;
    }
    
    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }
    
    public PrioridadeSuporte getPrioridade() {
        return prioridade;
    }
    
    public void setPrioridade(PrioridadeSuporte prioridade) {
        this.prioridade = prioridade;
    }
    
    public StatusChamado getStatus() {
        return status;
    }
    
    public void setStatus(StatusChamado status) {
        this.status = status;
    }
    
    public String getEmailUsuario() {
        return emailUsuario;
    }
    
    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
    
    public String getNomeUsuario() {
        return nomeUsuario;
    }
    
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
    
    public String getPaginaErro() {
        return paginaErro;
    }
    
    public void setPaginaErro(String paginaErro) {
        this.paginaErro = paginaErro;
    }
    
    public List<String> getAnexos() {
        return anexos;
    }
    
    public void setAnexos(List<String> anexos) {
        this.anexos = anexos;
    }
    
    public String getRespostaSuporte() {
        return respostaSuporte;
    }
    
    public void setRespostaSuporte(String respostaSuporte) {
        this.respostaSuporte = respostaSuporte;
    }
    
    public String getUsuarioSuporte() {
        return usuarioSuporte;
    }
    
    public void setUsuarioSuporte(String usuarioSuporte) {
        this.usuarioSuporte = usuarioSuporte;
    }
    
    public LocalDateTime getDataResposta() {
        return dataResposta;
    }
    
    public void setDataResposta(LocalDateTime dataResposta) {
        this.dataResposta = dataResposta;
    }
    
    public Integer getAvaliacaoNota() {
        return avaliacaoNota;
    }
    
    public void setAvaliacaoNota(Integer avaliacaoNota) {
        this.avaliacaoNota = avaliacaoNota;
    }
    
    public String getAvaliacaoComentario() {
        return avaliacaoComentario;
    }
    
    public void setAvaliacaoComentario(String avaliacaoComentario) {
        this.avaliacaoComentario = avaliacaoComentario;
    }
    
    public Long getEmpresaId() {
        return empresaId;
    }
    
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
    
    public String getNomeEmpresa() {
        return nomeEmpresa;
    }
    
    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
