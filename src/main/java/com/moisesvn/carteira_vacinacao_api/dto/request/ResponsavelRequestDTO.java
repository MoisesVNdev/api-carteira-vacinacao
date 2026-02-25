package com.moisesvn.carteira_vacinacao_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de requisição para criação/atualização de um vínculo `Responsavel`.
 */
public record ResponsavelRequestDTO(
    @Schema(description = "ID do usuario.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long usuarioId,
    @Schema(description = "ID da pessoa.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long pessoaId,
    @Schema(description = "Tipo de relacao com a pessoa.", example = "MAE", requiredMode = Schema.RequiredMode.REQUIRED)
    String tipoRelacao
) {
}
