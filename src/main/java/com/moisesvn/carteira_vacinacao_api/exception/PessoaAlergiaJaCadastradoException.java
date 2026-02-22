package com.moisesvn.carteira_vacinacao_api.exception;

/**
 * Exceção lançada quando se tenta vincular uma alergia que já está vinculada
 * à mesma pessoa.
 */
public class PessoaAlergiaJaCadastradoException extends RuntimeException {

    public PessoaAlergiaJaCadastradoException(Long pessoaId, Long alergiaId) {
        super(String.format(
            "A alergia com ID %d já está vinculada à pessoa com ID %d",
            alergiaId,
            pessoaId
        ));
    }
}
