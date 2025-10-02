package com.agendasim.mapper;

import com.agendasim.dto.suporte.ChamadoSuporteResponseDTO;
import com.agendasim.dto.suporte.CriarChamadoSuporteDTO;
import com.agendasim.model.ChamadoSuporte;
import org.springframework.stereotype.Component;

@Component
public class SuporteMapper {
    
    public ChamadoSuporteResponseDTO toResponseDTO(ChamadoSuporte chamado) {
        if (chamado == null) {
            return null;
        }
        
        ChamadoSuporteResponseDTO dto = new ChamadoSuporteResponseDTO();
        dto.setId(chamado.getId());
        dto.setTitulo(chamado.getTitulo());
        dto.setDescricao(chamado.getDescricao());
        dto.setCategoria(chamado.getCategoria());
        dto.setSubcategoria(chamado.getSubcategoria());
        dto.setPrioridade(chamado.getPrioridade());
        dto.setStatus(chamado.getStatus());
        dto.setEmailUsuario(chamado.getEmailUsuario());
        dto.setNomeUsuario(chamado.getNomeUsuario());
        dto.setPaginaErro(chamado.getPaginaErro());
        dto.setAnexos(chamado.getAnexos());
        dto.setRespostaSuporte(chamado.getRespostaSuporte());
        dto.setUsuarioSuporte(chamado.getUsuarioSuporte());
        dto.setDataResposta(chamado.getDataResposta());
        dto.setAvaliacaoNota(chamado.getAvaliacaoNota());
        dto.setAvaliacaoComentario(chamado.getAvaliacaoComentario());
        dto.setDataCriacao(chamado.getDataCriacao());
        dto.setDataAtualizacao(chamado.getDataAtualizacao());
        
        if (chamado.getEmpresa() != null) {
            dto.setEmpresaId(chamado.getEmpresa().getId());
            dto.setNomeEmpresa(chamado.getEmpresa().getNome());
        }
        
        return dto;
    }
    
    public ChamadoSuporte toEntity(CriarChamadoSuporteDTO dto) {
        if (dto == null) {
            return null;
        }
        
        ChamadoSuporte chamado = new ChamadoSuporte();
        chamado.setTitulo(dto.getTitulo());
        chamado.setDescricao(dto.getDescricao());
        chamado.setCategoria(dto.getCategoria());
        chamado.setSubcategoria(dto.getSubcategoria());
        chamado.setPrioridade(dto.getPrioridade());
        chamado.setEmailUsuario(dto.getEmailUsuario());
        chamado.setNomeUsuario(dto.getNomeUsuario());
        chamado.setPaginaErro(dto.getPaginaErro());
        chamado.setAnexos(dto.getAnexos());
        
        return chamado;
    }
}
