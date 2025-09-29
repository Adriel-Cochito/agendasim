package com.agendasim.dto.suporte;

import com.agendasim.enums.PrioridadeSuporte;
import com.agendasim.enums.StatusChamado;
import jakarta.validation.constraints.Size;

public class AtualizarChamadoSuporteDTO {
    
    @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
    private String titulo;
    
    @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
    private String descricao;
    
    private String categoria;
    
    private String subcategoria;
    
    private PrioridadeSuporte prioridade;
    
    private StatusChamado status;
    
    @Size(max = 100, message = "Página do erro deve ter no máximo 100 caracteres")
    private String paginaErro;
    
    @Size(max = 2000, message = "Resposta do suporte deve ter no máximo 2000 caracteres")
    private String respostaSuporte;
    
    @Size(max = 100, message = "Usuário do suporte deve ter no máximo 100 caracteres")
    private String usuarioSuporte;
    
    // Construtores
    public AtualizarChamadoSuporteDTO() {}
    
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
    
    public StatusChamado getStatus() {
        return status;
    }
    
    public void setStatus(StatusChamado status) {
        this.status = status;
    }
    
    public String getPaginaErro() {
        return paginaErro;
    }
    
    public void setPaginaErro(String paginaErro) {
        this.paginaErro = paginaErro;
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
}
