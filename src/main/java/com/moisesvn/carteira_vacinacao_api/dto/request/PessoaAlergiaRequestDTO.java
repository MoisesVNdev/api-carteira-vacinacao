package com.moisesvn.carteira_vacinacao_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de requisição para vincular uma alergia a uma pessoa (POST simples).
 */
public record PessoaAlergiaRequestDTO(
    @NotNull(message = "ID da alergia é obrigatório")
    @Schema(description = "ID da alergia.", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    Long alergiaId,
    
    @Schema(description = "Observacao adicional do vinculo.", example = "Reacao leve", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String observacao
) {
}
