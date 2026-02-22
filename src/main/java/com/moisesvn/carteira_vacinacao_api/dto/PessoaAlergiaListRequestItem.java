package com.moisesvn.carteira_vacinacao_api.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Item de alergia para o POST em lote (PessoaAlergia batch).
 * Mesma estrutura do PessoaAlergiaRequestDTO, mas usado dentro de um array.
 */
public record PessoaAlergiaListRequestItem(
    @NotNull(message = "ID da alergia é obrigatório")
    Long alergiaId,
    
    String observacao
) {
}
