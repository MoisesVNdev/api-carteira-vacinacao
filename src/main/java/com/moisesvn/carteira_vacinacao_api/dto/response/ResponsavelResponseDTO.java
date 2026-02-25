package com.moisesvn.carteira_vacinacao_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO de resposta para representar um `Responsavel` exposto pela API.
 */
public record ResponsavelResponseDTO(
    @Schema(description = "ID do vinculo de responsavel.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,
    @Schema(description = "ID do usuario vinculado.", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    Long usuarioId,
    @Schema(description = "ID da pessoa vinculada.", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    Long pessoaId,
    @Schema(description = "Tipo de relacao com a pessoa.", example = "PAI", requiredMode = Schema.RequiredMode.REQUIRED)
    String tipoRelacao,
    @Schema(description = "Data e hora de criacao do vinculo.", example = "2026-02-23T22:28:06", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime dataCriacao
) {
}
