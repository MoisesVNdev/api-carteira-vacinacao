package com.moisesvn.carteira_vacinacao_api.dto;

import java.time.LocalDate;

/**
 * DTO de requisição para criar/atualizar uma Pessoa.
 */
public record PessoaRequestDTO(
    String nomeCompleto,
    LocalDate dataNascimento,
    String cns,
    String cpf,
    String nomeMae,
    String genero,
    String nacionalidade,
    String naturalidade,
    String tipoSanguineo,
    String foto
) {
}
