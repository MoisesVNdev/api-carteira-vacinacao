package com.moisesvn.carteira_vacinacao_api.exception;

/**
 * Exceção lançada quando se tenta cadastrar um CNS que já existe no sistema.
 */
public class CnsJaCadastradoException extends RuntimeException {

    public CnsJaCadastradoException(String cns) {
        super(String.format("CNS '%s' já está cadastrado no sistema", cns));
    }
}
