package com.agendasim.dto.suporte;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ComentarioChamadoDTO {
    
    @NotBlank(message = "Comentário é obrigatório")
    @Size(max = 1000, message = "Comentário deve ter no máximo 1000 caracteres")
    private String comentario;
    
    // Construtores
    public ComentarioChamadoDTO() {}
    
    public ComentarioChamadoDTO(String comentario) {
        this.comentario = comentario;
    }
    
    // Getters e Setters
    public String getComentario() {
        return comentario;
    }
    
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
