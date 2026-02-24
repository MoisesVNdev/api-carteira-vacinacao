package com.moisesvn.carteira_vacinacao_api.openapi;

import com.moisesvn.carteira_vacinacao_api.dto.PessoaAlergiaListRequestItem;
import com.moisesvn.carteira_vacinacao_api.dto.PessoaAlergiaObservacaoRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.PessoaAlergiaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.PessoaAlergiaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Documentação OpenAPI para gerenciamento de vínculos entre pessoas e alergias.
 * Todos os endpoints exigem autenticação JWT.
 */
@Tag(name = "Alergias por Pessoa", description = "Gerenciamento de vínculos entre pessoas e alergias.")
@SecurityRequirement(name = "bearerAuth")
public interface PessoaAlergiaApi {

    @Operation(
        summary = "Listar alergias da pessoa",
        description = "Retorna todas as alergias vinculadas a uma pessoa específica."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.",
            content = @Content(schema = @Schema(implementation = PessoaAlergiaResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    ResponseEntity<List<PessoaAlergiaResponseDTO>> listar(
        @Parameter(description = "ID da pessoa.", required = true, example = "1")
        @PathVariable Long pessoaId
    );

    @Operation(
        summary = "Vincular alergia à pessoa",
        description = "Cria um vínculo entre a pessoa e uma alergia do catálogo, com observação opcional."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Alergia vinculada com sucesso.",
            content = @Content(schema = @Schema(implementation = PessoaAlergiaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "409", description = "Alergia já vinculada à pessoa.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    ResponseEntity<PessoaAlergiaResponseDTO> criar(
        @Parameter(description = "ID da pessoa.", required = true, example = "1")
        @PathVariable Long pessoaId,
        @Valid @RequestBody PessoaAlergiaRequestDTO dto
    );

    @Operation(
        summary = "Vincular múltiplas alergias (lote)",
        description = "Vincula várias alergias à pessoa em uma única operação atômica. Se qualquer alergia já estiver vinculada, toda a operação falha."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Alergias vinculadas com sucesso.",
            content = @Content(schema = @Schema(implementation = PessoaAlergiaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "409", description = "Uma ou mais alergias já estão vinculadas.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/lote")
    ResponseEntity<List<PessoaAlergiaResponseDTO>> criarLote(
        @Parameter(description = "ID da pessoa.", required = true, example = "1")
        @PathVariable Long pessoaId,
        @Valid @RequestBody List<PessoaAlergiaListRequestItem> items
    );

    @Operation(
        summary = "Atualizar observação do vínculo",
        description = "Atualiza apenas o campo de observação de um vínculo existente entre pessoa e alergia."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Observação atualizada com sucesso.",
            content = @Content(schema = @Schema(implementation = PessoaAlergiaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Pessoa ou alergia não encontradas.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/{alergiaId}/observacao")
    ResponseEntity<PessoaAlergiaResponseDTO> atualizarObservacao(
        @Parameter(description = "ID da pessoa.", required = true, example = "1")
        @PathVariable Long pessoaId,
        @Parameter(description = "ID da alergia.", required = true, example = "3")
        @PathVariable Long alergiaId,
        @Valid @RequestBody PessoaAlergiaObservacaoRequestDTO dto
    );

    @Operation(
        summary = "Remover observação do vínculo",
        description = "Limpa o campo de observação, mantendo o vínculo entre pessoa e alergia ativo."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Observação removida com sucesso.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Pessoa ou alergia não encontradas.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{alergiaId}/observacao")
    ResponseEntity<Void> deletarObservacao(
        @Parameter(description = "ID da pessoa.", required = true, example = "1")
        @PathVariable Long pessoaId,
        @Parameter(description = "ID da alergia.", required = true, example = "3")
        @PathVariable Long alergiaId
    );

    @Operation(
        summary = "Remover vínculo de alergia",
        description = "Remove completamente o vínculo entre a pessoa e a alergia."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Vínculo removido com sucesso.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Pessoa ou alergia não encontradas.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{alergiaId}")
    ResponseEntity<Void> deletar(
        @Parameter(description = "ID da pessoa.", required = true, example = "1")
        @PathVariable Long pessoaId,
        @Parameter(description = "ID da alergia.", required = true, example = "3")
        @PathVariable Long alergiaId
    );
}
