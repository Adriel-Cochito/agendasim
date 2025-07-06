package com.agendasim.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class IntegridadeReferencialException extends RuntimeException {
    
    public IntegridadeReferencialException(String mensagem) {
        super(mensagem);
    }
    
    public IntegridadeReferencialException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}