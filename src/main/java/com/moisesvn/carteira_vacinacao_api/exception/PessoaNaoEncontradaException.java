package com.moisesvn.carteira_vacinacao_api.exception;

public class PessoaNaoEncontradaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PessoaNaoEncontradaException(Long id) {
        super("Pessoa não encontrada com id: " + id);
    }

    public PessoaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
