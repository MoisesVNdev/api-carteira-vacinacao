package com.moisesvn.carteira_vacinacao_api.exception;

public class TokenInvalidoException extends RuntimeException {
    public TokenInvalidoException(String motivo) {
        super("Token inválido: " + motivo);
    }
}