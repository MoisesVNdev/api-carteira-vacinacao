package com.moisesvn.carteira_vacinacao_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO de resposta para representar uma Vacina (catálogo) exposta pela API.
 */
public record VacinaResponseDTO(
    @Schema(description = "ID da vacina.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,
    @Schema(description = "Nome da vacina.", example = "BCG", requiredMode = Schema.RequiredMode.REQUIRED)
    String nome,
    @Schema(description = "Descricao da vacina.", example = "Vacina contra tuberculose", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String descricao,
    @Schema(description = "Doenca evitada pela vacina.", example = "Tuberculose", requiredMode = Schema.RequiredMode.REQUIRED)
    String doencaEvitada,
    @Schema(description = "Data e hora de criacao do registro.", example = "2026-02-23T22:28:06", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime createdAt,
    @Schema(description = "Data e hora da ultima atualizacao.", example = "2026-02-23T22:28:06", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    LocalDateTime updatedAt
) {
}
