package com.moisesvn.carteira_vacinacao_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Item de alergia para o POST em lote (PessoaAlergia batch).
 * Mesma estrutura do PessoaAlergiaRequestDTO, mas usado dentro de um array.
 */
public record PessoaAlergiaListRequestItem(
    @NotNull(message = "ID da alergia é obrigatório")
    @Schema(description = "ID da alergia.", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    Long alergiaId,
    
    @Schema(description = "Observacao adicional do vinculo.", example = "Reacao leve", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String observacao
) {
}
