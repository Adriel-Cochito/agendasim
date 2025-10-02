package com.agendasim.dto.suporte;

public class EstatisticasSuporteDTO {
    
    private Long totalChamados;
    private Long chamadosAbertos;
    private Long chamadosResolvidos;
    private Double tempoMedioResolucao; // em horas
    private Double avaliacaoMedia;
    private Long chamadosPorCategoria;
    private Long chamadosPorPrioridade;
    
    // Construtores
    public EstatisticasSuporteDTO() {}
    
    public EstatisticasSuporteDTO(Long totalChamados, Long chamadosAbertos, Long chamadosResolvidos, 
                                 Double tempoMedioResolucao, Double avaliacaoMedia) {
        this.totalChamados = totalChamados;
        this.chamadosAbertos = chamadosAbertos;
        this.chamadosResolvidos = chamadosResolvidos;
        this.tempoMedioResolucao = tempoMedioResolucao;
        this.avaliacaoMedia = avaliacaoMedia;
    }
    
    // Getters e Setters
    public Long getTotalChamados() {
        return totalChamados;
    }
    
    public void setTotalChamados(Long totalChamados) {
        this.totalChamados = totalChamados;
    }
    
    public Long getChamadosAbertos() {
        return chamadosAbertos;
    }
    
    public void setChamadosAbertos(Long chamadosAbertos) {
        this.chamadosAbertos = chamadosAbertos;
    }
    
    public Long getChamadosResolvidos() {
        return chamadosResolvidos;
    }
    
    public void setChamadosResolvidos(Long chamadosResolvidos) {
        this.chamadosResolvidos = chamadosResolvidos;
    }
    
    public Double getTempoMedioResolucao() {
        return tempoMedioResolucao;
    }
    
    public void setTempoMedioResolucao(Double tempoMedioResolucao) {
        this.tempoMedioResolucao = tempoMedioResolucao;
    }
    
    public Double getAvaliacaoMedia() {
        return avaliacaoMedia;
    }
    
    public void setAvaliacaoMedia(Double avaliacaoMedia) {
        this.avaliacaoMedia = avaliacaoMedia;
    }
    
    public Long getChamadosPorCategoria() {
        return chamadosPorCategoria;
    }
    
    public void setChamadosPorCategoria(Long chamadosPorCategoria) {
        this.chamadosPorCategoria = chamadosPorCategoria;
    }
    
    public Long getChamadosPorPrioridade() {
        return chamadosPorPrioridade;
    }
    
    public void setChamadosPorPrioridade(Long chamadosPorPrioridade) {
        this.chamadosPorPrioridade = chamadosPorPrioridade;
    }
}
