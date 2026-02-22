package com.moisesvn.carteira_vacinacao_api.exception;

/**
 * Exceção lançada quando um responsável não é encontrado pelo ID.
 */
public class ResponsavelNaoEncontradoException extends RuntimeException {

    public ResponsavelNaoEncontradoException(Long id) {
        super(String.format("Responsável com ID %d não encontrado", id));
    }
}
