package com.moisesvn.carteira_vacinacao_api.openapi;

import com.moisesvn.carteira_vacinacao_api.dto.response.ApiInfoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Documentação OpenAPI para informações gerais e status da API.
 * Endpoints públicos — nenhum token JWT necessário.
 */
@Tag(name = "Informações da API", description = "Endpoints públicos de status e informações gerais.")
public interface HomeApi {

    @Operation(
        summary = "Informações da API",
        description = "Retorna nome, versão e endpoints disponíveis da aplicação."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Informações retornadas com sucesso.",
            content = @Content(schema = @Schema(type = "object", example = """
                {
                  "nome": "API Carteira de Vacinação Digital",
                  "versao": "v0.0.1-SNAPSHOT",
                  "endpoints": "Endpoints disponíveis: /auth/register, /auth/login, /usuarios"
                }
                """)))
    })
    @GetMapping("/")
    ResponseEntity<ApiInfoResponseDTO> home();

}
