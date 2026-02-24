package com.moisesvn.carteira_vacinacao_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioUpdateRequestDTO {

    @NotBlank(message = "Nome completo é obrigatório")
    @Schema(description = "Nome completo do usuario.", example = "Maria da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nomeCompleto;
}
