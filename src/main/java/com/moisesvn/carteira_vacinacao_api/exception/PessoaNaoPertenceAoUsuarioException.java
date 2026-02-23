package com.moisesvn.carteira_vacinacao_api.exception;

/**
 * Exceção lançada quando um usuário tenta acessar dados de uma pessoa
 * que não está vinculada a ele na tabela responsavel.
 */
public class PessoaNaoPertenceAoUsuarioException extends RuntimeException {
    
    public PessoaNaoPertenceAoUsuarioException(Long pessoaId, Long usuarioId) {
        super(String.format(
            "A pessoa ID %d não pertence ao usuário ID %d",
            pessoaId, usuarioId
        ));
    }
    
    public PessoaNaoPertenceAoUsuarioException(Long pessoaId) {
        super(String.format(
            "A pessoa ID %d não pertence ao usuário autenticado",
            pessoaId
        ));
    }
}
