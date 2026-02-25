package com.moisesvn.carteira_vacinacao_api.mapper;

import com.moisesvn.carteira_vacinacao_api.dto.response.ResponsavelResponseDTO;
import com.moisesvn.carteira_vacinacao_api.model.Responsavel;

/**
 * Mapper para conversões entre `Responsavel` e DTOs.
 */
public final class ResponsavelMapper {

    private ResponsavelMapper() {}

    public static ResponsavelResponseDTO toResponseDto(Responsavel r) {
        if (r == null) return null;
        Long usuarioId = r.getUsuario() != null ? r.getUsuario().getId() : null;
        Long pessoaId = r.getPessoa() != null ? r.getPessoa().getId() : null;
        return new ResponsavelResponseDTO(
            r.getId(),
            usuarioId,
            pessoaId,
            r.getTipoRelacao(),
            r.getDataCriacao()
        );
    }
}
