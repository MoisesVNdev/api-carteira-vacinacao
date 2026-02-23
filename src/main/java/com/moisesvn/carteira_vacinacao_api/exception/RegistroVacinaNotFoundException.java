package com.moisesvn.carteira_vacinacao_api.exception;

/**
 * Exceção lançada quando um registro de vacina não é encontrado pelo ID.
 */
public class RegistroVacinaNotFoundException extends RuntimeException {
    
    public RegistroVacinaNotFoundException(Long id) {
        super(String.format("Registro de vacina com ID %d não encontrado", id));
    }
}
