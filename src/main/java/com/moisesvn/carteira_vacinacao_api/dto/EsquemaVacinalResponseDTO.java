package com.moisesvn.carteira_vacinacao_api.dto;

/**
 * DTO de resposta para representar um Esquema Vacinal (dose) de uma vacina.
 */
public record EsquemaVacinalResponseDTO(
    Long id,
    Long vacinaId,
    String vacinaNome,
    String descricaoDose,
    Integer idadeRecomendadaMeses,
    Integer intervaloMinimoDias
) {
}
