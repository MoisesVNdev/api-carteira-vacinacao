package com.moisesvn.carteira_vacinacao_api.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propriedades JWT mapeadas a partir de application.yml.
 */
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secret, long expiration) {
}
