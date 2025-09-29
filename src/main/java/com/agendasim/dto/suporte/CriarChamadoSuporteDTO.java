package com.agendasim.dto.suporte;

import com.agendasim.enums.PrioridadeSuporte;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CriarChamadoSuporteDTO {
    
    @NotBlank(message = "Título é obrigatório")
    @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
    private String titulo;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
    private String descricao;
    
    @NotBlank(message = "Categoria é obrigatória")
    private String categoria;
    
    @NotBlank(message = "Subcategoria é obrigatória")
    private String subcategoria;
    
    @NotNull(message = "Prioridade é obrigatória")
    private PrioridadeSuporte prioridade;
    
    @NotBlank(message = "Email do usuário é obrigatório")
    @Email(message = "Email deve ser válido")
    private String emailUsuario;
    
    private String nomeUsuario;
    
    private String paginaErro;
    
    private List<String> anexos;
    
    // Construtores
    public CriarChamadoSuporteDTO() {}
    
    public CriarChamadoSuporteDTO(String titulo, String descricao, String categoria, String subcategoria, 
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
}
