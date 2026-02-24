package com.moisesvn.carteira_vacinacao_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de resposta para representar um Registro de Vacina (dose aplicada).
 */
public record RegistroVacinaResponseDTO(
    @Schema(description = "ID do registro de vacina.", example = "15", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,
    @Schema(description = "Nome da vacina.", example = "BCG", requiredMode = Schema.RequiredMode.REQUIRED)
    String vacinaNome,
    @Schema(description = "Descricao da dose.", example = "1a Dose", requiredMode = Schema.RequiredMode.REQUIRED)
    String dose,
    @Schema(description = "Data de aplicacao da dose.", example = "2024-06-01", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate dataAplicacao,
    @Schema(description = "Lote da vacina.", example = "LOT-2024-XYZ", requiredMode = Schema.RequiredMode.REQUIRED)
    String lote,
    @Schema(description = "Fabricante da vacina.", example = "Fiocruz", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String fabricante,
    @Schema(description = "Profissional que aplicou a dose.", example = "Dra. Ana", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String vacinador,
    @Schema(description = "Local da aplicacao.", example = "UBS Centro", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String localAplicacao,
    @Schema(description = "Data e hora de criacao do registro.", example = "2026-02-23T22:28:06", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime createdAt
) {
}
