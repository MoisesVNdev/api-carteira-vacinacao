package com.moisesvn.carteira_vacinacao_api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de resposta para representar um Registro de Vacina (dose aplicada).
 */
public record RegistroVacinaResponseDTO(
    Long id,
    String vacinaNome,
    String dose,
    LocalDate dataAplicacao,
    String lote,
    String fabricante,
    String vacinador,
    String localAplicacao,
    LocalDateTime createdAt
) {
}
