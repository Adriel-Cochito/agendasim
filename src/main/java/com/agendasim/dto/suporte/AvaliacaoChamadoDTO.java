package com.agendasim.dto.suporte;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AvaliacaoChamadoDTO {
    
    @NotNull(message = "Nota é obrigatória")
    @Min(value = 1, message = "Nota deve ser no mínimo 1")
    @Max(value = 5, message = "Nota deve ser no máximo 5")
    private Integer nota;
    
    @Size(max = 500, message = "Comentário deve ter no máximo 500 caracteres")
    private String comentario;
    
    // Construtores
    public AvaliacaoChamadoDTO() {}
    
    public AvaliacaoChamadoDTO(Integer nota, String comentario) {
        this.nota = nota;
        this.comentario = comentario;
    }
    
    // Getters e Setters
    public Integer getNota() {
        return nota;
    }
    
    public void setNota(Integer nota) {
        this.nota = nota;
    }
    
    public String getComentario() {
        return comentario;
    }
    
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
