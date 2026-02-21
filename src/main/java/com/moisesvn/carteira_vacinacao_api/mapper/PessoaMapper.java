package com.moisesvn.carteira_vacinacao_api.mapper;

import com.moisesvn.carteira_vacinacao_api.dto.PessoaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.PessoaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.model.Pessoa;

/**
 * Mapper utilitário para conversões entre `Pessoa` e seus DTOs.
 */
public final class PessoaMapper {

    private PessoaMapper() {
    }

    public static Pessoa toEntity(PessoaRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Pessoa.builder()
            .nomeCompleto(dto.nomeCompleto())
            .dataNascimento(dto.dataNascimento())
            .cns(dto.cns())
            .cpf(dto.cpf())
            .nomeMae(dto.nomeMae())
            .genero(dto.genero())
            .nacionalidade(dto.nacionalidade())
            .naturalidade(dto.naturalidade())
            .tipoSanguineo(dto.tipoSanguineo())
            .foto(dto.foto())
            .build();
    }

    public static PessoaResponseDTO toResponseDto(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }

        return new PessoaResponseDTO(
            pessoa.getId(),
            pessoa.getNomeCompleto(),
            pessoa.getDataNascimento(),
            pessoa.getCns(),
            pessoa.getCpf(),
            pessoa.getNomeMae(),
            pessoa.getGenero(),
            pessoa.getNacionalidade(),
            pessoa.getNaturalidade(),
            pessoa.getTipoSanguineo(),
            pessoa.getFoto(),
            pessoa.getCreatedAt(),
            pessoa.getUpdatedAt()
        );
    }
}
