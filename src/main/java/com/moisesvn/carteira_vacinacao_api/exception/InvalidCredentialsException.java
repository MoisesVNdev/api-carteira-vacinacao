package com.moisesvn.carteira_vacinacao_api.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        // Mensagem genérica intencional para evitar enumeração de usuários
        super("Credenciais inválidas");
    }
}