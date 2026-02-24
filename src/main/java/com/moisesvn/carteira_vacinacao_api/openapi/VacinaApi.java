package com.moisesvn.carteira_vacinacao_api.openapi;

import com.moisesvn.carteira_vacinacao_api.dto.EsquemaVacinalResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.VacinaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Documentação OpenAPI para catálogo de vacinas e esquemas vacinais.
 * Todos os endpoints exigem autenticação JWT.
 * Somente leitura — gerenciado internamente via Flyway.
 */
@Tag(name = "Vacinas", description = "Catálogo de vacinas e esquemas vacinais do PNI. Somente leitura — gerenciado via Flyway.")
@SecurityRequirement(name = "bearerAuth")
public interface VacinaApi {

    @Operation(
        summary = "Listar vacinas",
        description = "Retorna o catálogo completo de vacinas do Programa Nacional de Imunizações."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.",
            content = @Content(schema = @Schema(implementation = VacinaResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    ResponseEntity<List<VacinaResponseDTO>> listarTodas();

    @Operation(
        summary = "Buscar vacina por ID",
        description = "Retorna os dados de uma vacina específica."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vacina encontrada.",
            content = @Content(schema = @Schema(implementation = VacinaResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Vacina não encontrada.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    ResponseEntity<VacinaResponseDTO> buscarPorId(
        @Parameter(description = "ID da vacina.", required = true, example = "1")
        @PathVariable Long id
    );

    @Operation(
        summary = "Listar esquema vacinal",
        description = "Retorna todas as doses (esquema vacinal) de uma vacina específica. Ex: 1ª Dose, 2ª Dose, Reforço."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de esquemas retornada com sucesso.",
            content = @Content(schema = @Schema(implementation = EsquemaVacinalResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Vacina não encontrada.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}/esquema")
    ResponseEntity<List<EsquemaVacinalResponseDTO>> listarEsquemasVacinais(
        @Parameter(description = "ID da vacina.", required = true, example = "5")
        @PathVariable Long id
    );
}
