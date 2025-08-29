// src/main/java/com/agendasim/exception/GlobalExceptionHandler.java
package com.agendasim.exception;

import com.agendasim.exception.response.ApiError;
import com.agendasim.exception.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String traceId = generateTraceId();
        
        List<ApiError.ValidationError> validationErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(this::mapFieldError)
                .collect(Collectors.toList());

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Dados inválidos fornecidos")
                .path(getCurrentPath())
                .method(getCurrentMethod())
                .traceId(traceId)
                .validationErrors(validationErrors)
                .build();

        log.warn("Validation error - TraceId: {} - Errors: {}", traceId, validationErrors.size());

        return ResponseEntity.badRequest().body(ApiResponse.error(error));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        String traceId = generateTraceId();
        
        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getStatus().value())
                .error(ex.getStatus().getReasonPhrase())
                .message(ex.getMessage())
                .path(getCurrentPath())
                .method(getCurrentMethod())
                .traceId(traceId)
                .details(Map.of("code", ex.getCode()))
                .build();

        log.warn("Business error - TraceId: {} - Code: {} - Message: {}", 
                traceId, ex.getCode(), ex.getMessage());

        return ResponseEntity.status(ex.getStatus()).body(ApiResponse.error(error));
    }

    @ExceptionHandler({AccessDeniedException.class, AccessDeniedException.class})
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(Exception ex) {
        String traceId = generateTraceId();
        
        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Access Denied")
                .message("Você não tem permissão para realizar esta ação")
                .path(getCurrentPath())
                .method(getCurrentMethod())
                .traceId(traceId)
                .details(Map.of(
                    "code", "ACCESS_DENIED",
                    "suggestion", "Entre em contato com o administrador se acredita que deveria ter acesso"
                ))
                .build();

        log.warn("Access denied - TraceId: {} - User attempted: {} {}", 
                traceId, getCurrentMethod(), getCurrentPath());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(error));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        String traceId = generateTraceId();
        
        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Authentication Failed")
                .message("Credenciais inválidas")
                .path(getCurrentPath())
                .method(getCurrentMethod())
                .traceId(traceId)
                .details(Map.of(
                    "code", "INVALID_CREDENTIALS",
                    "suggestion", "Verifique seu email e senha"
                ))
                .build();

        log.warn("Authentication failed - TraceId: {} - Path: {}", traceId, getCurrentPath());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(error));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        String traceId = generateTraceId();
        
        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Authentication Required")
                .message("Token de autenticação inválido ou expirado")
                .path(getCurrentPath())
                .method(getCurrentMethod())
                .traceId(traceId)
                .details(Map.of(
                    "code", "AUTHENTICATION_REQUIRED",
                    "suggestion", "Faça login novamente"
                ))
                .build();

        log.warn("Authentication required - TraceId: {} - Path: {}", traceId, getCurrentPath());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(error));
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiResponse<Void>> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        String traceId = generateTraceId();
        
        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource Not Found")
                .message(ex.getMessage())
                .path(getCurrentPath())
                .method(getCurrentMethod())
                .traceId(traceId)
                .details(Map.of(
                    "code", "RESOURCE_NOT_FOUND",
                    "suggestion", "Verifique se o ID está correto"
                ))
                .build();

        log.warn("Resource not found - TraceId: {} - Message: {}", traceId, ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(error));
    }

    @ExceptionHandler({DataIntegrityViolationException.class, IntegridadeReferencialException.class})
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(Exception ex) {
        String traceId = generateTraceId();
        
        String message = "Não é possível realizar esta operação devido a dependências existentes";
        String suggestion = "Remova as dependências antes de continuar";
        
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("SERVICO_PROFISSIONAL")) {
                message = "Profissional está vinculado a serviços ativos";
                suggestion = "Remova o profissional dos serviços antes de excluí-lo";
            } else if (ex.getMessage().contains("AGENDA")) {
                message = "Existem agendamentos associados";
                suggestion = "Cancele ou conclua os agendamentos antes de excluir";
            }
        }

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflict")
                .message(message)
                .path(getCurrentPath())
                .method(getCurrentMethod())
                .traceId(traceId)
                .details(Map.of(
                    "code", "DATA_INTEGRITY_VIOLATION",
                    "suggestion", suggestion
                ))
                .build();

        log.warn("Data integrity violation - TraceId: {} - Message: {}", traceId, message);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        String traceId = generateTraceId();
        
        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("Ocorreu um erro interno no servidor")
                .path(getCurrentPath())
                .method(getCurrentMethod())
                .traceId(traceId)
                .details(Map.of(
                    "code", "INTERNAL_ERROR",
                    "suggestion", "Tente novamente em alguns instantes"
                ))
                .build();

        log.error("Internal server error - TraceId: {} - Exception: {}", 
                traceId, ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(error));
    }

    private ApiError.ValidationError mapFieldError(FieldError fieldError) {
        return ApiError.ValidationError.builder()
                .field(fieldError.getField())
                .rejectedValue(fieldError.getRejectedValue())
                .message(fieldError.getDefaultMessage())
                .code(fieldError.getCode())
                .build();
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private String getCurrentPath() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            return request.getRequestURI();
        }
        return "unknown";
    }

    private String getCurrentMethod() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            return request.getMethod();
        }
        return "unknown";
    }
}