// src/main/java/com/agendasim/exception/SecurityException.java
package com.agendasim.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public AccessDeniedException(String message) {
        this(message, HttpStatus.FORBIDDEN, "ACCESS_DENIED");
    }

    public AccessDeniedException(String message, HttpStatus status, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() { 
        return status; 
    }
    
    public String getCode() { 
        return code; 
    }
}