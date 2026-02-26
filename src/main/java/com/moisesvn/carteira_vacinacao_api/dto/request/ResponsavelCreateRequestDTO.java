package com.moisesvn.carteira_vacinacao_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de requisição para criar um novo vínculo de responsável.
 * 
 * O usuário responsável é extraído automaticamente do JWT (SecurityContext).
 * Por isso, este DTO não inclui usuarioId.
 */
public record ResponsavelCreateRequestDTO(
    @NotNull(message = "ID da pessoa é obrigatório")
    @Schema(description = "ID da pessoa a ser vinculada.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long pessoaId,
    
    @NotBlank(message = "Tipo de relação é obrigatório")
    @Schema(description = "Tipo de relação com a pessoa (ex: PAI, MÃE, FILHA, FILHO, TUTOR, RESPONSAVEL).", 
        example = "FILHA", requiredMode = Schema.RequiredMode.REQUIRED)
    String tipoRelacao
) {
}
