package com.moisesvn.carteira_vacinacao_api.exception;

/**
 * Exceção lançada quando se tenta cadastrar um CPF que já existe no sistema.
 */
public class CpfJaCadastradoException extends RuntimeException {

    public CpfJaCadastradoException(String cpf) {
        super(String.format("CPF '%s' já está cadastrado no sistema", cpf));
    }
}
