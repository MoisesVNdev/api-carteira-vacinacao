package com.moisesvn.carteira_vacinacao_api.dto.response;

import com.moisesvn.carteira_vacinacao_api.model.StatusVacinal;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 * DTO de resposta para representar um item do calendário vacinal personalizado.
 * 
 * Combina os dados do esquema vacinal (dose prevista) com o status dinâmico
 * calculado com base nos registros da pessoa:
 * - APLICADA: dose já registrada
 * - PENDENTE: dose não aplicada, dentro do prazo
 * - ATRASADA: dose não aplicada, fora do prazo
 */
public record CalendarioVacinalItemResponseDTO(
    @Schema(description = "ID da vacina.", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    Long vacinaId,
    @Schema(description = "Nome da vacina.", example = "Tríplice Viral", requiredMode = Schema.RequiredMode.REQUIRED)
    String vacinaNome,
    @Schema(description = "ID do esquema vacinal.", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    Long esquemaVacinalId,
    @Schema(description = "Descricao da dose.", example = "1a Dose", requiredMode = Schema.RequiredMode.REQUIRED)
    String dose,
    @Schema(description = "Idade recomendada em meses.", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer idadeRecomendadaMeses,
    @Schema(description = "Data prevista da aplicacao.", example = "2024-06-01", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate dataPrevista,
    @Schema(description = "Status calculado da dose.", example = "PENDENTE", allowableValues = {"APLICADA", "PENDENTE", "ATRASADA"}, requiredMode = Schema.RequiredMode.REQUIRED)
    StatusVacinal status,
    @Schema(description = "Registro da dose aplicada quando o status for APLICADA.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    RegistroVacinaResponseDTO registro // null se status != APLICADA
) {
}
