package com.moisesvn.carteira_vacinacao_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/**
 * DTO de requisição para registrar a aplicação de uma dose de vacina.
 * 
 * IMPORTANTE: O campo `pessoaId` vem da URL do endpoint, não do body.
 * O `usuario_id` é extraído do token JWT, nunca do body.
 */
public record RegistroVacinaRequestDTO(
    
    @NotNull(message = "ID da pessoa é obrigatório")
    Long pessoaId,
    
    @NotNull(message = "ID do esquema vacinal é obrigatório")
    Long esquemaVacinalId,
    
    @NotNull(message = "Data de aplicação é obrigatória")
    @PastOrPresent(message = "Data de aplicação não pode ser futura")
    LocalDate dataAplicacao,
    
    @NotBlank(message = "Lote da vacina é obrigatório")
    String lote,
    
    String fabricante,
    
    String vacinador,
    
    String localAplicacao
) {
}
