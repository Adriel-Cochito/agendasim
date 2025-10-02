package com.agendasim.enums;

public enum StatusChamado {
    ABERTO("Aberto"),
    EM_ANDAMENTO("Em Andamento"),
    AGUARDANDO_USUARIO("Aguardando Usuário"),
    RESOLVIDO("Resolvido"),
    FECHADO("Fechado");
    
    private final String descricao;
    
    StatusChamado(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
