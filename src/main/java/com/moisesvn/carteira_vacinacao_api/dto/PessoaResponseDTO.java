package com.moisesvn.carteira_vacinacao_api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de resposta para representar uma Pessoa na API.
 */
public record PessoaResponseDTO(
    Long id,
    String nomeCompleto,
    LocalDate dataNascimento,
    String cns,
    String cpf,
    String nomeMae,
    String genero,
    String nacionalidade,
    String naturalidade,
    String tipoSanguineo,
    String foto,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
