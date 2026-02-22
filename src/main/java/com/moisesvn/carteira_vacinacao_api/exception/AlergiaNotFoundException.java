package com.moisesvn.carteira_vacinacao_api.exception;

/**
 * Exceção lançada quando uma alergia não é encontrada no sistema.
 */
public class AlergiaNotFoundException extends RuntimeException {

    public AlergiaNotFoundException(Long id) {
        super(String.format("Alergia com ID %d não encontrada", id));
    }
}
