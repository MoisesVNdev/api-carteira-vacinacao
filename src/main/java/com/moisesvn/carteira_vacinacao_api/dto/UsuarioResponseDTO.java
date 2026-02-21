package com.moisesvn.carteira_vacinacao_api.dto;

import java.time.LocalDateTime;

/**
 * DTO de resposta com dados públicos do usuário.
 * Record imutável, nunca expõe a senha ou dados sensíveis.
 *
 * @param id ID único do usuário
 * @param nomeCompleto Nome completo do usuário
 * @param email Email do usuário
 * @param dataCadastro Data e hora do cadastro
 */
public record UsuarioResponseDTO(
    Long id,
    String nomeCompleto,
    String email,
    LocalDateTime dataCadastro
) {}