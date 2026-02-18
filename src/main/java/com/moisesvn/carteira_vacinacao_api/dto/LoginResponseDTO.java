package com.moisesvn.carteira_vacinacao_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String tipo;       // sempre "Bearer"
    private long expiraEm;     // expiração em ms (epoch)
}