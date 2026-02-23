package com.moisesvn.carteira_vacinacao_api.mapper;

import com.moisesvn.carteira_vacinacao_api.dto.EsquemaVacinalResponseDTO;
import com.moisesvn.carteira_vacinacao_api.model.EsquemaVacinal;

/**
 * Mapper para conversões entre `EsquemaVacinal` e DTOs.
 */
public final class EsquemaVacinalMapper {

    private EsquemaVacinalMapper() {}

    public static EsquemaVacinalResponseDTO toResponseDto(EsquemaVacinal esquema) {
        if (esquema == null) return null;
        
        Long vacinaId = esquema.getVacina() != null ? esquema.getVacina().getId() : null;
        String vacinaNome = esquema.getVacina() != null ? esquema.getVacina().getNome() : null;
        
        return new EsquemaVacinalResponseDTO(
            esquema.getId(),
            vacinaId,
            vacinaNome,
            esquema.getDescricaoDose(),
            esquema.getIdadeRecomendadaMeses(),
            esquema.getIntervaloMinimoDias()
        );
    }
}
