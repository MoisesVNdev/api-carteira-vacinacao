package com.moisesvn.carteira_vacinacao_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO de resposta padronizada para erros da API.
 * Retornado automaticamente pelo GlobalExceptionHandler em caso de exceções.
 *
 * @param timestamp Data e hora do erro
 * @param status Código HTTP do erro
 * @param erro Nome da exceção que originou o erro
 * @param mensagem Mensagem descritiva do erro
 * @param caminho Caminho da requisição que gerou o erro
 */
@Schema(description = "Resposta padronizada de erro da API.")
public record ErrorResponseDTO(
    @Schema(description = "Data e hora em que o erro ocorreu.", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDateTime timestamp,
    
    @Schema(description = "Código HTTP do status.", requiredMode = Schema.RequiredMode.REQUIRED)
    int status,
    
    @Schema(description = "Nome da exceção.", requiredMode = Schema.RequiredMode.REQUIRED)
    String erro,
    
    @Schema(description = "Mensagem descritiva do erro.", requiredMode = Schema.RequiredMode.REQUIRED)
    String mensagem,
    
    @Schema(description = "Caminho da requisição que gerou o erro.", requiredMode = Schema.RequiredMode.REQUIRED)
    String caminho
) {
}
