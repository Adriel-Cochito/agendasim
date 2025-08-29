// src/main/java/com/agendasim/exception/response/ApiError.java
package com.agendasim.exception.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;
    
    private Integer status;
    private String error;
    private String message;
    private String path;
    private String method;
    private String traceId;
    
    // Para validações e múltiplos erros
    private List<ValidationError> validationErrors;
    
    // Para dados adicionais específicos do erro
    private Map<String, Object> details;
    
    @Data
    @Builder
    public static class ValidationError {
        private String field;
        private Object rejectedValue;
        private String message;
        private String code;
    }
}