package com.agendasim.exception;

import org.springframework.http.HttpStatus;

public class ConflitoAgendamentoException extends BusinessException {
    
    public ConflitoAgendamentoException(String message) {
        super(message);
    }
    
    public ConflitoAgendamentoException(String message, HttpStatus status) {
        super(message, status);
    }
}
