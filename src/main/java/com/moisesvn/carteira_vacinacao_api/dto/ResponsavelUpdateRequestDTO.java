package com.moisesvn.carteira_vacinacao_api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de requisição para atualização de um vínculo `Responsavel`.
 * Permite apenas atualizar o tipo de relação.
 */
public record ResponsavelUpdateRequestDTO(
    @NotBlank(message = "Tipo de relação é obrigatório")
    String tipoRelacao
) {
}
