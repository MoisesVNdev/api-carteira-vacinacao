package com.moisesvn.carteira_vacinacao_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO de requisição para atualização de um vínculo `Responsavel`.
 * Permite apenas atualizar o tipo de relação.
 */
public record ResponsavelUpdateRequestDTO(
    @NotBlank(message = "Tipo de relação é obrigatório")
    @Schema(description = "Tipo de relacao com a pessoa.", example = "MAE", requiredMode = Schema.RequiredMode.REQUIRED)
    String tipoRelacao
) {
}
