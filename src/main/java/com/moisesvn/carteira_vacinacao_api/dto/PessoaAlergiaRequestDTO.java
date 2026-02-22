package com.moisesvn.carteira_vacinacao_api.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO de requisição para vincular uma alergia a uma pessoa (POST simples).
 */
public record PessoaAlergiaRequestDTO(
    @NotNull(message = "ID da alergia é obrigatório")
    Long alergiaId,
    
    String observacao
) {
}
