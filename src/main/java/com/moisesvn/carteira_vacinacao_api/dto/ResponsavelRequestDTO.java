package com.moisesvn.carteira_vacinacao_api.dto;

/**
 * DTO de requisição para criação/atualização de um vínculo `Responsavel`.
 */
public record ResponsavelRequestDTO(
    Long usuarioId,
    Long pessoaId,
    String tipoRelacao
) {
}
