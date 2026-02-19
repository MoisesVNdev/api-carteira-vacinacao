package com.moisesvn.carteira_vacinacao_api.exception;

public class ResponsavelJaCadastradoException extends RuntimeException {
    public ResponsavelJaCadastradoException(Long usuarioId, Long pessoaId) {
        super("Vínculo já existe entre usuario=" + usuarioId + " e pessoa=" + pessoaId);
    }
}
