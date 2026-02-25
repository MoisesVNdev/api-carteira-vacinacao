package com.moisesvn.carteira_vacinacao_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de resposta para representar uma Pessoa na API.
 */
public record PessoaResponseDTO(
    @Schema(description = "ID da pessoa.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,
    @Schema(description = "Nome completo da pessoa.", example = "Joao da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    String nomeCompleto,
    @Schema(description = "Data de nascimento.", example = "2010-05-10", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate dataNascimento,
    @Schema(description = "Numero do CNS.", example = "898001160000000", requiredMode = Schema.RequiredMode.REQUIRED)
    String cns,
    @Schema(description = "CPF da pessoa.", example = "12345678901", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String cpf,
    @Schema(description = "Nome da mae.", example = "Maria da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
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
    @Schema(description = "Data e hora de criacao do registro.", example = "2026-02-23T22:28:06", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime createdAt,
    @Schema(description = "Data e hora da ultima atualizacao.", example = "2026-02-23T22:28:06", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    LocalDateTime updatedAt
) {
}
