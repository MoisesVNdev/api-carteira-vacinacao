package com.moisesvn.carteira_vacinacao_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO de resposta com dados públicos do usuário.
 * Record imutável, nunca expõe a senha ou dados sensíveis.
 *
 * @param id ID único do usuário
 * @param nomeCompleto Nome completo do usuário
 * @param email Email do usuário
 * @param dataCadastro Data e hora do cadastro
 */
public record UsuarioResponseDTO(
    @Schema(description = "ID do usuario.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,
    @Schema(description = "Nome completo do usuario.", example = "Maria da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    String nomeCompleto,
    @Schema(description = "Email do usuario.", example = "maria@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    String email,
    @Schema(description = "Data e hora do cadastro.", example = "2026-02-23T22:28:06", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime dataCadastro
) {}