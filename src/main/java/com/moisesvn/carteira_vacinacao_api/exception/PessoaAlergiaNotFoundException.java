package com.moisesvn.carteira_vacinacao_api.exception;

/**
 * Exceção lançada quando um vínculo entre pessoa e alergia não é encontrado.
 */
public class PessoaAlergiaNotFoundException extends RuntimeException {

    public PessoaAlergiaNotFoundException(Long pessoaId, Long alergiaId) {
        super(String.format(
            "Vínculo entre pessoa (ID: %d) e alergia (ID: %d) não encontrado",
            pessoaId,
            alergiaId
        ));
    }
}
