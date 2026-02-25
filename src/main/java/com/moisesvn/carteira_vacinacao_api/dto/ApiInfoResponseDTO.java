package com.moisesvn.carteira_vacinacao_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de resposta para informações gerais da API.
 * Retornado pelo endpoint GET /api/v1/health (home).
 */
public record ApiInfoResponseDTO(
    @Schema(
        description = "Nome da API",
        example = "API Carteira de Vacinação Digital",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String nome,
    
    @Schema(
        description = "Versão da API",
        example = "v0.0.1-SNAPSHOT",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String versao,
    
    @Schema(
        description = "Listagem de endpoints disponíveis",
        example = "Endpoints disponíveis: /api/v1/auth/register, /api/v1/auth/login, ...",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String endpoints
) { }
