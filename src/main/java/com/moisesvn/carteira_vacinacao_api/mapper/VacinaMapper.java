package com.moisesvn.carteira_vacinacao_api.mapper;

import com.moisesvn.carteira_vacinacao_api.dto.VacinaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.model.Vacina;

/**
 * Mapper para conversões entre `Vacina` e DTOs.
 */
public final class VacinaMapper {

    private VacinaMapper() {}

    public static VacinaResponseDTO toResponseDto(Vacina vacina) {
        if (vacina == null) return null;
        return new VacinaResponseDTO(
            vacina.getId(),
            vacina.getNome(),
            vacina.getDescricao(),
            vacina.getDoencaEvitada(),
            vacina.getCreatedAt(),
            vacina.getUpdatedAt()
        );
    }
}
