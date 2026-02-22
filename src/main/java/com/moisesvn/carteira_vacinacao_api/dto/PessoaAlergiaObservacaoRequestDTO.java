package com.moisesvn.carteira_vacinacao_api.dto;

/**
 * DTO de requisição para atualizar apenas a observação de um vínculo PessoaAlergia.
 */
public record PessoaAlergiaObservacaoRequestDTO(
    String observacao
) {
}
