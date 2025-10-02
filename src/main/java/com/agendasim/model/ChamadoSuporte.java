package com.agendasim.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.agendasim.enums.PrioridadeSuporte;
import com.agendasim.enums.StatusChamado;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "chamados_suporte")
public class ChamadoSuporte {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Título é obrigatório")
    @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
    @Column(nullable = false, length = 200)
    private String titulo;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
    @Column(nullable = false, length = 2000)
    private String descricao;
    
    @NotBlank(message = "Categoria é obrigatória")
    @Column(nullable = false, length = 50)
    private String categoria;
    
    @NotBlank(message = "Subcategoria é obrigatória")
    @Column(nullable = false, length = 50)
    private String subcategoria;
    
    @NotNull(message = "Prioridade é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadeSuporte prioridade;
    
    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusChamado status = StatusChamado.ABERTO;
    
    @NotBlank(message = "Email do usuário é obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(nullable = false, length = 100)
    private String emailUsuario;
    
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @Column(nullable = true, length = 100)
    private String nomeUsuario;
    
    @Column(length = 100)
    private String paginaErro;
    
    @ElementCollection
    @CollectionTable(name = "chamado_anexos", joinColumns = @JoinColumn(name = "chamado_id"))
    @Column(name = "url_anexo")
    private List<String> anexos = new ArrayList<>();
    
    @Column(length = 2000)
    private String respostaSuporte;
    
    @Column(length = 100)
    private String usuarioSuporte;
    
    @Column(name = "data_resposta")
    private LocalDateTime dataResposta;
    
    @Column(name = "avaliacao_nota")
    private Integer avaliacaoNota;
    
    @Column(name = "avaliacao_comentario", length = 500)
    private String avaliacaoComentario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
    
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
    
    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    // Construtores
    public ChamadoSuporte() {}
    
    public ChamadoSuporte(String titulo, String descricao, String categoria, String subcategoria, 
                         PrioridadeSuporte prioridade, String emailUsuario, String nomeUsuario) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.prioridade = prioridade;
        this.emailUsuario = emailUsuario;
        this.nomeUsuario = nomeUsuario;
    }
    
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
    
    public Empresa getEmpresa() {
        return empresa;
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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
