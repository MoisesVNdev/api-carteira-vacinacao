package com.moisesvn.carteira_vacinacao_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Nome completo da pessoa.", example = "Maria da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    String nomeCompleto,
    
    @NotNull(message = "Data de nascimento é obrigatória")
    @Schema(description = "Data de nascimento.", example = "2010-05-10", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate dataNascimento,
    
    @NotBlank(message = "CNS é obrigatório")
    @Schema(description = "Numero do CNS.", example = "898001160000000", requiredMode = Schema.RequiredMode.REQUIRED)
    String cns,
    
    @Schema(description = "CPF da pessoa.", example = "12345678901", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String cpf,
    
    @NotBlank(message = "Nome da mãe é obrigatório")
    @Schema(description = "Nome da mae.", example = "Ana Souza", requiredMode = Schema.RequiredMode.REQUIRED)
    String nomeMae,
    
    @Schema(description = "Genero da pessoa.", example = "FEMININO", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String genero,
    @Schema(description = "Nacionalidade.", example = "Brasileira", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String nacionalidade,
    @Schema(description = "Naturalidade.", example = "Sao Paulo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String naturalidade,
    @Schema(description = "Tipo sanguineo.", example = "O+", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String tipoSanguineo,
    @Schema(description = "URL ou caminho da foto.", example = "https://exemplo.com/foto.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String foto,
    
    @NotBlank(message = "Tipo de relação é obrigatório")
    @Schema(description = "Tipo de relacao com o usuario responsavel.", example = "MAE", requiredMode = Schema.RequiredMode.REQUIRED)
    String tipoRelacao
) {
}
