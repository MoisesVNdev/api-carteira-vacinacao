package com.moisesvn.carteira_vacinacao_api.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário não encontrado com id: " + id);
    }
    
    public UsuarioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}