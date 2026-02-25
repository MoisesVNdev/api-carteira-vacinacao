package com.moisesvn.carteira_vacinacao_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "Nome completo é obrigatório")
    @Schema(description = "Nome completo do usuario.", example = "Maria da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nomeCompleto;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Schema(description = "E-mail do usuario.", example = "maria@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    /**
     * Mínimo 8 caracteres, ao menos: 1 maiúscula, 1 número, 1 caractere especial.
     * Validação aplicada apenas no registro; na atualização, o UsuarioRequestDTO
     * pode ter regras mais relaxadas conforme necessidade.
     */
    @NotBlank(message = "Senha é obrigatória")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
        message = "Senha deve ter no mínimo 8 caracteres, uma letra maiúscula, um número e um caractere especial"
    )
    @Schema(description = "Senha do usuario com regras de complexidade.", example = "Senha@123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String senha;
}
