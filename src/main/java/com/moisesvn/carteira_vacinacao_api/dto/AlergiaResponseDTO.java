package com.moisesvn.carteira_vacinacao_api.dto;

import java.time.LocalDateTime;

/**
 * DTO de resposta para representar uma Alergia (catálogo) exposta pela API.
 */
public record AlergiaResponseDTO(
    Long id,
    String descricao,
    LocalDateTime createdAt
) {
}
