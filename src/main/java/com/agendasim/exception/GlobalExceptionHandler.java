// GlobalExceptionHandler.java (atualizado com EmailJaCadastradoException)
package com.agendasim.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * NOVO: Trata especificamente a exception de email já cadastrado
     */
    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ErrorResponse> handleEmailJaCadastrado(
            EmailJaCadastradoException ex, 
            HttpServletRequest request) {
        
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        
        logger.warn("Email já cadastrado - TraceId: {} - Email: {}", traceId, ex.getEmail());
        
        ErrorResponse errorResponse = new ErrorResponse(
            false,
            new ErrorDetails(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                HttpStatus.CONFLICT.value(),
                "Email já cadastrado",
                ex.getMessage(),
                request.getRequestURI(),
                request.getMethod(),
                traceId,
                new ErrorDetailsInfo("EMAIL_ALREADY_EXISTS", "Tente usar um email diferente ou faça login se já possui conta")
            )
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * NOVO: Trata especificamente a exception de conflito de agendamento
     */
    @ExceptionHandler(ConflitoAgendamentoException.class)
    public ResponseEntity<ErrorResponse> handleConflitoAgendamento(
            ConflitoAgendamentoException ex, 
            HttpServletRequest request) {
        
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        
        logger.warn("Conflito de agendamento - TraceId: {} - Message: {}", traceId, ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            false,
            new ErrorDetails(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                HttpStatus.CONFLICT.value(),
                "Conflito de Agendamento",
                ex.getMessage(),
                request.getRequestURI(),
                request.getMethod(),
                traceId,
                new ErrorDetailsInfo("SCHEDULING_CONFLICT", "Escolha outro horário ou profissional para o agendamento")
            )
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex, 
            HttpServletRequest request) {
        
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        
        // Log do erro
        logger.warn("Validation error - TraceId: {} - Violations: {}", traceId, ex.getConstraintViolations().size());
        
        // Pegar a primeira violação para a mensagem principal
        ConstraintViolation<?> firstViolation = ex.getConstraintViolations().iterator().next();
        String mainMessage = firstViolation.getMessage();
        
        // Se houver múltiplas violações, combinar as mensagens
        if (ex.getConstraintViolations().size() > 1) {
            StringBuilder messages = new StringBuilder();
            for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
                if (messages.length() > 0) messages.append("; ");
                messages.append(violation.getMessage());
            }
            mainMessage = messages.toString();
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            false,
            new ErrorDetails(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error", 
                mainMessage,
                request.getRequestURI(),
                request.getMethod(),
                traceId,
                new ErrorDetailsInfo("VALIDATION_ERROR", "Verifique os campos e tente novamente")
            )
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ErrorResponse> handleTransactionSystemException(
            TransactionSystemException ex, 
            HttpServletRequest request) {
        
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        
        // Verificar se é um erro de validação dentro da transação
        if (ex.getCause() != null && ex.getCause().getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException constraintEx = (ConstraintViolationException) ex.getCause().getCause();
            
            logger.warn("Transaction validation error - TraceId: {} - Violations: {}", traceId, constraintEx.getConstraintViolations().size());
            
            // Pegar a primeira violação para a mensagem principal
            ConstraintViolation<?> firstViolation = constraintEx.getConstraintViolations().iterator().next();
            String mainMessage = firstViolation.getMessage();
            
            // Se houver múltiplas violações, combinar as mensagens
            if (constraintEx.getConstraintViolations().size() > 1) {
                StringBuilder messages = new StringBuilder();
                for (ConstraintViolation<?> violation : constraintEx.getConstraintViolations()) {
                    if (messages.length() > 0) messages.append("; ");
                    messages.append(violation.getMessage());
                }
                mainMessage = messages.toString();
            }
            
            ErrorResponse errorResponse = new ErrorResponse(
                false,
                new ErrorDetails(
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    HttpStatus.BAD_REQUEST.value(),
                    "Validation Error",
                    mainMessage,
                    request.getRequestURI(),
                    request.getMethod(),
                    traceId,
                    new ErrorDetailsInfo("VALIDATION_ERROR", "Verifique os campos e tente novamente")
                )
            );
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
        // Se não for um erro de validação, tratar como erro interno
        logger.error("Transaction error - TraceId: {} - Exception: {}", traceId, ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            false,
            new ErrorDetails(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocorreu um erro interno no servidor",
                request.getRequestURI(),
                request.getMethod(),
                traceId,
                new ErrorDetailsInfo("INTERNAL_ERROR", "Tente novamente em alguns instantes")
            )
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, 
            HttpServletRequest request) {
        
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        
        logger.warn("Method argument validation error - TraceId: {} - Field errors: {}", traceId, ex.getFieldErrorCount());
        
        // Pegar o primeiro erro de campo
        String mainMessage = "Dados inválidos";
        if (ex.getFieldError() != null) {
            mainMessage = ex.getFieldError().getDefaultMessage();
        }
        
        // Se houver múltiplos erros, combinar as mensagens
        if (ex.getFieldErrorCount() > 1) {
            StringBuilder messages = new StringBuilder();
            ex.getFieldErrors().forEach(error -> {
                if (messages.length() > 0) messages.append("; ");
                messages.append(error.getDefaultMessage());
            });
            mainMessage = messages.toString();
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            false,
            new ErrorDetails(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                mainMessage,
                request.getRequestURI(),
                request.getMethod(),
                traceId,
                new ErrorDetailsInfo("VALIDATION_ERROR", "Verifique os campos e tente novamente")
            )
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleRecursoNaoEncontrado(
            RecursoNaoEncontradoException ex, 
            HttpServletRequest request) {
        
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        
        logger.warn("Recurso não encontrado - TraceId: {} - Message: {}", traceId, ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            false,
            new ErrorDetails(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                ex.getMessage(),
                request.getRequestURI(),
                request.getMethod(),
                traceId,
                new ErrorDetailsInfo("RESOURCE_NOT_FOUND", "O recurso solicitado não foi encontrado")
            )
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, 
            HttpServletRequest request) {
        
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        
        logger.error("Internal server error - TraceId: {} - Exception: {}", traceId, ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            false,
            new ErrorDetails(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocorreu um erro interno no servidor",
                request.getRequestURI(),
                request.getMethod(),
                traceId,
                new ErrorDetailsInfo("INTERNAL_ERROR", "Tente novamente em alguns instantes")
            )
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // Classes para estrutura de resposta de erro
    public static class ErrorResponse {
        private boolean success;
        private ErrorDetails error;
        
        public ErrorResponse(boolean success, ErrorDetails error) {
            this.success = success;
            this.error = error;
        }
        
        // Getters e Setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public ErrorDetails getError() { return error; }
        public void setError(ErrorDetails error) { this.error = error; }
    }
    
    public static class ErrorDetails {
        private String timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
        private String method;
        private String traceId;
        private ErrorDetailsInfo details;
        
        public ErrorDetails(String timestamp, int status, String error, String message, 
                          String path, String method, String traceId, ErrorDetailsInfo details) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
            this.method = method;
            this.traceId = traceId;
            this.details = details;
        }
        
        // Getters e Setters
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        public String getTraceId() { return traceId; }
        public void setTraceId(String traceId) { this.traceId = traceId; }
        public ErrorDetailsInfo getDetails() { return details; }
        public void setDetails(ErrorDetailsInfo details) { this.details = details; }
    }
    
    public static class ErrorDetailsInfo {
        private String code;
        private String suggestion;
        
        public ErrorDetailsInfo(String code, String suggestion) {
            this.code = code;
            this.suggestion = suggestion;
        }
        
        // Getters e Setters
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getSuggestion() { return suggestion; }
        public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
    }
}