package com.moisesvn.carteira_vacinacao_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioUpdateRequestDTO {

    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;
}
