package com.moisesvn.carteira_vacinacao_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de resposta com token JWT e metadados.
 * Record imutável contendo o token para autenticação subsequente.
 *
 * @param token Token JWT para incluir no header Authorization
 * @param tipo Tipo de token (sempre "Bearer")
 * @param expiraEm Timestamp de expiração em milissegundos (epoch)
 */
public record LoginResponseDTO(
    @Schema(description = "Token JWT para uso no header Authorization.", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", requiredMode = Schema.RequiredMode.REQUIRED)
    String token,
    @Schema(description = "Tipo do token.", example = "Bearer", requiredMode = Schema.RequiredMode.REQUIRED)
    String tipo,
    @Schema(description = "Timestamp de expiracao em milissegundos (epoch).", example = "1700000000000", requiredMode = Schema.RequiredMode.REQUIRED)
    long expiraEm
) {}