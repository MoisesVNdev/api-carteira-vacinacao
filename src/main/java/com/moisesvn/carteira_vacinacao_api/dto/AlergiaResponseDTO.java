package com.moisesvn.carteira_vacinacao_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO de resposta para representar uma Alergia (catálogo) exposta pela API.
 */
public record AlergiaResponseDTO(
    @Schema(description = "ID da alergia.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,
    @Schema(description = "Descricao da alergia.", example = "Alergia a lactose", requiredMode = Schema.RequiredMode.REQUIRED)
    String descricao,
    @Schema(description = "Data e hora de criacao do registro.", example = "2026-02-23T22:28:06", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime createdAt
) {
}
