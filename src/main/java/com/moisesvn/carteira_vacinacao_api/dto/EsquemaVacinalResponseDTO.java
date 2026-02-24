package com.moisesvn.carteira_vacinacao_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de resposta para representar um Esquema Vacinal (dose) de uma vacina.
 */
public record EsquemaVacinalResponseDTO(
    @Schema(description = "ID do esquema vacinal.", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,
    @Schema(description = "ID da vacina.", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    Long vacinaId,
    @Schema(description = "Nome da vacina.", example = "Tríplice Viral", requiredMode = Schema.RequiredMode.REQUIRED)
    String vacinaNome,
    @Schema(description = "Descricao da dose.", example = "1a Dose", requiredMode = Schema.RequiredMode.REQUIRED)
    String descricaoDose,
    @Schema(description = "Idade recomendada em meses.", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer idadeRecomendadaMeses,
    @Schema(description = "Intervalo minimo em dias entre doses.", example = "30", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    Integer intervaloMinimoDias
) {
}
