package com.moisesvn.carteira_vacinacao_api.mapper;

import com.moisesvn.carteira_vacinacao_api.dto.response.AlergiaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.model.Alergia;

/**
 * Mapper para conversões entre `Alergia` e DTOs.
 */
public final class AlergiaMapper {

    private AlergiaMapper() {}

    public static AlergiaResponseDTO toResponseDto(Alergia alergia) {
        if (alergia == null) return null;
        return new AlergiaResponseDTO(
            alergia.getId(),
            alergia.getDescricao(),
            alergia.getCreatedAt()
        );
    }
}
