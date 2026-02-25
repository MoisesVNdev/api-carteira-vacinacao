package com.moisesvn.carteira_vacinacao_api.openapi;

import com.moisesvn.carteira_vacinacao_api.dto.ErrorResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.request.PessoaAlergiaListRequestItem;
import com.moisesvn.carteira_vacinacao_api.dto.request.PessoaAlergiaObservacaoRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.request.PessoaAlergiaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.response.PessoaAlergiaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaAlergiaResponseDTO.class)))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        value = "{\"timestamp\":\"2026-02-24T10:30:00Z\",\"status\":401,\"erro\":\"InvalidTokenException\",\"mensagem\":\"Token inválido ou expirado\",\"caminho\":\"/api/pessoas/1/alergias\"}"
                    )
                }
            )),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        value = "{\"timestamp\":\"2026-02-24T10:30:00Z\",\"status\":404,\"erro\":\"PessoaNotFoundException\",\"mensagem\":\"Pessoa com ID 999 não encontrada\",\"caminho\":\"/api/pessoas/999/alergias\"}"
                    )
                }
            ))
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
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        value = "{\"timestamp\":\"2026-02-24T10:30:00Z\",\"status\":400,\"erro\":\"ValidationException\",\"mensagem\":\"Campo 'alergiaId' é obrigatório\",\"caminho\":\"/api/pessoas/1/alergias\"}"
                    )
                }
            )),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        value = "{\"timestamp\":\"2026-02-24T10:30:00Z\",\"status\":401,\"erro\":\"InvalidTokenException\",\"mensagem\":\"Token inválido ou expirado\",\"caminho\":\"/api/pessoas/1/alergias\"}"
                    )
                }
            )),
        @ApiResponse(responseCode = "409", description = "Alergia já vinculada à pessoa.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        value = "{\"timestamp\":\"2026-02-24T10:30:00Z\",\"status\":409,\"erro\":\"AlergiaJaVinculadaException\",\"mensagem\":\"Alergia com ID 5 já está vinculada a esta pessoa\",\"caminho\":\"/api/pessoas/1/alergias\"}"
                    )
                }
            ))
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
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PessoaAlergiaResponseDTO.class)))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        value = "{\"timestamp\":\"2026-02-24T10:30:00Z\",\"status\":400,\"erro\":\"ValidationException\",\"mensagem\":\"Lista de alergias vazia ou inválida\",\"caminho\":\"/api/pessoas/1/alergias/lote\"}"
                    )
                }
            )),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        value = "{\"timestamp\":\"2026-02-24T10:30:00Z\",\"status\":401,\"erro\":\"InvalidTokenException\",\"mensagem\":\"Token inválido ou expirado\",\"caminho\":\"/api/pessoas/1/alergias/lote\"}"
                    )
                }
            )),
        @ApiResponse(responseCode = "409", description = "Uma ou mais alergias já estão vinculadas.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        value = "{\"timestamp\":\"2026-02-24T10:30:00Z\",\"status\":409,\"erro\":\"AlergiaJaVinculadaException\",\"mensagem\":\"Uma ou mais alergias já estão vinculadas: [5, 7]\",\"caminho\":\"/api/pessoas/1/alergias/lote\"}"
                    )
                }
            ))
    })
    @PostMapping("/lote")
    ResponseEntity<List<PessoaAlergiaResponseDTO>> criarLote(
        @Parameter(description = "ID da pessoa.", required = true, example = "1")
        @PathVariable Long pessoaId,
        @Valid @RequestBody List<PessoaAlergiaListRequestItem> items
    );

    @Operation(
        summary = "Atualizar observação do vínculo",
        description = "Atualiza parcialmente apenas o campo de observação de um vínculo existente entre pessoa e alergia."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Observação atualizada com sucesso.",
            content = @Content(schema = @Schema(implementation = PessoaAlergiaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        value = "{\"timestamp\":\"2026-02-24T10:30:00Z\",\"status\":400,\"erro\":\"ValidationException\",\"mensagem\":\"Campo 'observacao' não pode exceder 500 caracteres\",\"caminho\":\"/api/pessoas/1/alergias/3/observacao\"}"
                    )
                }
            )),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        value = "{\"timestamp\":\"2026-02-24T10:30:00Z\",\"status\":401,\"erro\":\"InvalidTokenException\",\"mensagem\":\"Token inválido ou expirado\",\"caminho\":\"/api/pessoas/1/alergias/3/observacao\"}"
                    )
                }
            )),
        @ApiResponse(responseCode = "404", description = "Pessoa ou alergia não encontradas.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        value = "{\"timestamp\":\"2026-02-24T10:30:00Z\",\"status\":404,\"erro\":\"PessoaNotFoundException\",\"mensagem\":\"Pessoa com ID 999 não encontrada\",\"caminho\":\"/api/pessoas/999/alergias/3/observacao\"}"
                    )
                }
            ))
    })
    @PatchMapping("/{alergiaId}/observacao")
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
        @ApiResponse(responseCode = "204", description = "Observação removida com sucesso."),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        value = "{\"timestamp\":\"2026-02-24T10:30:00Z\",\"status\":401,\"erro\":\"InvalidTokenException\",\"mensagem\":\"Token inválido ou expirado\",\"caminho\":\"/api/pessoas/1/alergias/3/observacao\"}"
                    )
                }
            )),
        @ApiResponse(responseCode = "404", description = "Pessoa ou alergia não encontradas.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        value = "{\"timestamp\":\"2026-02-24T10:30:00Z\",\"status\":404,\"erro\":\"PessoaNotFoundException\",\"mensagem\":\"Pessoa com ID 999 não encontrada\",\"caminho\":\"/api/pessoas/999/alergias/3/observacao\"}"
                    )
                }
            ))
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
        @ApiResponse(responseCode = "204", description = "Vínculo removido com sucesso."),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        value = "{\"timestamp\":\"2026-02-24T10:30:00Z\",\"status\":401,\"erro\":\"InvalidTokenException\",\"mensagem\":\"Token inválido ou expirado\",\"caminho\":\"/api/pessoas/1/alergias/3\"}"
                    )
                }
            )),
        @ApiResponse(responseCode = "404", description = "Pessoa ou alergia não encontradas.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        value = "{\"timestamp\":\"2026-02-24T10:30:00Z\",\"status\":404,\"erro\":\"PessoaNotFoundException\",\"mensagem\":\"Pessoa com ID 999 não encontrada\",\"caminho\":\"/api/pessoas/999/alergias/3\"}"
                    )
                }
            ))
    })
    @DeleteMapping("/{alergiaId}")
    ResponseEntity<Void> deletar(
        @Parameter(description = "ID da pessoa.", required = true, example = "1")
        @PathVariable Long pessoaId,
        @Parameter(description = "ID da alergia.", required = true, example = "3")
        @PathVariable Long alergiaId
    );
}
