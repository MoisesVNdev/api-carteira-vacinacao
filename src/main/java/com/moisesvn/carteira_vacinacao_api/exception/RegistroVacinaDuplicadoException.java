package com.moisesvn.carteira_vacinacao_api.exception;

/**
 * Exceção lançada quando já existe um registro da mesma dose (esquema vacinal)
 * para a pessoa especificada.
 */
public class RegistroVacinaDuplicadoException extends RuntimeException {
    
    public RegistroVacinaDuplicadoException(Long pessoaId, Long esquemaVacinalId) {
        super(String.format(
            "Já existe registro da dose (esquema vacinal ID %d) para a pessoa ID %d",
            esquemaVacinalId, pessoaId
        ));
    }
}
