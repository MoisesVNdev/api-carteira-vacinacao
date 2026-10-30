package com.moisesvn.carteira_vacinacao_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de requisição para criar um novo vínculo de responsável.
 *
 * O usuário responsável é extraído automaticamente do JWT (SecurityContext).
 * Por isso, este DTO não inclui usuarioId.
 *
 * O CNS da pessoa é obrigatório como prova de que o usuário conhece a pessoa:
 * sem ele, qualquer usuário poderia se vincular a qualquer pessoa só pelo ID.
 */
public record ResponsavelCreateRequestDTO(
    @NotNull(message = "ID da pessoa é obrigatório")
    @Schema(description = "ID da pessoa a ser vinculada.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long pessoaId,

    @NotBlank(message = "CNS da pessoa é obrigatório")
    @Schema(description = "CNS (Cartão Nacional de Saúde) da pessoa, para confirmar o vínculo.",
        example = "898001160000000", requiredMode = Schema.RequiredMode.REQUIRED)
    String cns,

    @NotBlank(message = "Tipo de relação é obrigatório")
    @Schema(description = "Tipo de relação com a pessoa (ex: PAI, MÃE, FILHA, FILHO, TUTOR, RESPONSAVEL).",
        example = "FILHA", requiredMode = Schema.RequiredMode.REQUIRED)
    String tipoRelacao
) {
}
