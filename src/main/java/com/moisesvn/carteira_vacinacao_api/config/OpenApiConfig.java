package com.moisesvn.carteira_vacinacao_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração OpenAPI (Swagger) para documentação automática da API.
 * 
 * Define:
 * - Metadados gerais da API (título, descrição, versão)
 * - Esquema de segurança JWT (Bearer Token)
 * - Disponível em /swagger-ui.html (desenvolvimento) e /api-docs (JSON)
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carteiraVacinacaoOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Carteira de Vacinação API")
                .description("""
                    API REST para gerenciamento de carteira de vacinação digital.
                    Permite cadastro de pessoas, responsáveis e registro de vacinas aplicadas.
                    Autenticação via JWT Bearer Token.
                    """)
                .version("v1.0.0")
                .contact(new Contact()
                    .name("Moises VN")
                    .url("https://github.com/MoisesVNdev")))
            .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("""
                        Token JWT obtido via POST /auth/login.
                        Insira apenas o token, sem o prefixo 'Bearer'.
                        """)));
    }
}
