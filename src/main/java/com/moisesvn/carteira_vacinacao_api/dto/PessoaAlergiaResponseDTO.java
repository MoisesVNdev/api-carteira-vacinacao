package com.moisesvn.carteira_vacinacao_api.dto;

import java.time.LocalDateTime;

/**
 * DTO de resposta para representar um vínculo PessoaAlergia.
 * Inclui a descrição da alergia para conveniência do cliente.
 */
public record PessoaAlergiaResponseDTO(
    Long pessoaId,
    Long alergiaId,
    String descricao,
    String observacao,
    LocalDateTime createdAt
) {
}
