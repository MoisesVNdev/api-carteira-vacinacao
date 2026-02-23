package com.moisesvn.carteira_vacinacao_api.exception;

/**
 * Exceção lançada quando uma vacina não é encontrada pelo ID.
 */
public class VacinaNotFoundException extends RuntimeException {
    
    public VacinaNotFoundException(Long id) {
        super(String.format("Vacina com ID %d não encontrada", id));
    }
}
