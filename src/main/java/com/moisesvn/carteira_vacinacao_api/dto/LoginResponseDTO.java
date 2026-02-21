package com.moisesvn.carteira_vacinacao_api.dto;

/**
 * DTO de resposta com token JWT e metadados.
 * Record imutável contendo o token para autenticação subsequente.
 *
 * @param token Token JWT para incluir no header Authorization
 * @param tipo Tipo de token (sempre "Bearer")
 * @param expiraEm Timestamp de expiração em milissegundos (epoch)
 */
public record LoginResponseDTO(
    String token,
    String tipo,
    long expiraEm
) {}