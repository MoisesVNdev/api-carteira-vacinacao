package com.moisesvn.carteira_vacinacao_api.exception;

/**
 * Exceção lançada quando se tenta cadastrar uma alergia que já existe no sistema.
 */
public class AlergiaJaCadastradaException extends RuntimeException {

    public AlergiaJaCadastradaException(String descricao) {
        super(String.format("Alergia '%s' já está cadastrada no sistema", descricao));
    }
}
