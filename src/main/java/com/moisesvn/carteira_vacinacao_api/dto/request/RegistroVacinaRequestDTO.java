package com.moisesvn.carteira_vacinacao_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

/**
 * DTO de requisição para registrar a aplicação de uma dose de vacina.
 * 
 * IMPORTANTE: O campo `pessoaId` vem da URL do endpoint, não do body.
 * O `usuario_id` é extraído do token JWT, nunca do body.
 */
public record RegistroVacinaRequestDTO(
    
    @NotNull(message = "ID da pessoa é obrigatório")
    @Schema(description = "ID da pessoa.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long pessoaId,
    
    @NotNull(message = "ID do esquema vacinal é obrigatório")
    @Schema(description = "ID do esquema vacinal.", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    Long esquemaVacinalId,
    
    @NotNull(message = "Data de aplicação é obrigatória")
    @PastOrPresent(message = "Data de aplicação não pode ser futura")
    @Schema(description = "Data de aplicacao.", example = "2024-06-01", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate dataAplicacao,
    
    @NotBlank(message = "Lote da vacina é obrigatório")
    @Schema(description = "Lote da vacina.", example = "LOT-2024-XYZ", requiredMode = Schema.RequiredMode.REQUIRED)
    String lote,
    
    @Schema(description = "Fabricante da vacina.", example = "Fiocruz", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String fabricante,
    
    @Schema(description = "Profissional que aplicou a dose.", example = "Dra. Ana", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String vacinador,
    
    @Schema(description = "Local de aplicacao.", example = "UBS Centro", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String localAplicacao
) {
}
