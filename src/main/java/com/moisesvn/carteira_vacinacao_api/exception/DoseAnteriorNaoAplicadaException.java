package com.moisesvn.carteira_vacinacao_api.exception;

/**
 * Exceção lançada quando há tentativa de registrar uma dose sem que
 * a dose anterior da mesma vacina esteja aplicada (validação de hierarquia de doses).
 */
public class DoseAnteriorNaoAplicadaException extends RuntimeException {
    
    public DoseAnteriorNaoAplicadaException(String nomeVacina, String doseAtual, String doseAnterior) {
        super(String.format(
            "Não é possível registrar a dose '%s' da vacina '%s' sem que a dose '%s' esteja aplicada",
            doseAtual, nomeVacina, doseAnterior
        ));
    }
}
