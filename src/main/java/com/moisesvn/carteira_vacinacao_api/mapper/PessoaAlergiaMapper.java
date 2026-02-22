package com.moisesvn.carteira_vacinacao_api.mapper;

import com.moisesvn.carteira_vacinacao_api.dto.PessoaAlergiaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.model.PessoaAlergia;

/**
 * Mapper para conversões entre `PessoaAlergia` e DTOs.
 */
public final class PessoaAlergiaMapper {

    private PessoaAlergiaMapper() {}

    public static PessoaAlergiaResponseDTO toResponseDto(PessoaAlergia pessoaAlergia) {
        if (pessoaAlergia == null) return null;
        
        Long pessoaId = pessoaAlergia.getPessoa() != null ? pessoaAlergia.getPessoa().getId() : null;
        Long alergiaId = pessoaAlergia.getAlergia() != null ? pessoaAlergia.getAlergia().getId() : null;
        String descricao = pessoaAlergia.getAlergia() != null ? pessoaAlergia.getAlergia().getDescricao() : null;
        
        return new PessoaAlergiaResponseDTO(
            pessoaId,
            alergiaId,
            descricao,
            pessoaAlergia.getObservacao(),
            pessoaAlergia.getCreatedAt()
        );
    }
}
