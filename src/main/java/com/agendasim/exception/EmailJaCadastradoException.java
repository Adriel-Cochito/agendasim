package com.agendasim.exception;

/**
 * Exception lançada quando se tenta cadastrar um profissional com email já existente
 */
public class EmailJaCadastradoException extends RuntimeException {
    
    private final String email;
    
    public EmailJaCadastradoException(String email) {
        super("Já existe um profissional cadastrado com o email: " + email);
        this.email = email;
    }
    
    public EmailJaCadastradoException(String email, String message) {
        super(message);
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
}