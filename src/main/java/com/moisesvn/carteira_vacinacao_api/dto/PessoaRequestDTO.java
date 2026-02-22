package com.moisesvn.carteira_vacinacao_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * DTO de requisição para criar/atualizar uma Pessoa.
 * O campo tipoRelacao é usado para criar automaticamente o vínculo
 * com o responsável (usuário autenticado).
 */
public record PessoaRequestDTO(
    @NotBlank(message = "Nome completo é obrigatório")
    String nomeCompleto,
    
    @NotNull(message = "Data de nascimento é obrigatória")
    LocalDate dataNascimento,
    
    @NotBlank(message = "CNS é obrigatório")
    String cns,
    
    String cpf,
    
    @NotBlank(message = "Nome da mãe é obrigatório")
    String nomeMae,
    
    String genero,
    String nacionalidade,
    String naturalidade,
    String tipoSanguineo,
    String foto,
    
    @NotBlank(message = "Tipo de relação é obrigatório")
    String tipoRelacao
) {
}
