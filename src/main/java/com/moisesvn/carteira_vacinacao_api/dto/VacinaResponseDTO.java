package com.moisesvn.carteira_vacinacao_api.dto;

import java.time.LocalDateTime;

/**
 * DTO de resposta para representar uma Vacina (catálogo) exposta pela API.
 */
public record VacinaResponseDTO(
    Long id,
    String nome,
    String descricao,
    String doencaEvitada,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
