package com.moisesvn.carteira_vacinacao_api.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String nomeCompleto;
    private String email;
    private LocalDateTime dataCadastro;
}