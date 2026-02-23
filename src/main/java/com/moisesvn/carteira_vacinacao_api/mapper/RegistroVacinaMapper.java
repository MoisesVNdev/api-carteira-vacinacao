package com.moisesvn.carteira_vacinacao_api.mapper;

import com.moisesvn.carteira_vacinacao_api.dto.RegistroVacinaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.model.RegistroVacina;

/**
 * Mapper para conversões entre `RegistroVacina` e DTOs.
 */
public final class RegistroVacinaMapper {

    private RegistroVacinaMapper() {}

    public static RegistroVacinaResponseDTO toResponseDto(RegistroVacina registro) {
        if (registro == null) return null;
        
        String vacinaNome = null;
        String dose = null;
        
        if (registro.getEsquemaVacinal() != null) {
            dose = registro.getEsquemaVacinal().getDescricaoDose();
            if (registro.getEsquemaVacinal().getVacina() != null) {
                vacinaNome = registro.getEsquemaVacinal().getVacina().getNome();
            }
        }
        
        return new RegistroVacinaResponseDTO(
            registro.getId(),
            vacinaNome,
            dose,
            registro.getDataAplicacao(),
            registro.getLote(),
            registro.getFabricante(),
            registro.getVacinador(),
            registro.getLocalAplicacao(),
            registro.getCreatedAt()
        );
    }
}
