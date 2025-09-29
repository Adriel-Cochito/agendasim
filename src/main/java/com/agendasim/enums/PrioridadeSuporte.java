package com.agendasim.enums;

public enum PrioridadeSuporte {
    BAIXA("Baixa"),
    MEDIA("Média"),
    ALTA("Alta"),
    CRITICA("Crítica");
    
    private final String descricao;
    
    PrioridadeSuporte(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
