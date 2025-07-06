package com.agendasim.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ValidationExceptionHandler {

    public static class ErrorDetail {
        private String field;
        private String message;
        private String code;

        public ErrorDetail(String field, String message, String code) {
            this.field = field;
            this.message = message;
            this.code = code;
        }

        public String getField() { return field; }
        public String getMessage() { return message; }
        public String getCode() { return code; }
    }

    public static class StandardErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private List<ErrorDetail> errors;
        private String path;

        public StandardErrorResponse(LocalDateTime timestamp, int status, List<ErrorDetail> errors, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.errors = errors;
            this.path = path;
        }

        public LocalDateTime getTimestamp() { return timestamp; }
        public int getStatus() { return status; }
        public List<ErrorDetail> getErrors() { return errors; }
        public String getPath() { return path; }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<ErrorDetail> errorDetails = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorDetails.add(new ErrorDetail(
                fieldError.getField(),
                fieldError.getDefaultMessage(),
                fieldError.getCode()
            ));
        }

        StandardErrorResponse response = new StandardErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            errorDetails,
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        String mensagem = "Não é possível excluir este registro pois ele possui dependências associadas";
        String detalhes = ex.getMessage();
        
        if (detalhes != null) {
            if (detalhes.contains("SERVICO_PROFISSIONAL")) {
                mensagem = "Não é possível excluir este profissional pois ele está associado a um ou mais serviços";
            } else if (detalhes.contains("AGENDA")) {
                mensagem = "Não é possível excluir este registro pois ele possui agendamentos associados";
            } else if (detalhes.contains("DISPONIBILIDADE")) {
                mensagem = "Não é possível excluir este registro pois ele possui disponibilidades associadas";
            }
        }

        List<ErrorDetail> errorDetails = List.of(
            new ErrorDetail("id", mensagem, "DATA_INTEGRITY_VIOLATION")
        );

        StandardErrorResponse response = new StandardErrorResponse(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            errorDetails,
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IntegridadeReferencialException.class)
    public ResponseEntity<StandardErrorResponse> handleIntegridadeReferencial(IntegridadeReferencialException ex, WebRequest request) {
        List<ErrorDetail> errorDetails = List.of(
            new ErrorDetail("id", ex.getMessage(), "REFERENTIAL_INTEGRITY_VIOLATION")
        );

        StandardErrorResponse response = new StandardErrorResponse(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            errorDetails,
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<StandardErrorResponse> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex, WebRequest request) {
        List<ErrorDetail> errorDetails = List.of(
            new ErrorDetail("id", ex.getMessage(), "NOT_FOUND")
        );

        StandardErrorResponse response = new StandardErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            errorDetails,
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}