package com.moisesvn.carteira_vacinacao_api.openapi;

import com.moisesvn.carteira_vacinacao_api.dto.AlergiaResponseDTO;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Documentação OpenAPI para catálogo de alergias.
 * Todos os endpoints exigem autenticação JWT.
 * Somente leitura — gerenciado internamente via Flyway.
 */
@Tag(name = "Alergias", description = "Catálogo de alergias disponíveis. Somente leitura — gerenciado internamente.")
@SecurityRequirement(name = "bearerAuth")
public interface AlergiaApi {

    @Operation(
        summary = "Listar alergias",
        description = "Retorna o catálogo completo de alergias disponíveis para vínculo."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.",
            content = @Content(schema = @Schema(implementation = AlergiaResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    ResponseEntity<List<AlergiaResponseDTO>> listarTodas();

    @Operation(
        summary = "Buscar alergia por ID",
        description = "Retorna os dados de uma alergia específica do catálogo."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alergia encontrada.",
            content = @Content(schema = @Schema(implementation = AlergiaResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Alergia não encontrada.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    ResponseEntity<AlergiaResponseDTO> buscarPorId(
        @Parameter(description = "ID da alergia.", required = true, example = "1")
        @PathVariable Long id
    );

    @Operation(
        summary = "Buscar alergias por lista de IDs",
        description = "Retorna múltiplas alergias em uma única requisição. Útil para pré-carregar seleções."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.",
            content = @Content(schema = @Schema(implementation = AlergiaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Parâmetro 'ids' inválido ou ausente.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/lote")
    ResponseEntity<List<AlergiaResponseDTO>> buscarPorIds(
        @Parameter(description = "Lista de IDs das alergias separados por vírgula.", required = true, example = "1,2,5")
        @RequestParam("ids") List<Long> ids
    );
}
