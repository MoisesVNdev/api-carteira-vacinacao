package com.moisesvn.carteira_vacinacao_api.dto;

import com.moisesvn.carteira_vacinacao_api.model.StatusVacinal;

import java.time.LocalDate;

/**
 * DTO de resposta para representar um item do calendário vacinal personalizado.
 * 
 * Combina os dados do esquema vacinal (dose prevista) com o status dinâmico
 * calculado com base nos registros da pessoa:
 * - APLICADA: dose já registrada
 * - PENDENTE: dose não aplicada, dentro do prazo
 * - ATRASADA: dose não aplicada, fora do prazo
 */
public record CalendarioVacinalItemResponseDTO(
    Long vacinaId,
    String vacinaNome,
    Long esquemaVacinalId,
    String dose,
    Integer idadeRecomendadaMeses,
    LocalDate dataPrevista,
    StatusVacinal status,
    RegistroVacinaResponseDTO registro // null se status != APLICADA
) {
}
