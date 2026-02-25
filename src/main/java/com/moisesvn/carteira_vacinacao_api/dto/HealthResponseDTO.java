package com.moisesvn.carteira_vacinacao_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de resposta para status de saúde da API.
 * Retornado pelo endpoint GET /api/v1/health (health check).
 */
public record HealthResponseDTO(
    @Schema(
        description = "Status de saúde da API",
        example = "UP",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String status,
    
    @Schema(
        description = "Mensagem descritiva do status",
        example = "API está funcionando corretamente",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String mensagem
) { }
