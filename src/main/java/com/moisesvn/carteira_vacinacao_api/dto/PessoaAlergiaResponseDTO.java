package com.moisesvn.carteira_vacinacao_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO de resposta para representar um vínculo PessoaAlergia.
 * Inclui a descrição da alergia para conveniência do cliente.
 */
public record PessoaAlergiaResponseDTO(
    @Schema(description = "ID da pessoa.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long pessoaId,
    @Schema(description = "ID da alergia.", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    Long alergiaId,
    @Schema(description = "Descricao da alergia.", example = "Alergia a lactose", requiredMode = Schema.RequiredMode.REQUIRED)
    String descricao,
    @Schema(description = "Observacao adicional do vinculo.", example = "Reacao moderada", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String observacao,
    @Schema(description = "Data e hora de criacao do vinculo.", example = "2026-02-23T22:28:06", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime createdAt
) {
}
