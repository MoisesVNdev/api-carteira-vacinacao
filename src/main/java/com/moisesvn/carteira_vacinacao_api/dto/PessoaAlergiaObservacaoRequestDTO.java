package com.moisesvn.carteira_vacinacao_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de requisição para atualizar apenas a observação de um vínculo PessoaAlergia.
 */
public record PessoaAlergiaObservacaoRequestDTO(
    @Schema(description = "Observacao atualizada do vinculo.", example = "Reacao moderada", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String observacao
) {
}
