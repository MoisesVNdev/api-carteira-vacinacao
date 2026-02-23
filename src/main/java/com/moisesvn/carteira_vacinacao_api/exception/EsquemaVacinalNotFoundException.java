package com.moisesvn.carteira_vacinacao_api.exception;

/**
 * Exceção lançada quando um esquema vacinal não é encontrado pelo ID.
 */
public class EsquemaVacinalNotFoundException extends RuntimeException {
    
    public EsquemaVacinalNotFoundException(Long id) {
        super(String.format("Esquema vacinal com ID %d não encontrado", id));
    }
}
