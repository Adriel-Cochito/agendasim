package com.agendasim.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NomeUnicoJaCadastradoException extends RuntimeException {
    
    public NomeUnicoJaCadastradoException(String nomeUnico) {
        super("Nome único '" + nomeUnico + "' já está em uso por outra empresa");
    }
    
    public NomeUnicoJaCadastradoException(String nomeUnico, String mensagemAdicional) {
        super("Nome único '" + nomeUnico + "' já está em uso por outra empresa. " + mensagemAdicional);
    }
}
