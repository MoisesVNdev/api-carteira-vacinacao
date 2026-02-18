package com.moisesvn.carteira_vacinacao_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
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
    private String senha;
}