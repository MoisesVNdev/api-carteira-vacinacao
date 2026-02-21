package com.moisesvn.carteira_vacinacao_api.dto;

import java.time.LocalDateTime;

/**
 * DTO de resposta para representar um `Responsavel` exposto pela API.
 */
public record ResponsavelResponseDTO(
    Long id,
    Long usuarioId,
    Long pessoaId,
    String tipoRelacao,
    LocalDateTime dataCriacao
) {
}
