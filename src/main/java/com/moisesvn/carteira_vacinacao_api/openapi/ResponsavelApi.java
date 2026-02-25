package com.moisesvn.carteira_vacinacao_api.openapi;

import com.moisesvn.carteira_vacinacao_api.dto.ErrorResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.response.ResponsavelResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.request.ResponsavelUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
 * Documentação OpenAPI para gerenciamento de responsáveis.
 * Todos os endpoints exigem autenticação JWT.
 * Nota: A criação de responsáveis é automática ao cadastrar uma pessoa.
 */
@Tag(name = "Responsáveis", description = "Gerenciamento de vínculos entre usuários e pessoas. Criação é automática ao cadastrar uma pessoa.")
@SecurityRequirement(name = "bearerAuth")
public interface ResponsavelApi {

    @Operation(
        summary = "Listar responsáveis por usuário",
        description = "Retorna todos os vínculos de responsável de um usuário específico."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsavelResponseDTO.class)))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(value = """
                    {
                      "timestamp": "2026-02-24T10:30:00",
                      "status": 401,
                      "erro": "TokenInvalidoException",
                      "mensagem": "Token JWT inválido ou expirado",
                      "caminho": "/api/responsaveis/usuario/1"
                    }
                    """)
                }))
    })
    @GetMapping("/usuario/{usuarioId}")
    ResponseEntity<List<ResponsavelResponseDTO>> listByUsuario(
        @Parameter(description = "ID do usuário.", required = true, example = "1")
        @PathVariable Long usuarioId
    );

    @Operation(
        summary = "Buscar responsável por ID",
        description = "Retorna os dados de um vínculo de responsável específico."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Responsável encontrado.",
            content = @Content(schema = @Schema(implementation = ResponsavelResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(value = """
                    {
                      "timestamp": "2026-02-24T10:30:00",
                      "status": 401,
                      "erro": "TokenInvalidoException",
                      "mensagem": "Token JWT inválido ou expirado",
                      "caminho": "/api/responsaveis/1"
                    }
                    """)
                })),
        @ApiResponse(responseCode = "404", description = "Responsável não encontrado.",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(value = """
                    {
                      "timestamp": "2026-02-24T10:30:00",
                      "status": 404,
                      "erro": "ResponsavelNaoEncontradoException",
                      "mensagem": "Responsável com ID 1 não encontrado",
                      "caminho": "/api/responsaveis/1"
                    }
                    """)
                }))
    })
    @GetMapping("/{id}")
    ResponseEntity<ResponsavelResponseDTO> getById(
        @Parameter(description = "ID do responsável.", required = true, example = "1")
        @PathVariable Long id
    );

    @Operation(
        summary = "Atualizar tipo de relação",
        description = "Atualiza parcialmente o tipo de relação (ex: PAI, MÃE, TUTOR) de um vínculo existente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vínculo atualizado com sucesso.",
            content = @Content(schema = @Schema(implementation = ResponsavelResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos.",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(value = """
                    {
                      "timestamp": "2026-02-24T10:30:00",
                      "status": 400,
                      "erro": "MethodArgumentNotValidException",
                      "mensagem": "tipoRelacao: Tipo de relação é obrigatório",
                      "caminho": "/api/responsaveis/1"
                    }
                    """)
                })),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(value = """
                    {
                      "timestamp": "2026-02-24T10:30:00",
                      "status": 401,
                      "erro": "TokenInvalidoException",
                      "mensagem": "Token JWT inválido ou expirado",
                      "caminho": "/api/responsaveis/1"
                    }
                    """)
                })),
        @ApiResponse(responseCode = "404", description = "Responsável não encontrado.",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(value = """
                    {
                      "timestamp": "2026-02-24T10:30:00",
                      "status": 404,
                      "erro": "ResponsavelNaoEncontradoException",
                      "mensagem": "Responsável com ID 1 não encontrado",
                      "caminho": "/api/responsaveis/1"
                    }
                    """)
                }))
    })
    @PatchMapping("/{id}")
    ResponseEntity<ResponsavelResponseDTO> update(
        @Parameter(description = "ID do responsável.", required = true, example = "1")
        @PathVariable Long id,
        @Valid @RequestBody ResponsavelUpdateRequestDTO dto
    );

    @Operation(
        summary = "Remover responsável",
        description = "Desvincula o usuário da pessoa, removendo o registro de responsável."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Responsável removido com sucesso."),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(value = """
                    {
                      "timestamp": "2026-02-24T10:30:00",
                      "status": 401,
                      "erro": "TokenInvalidoException",
                      "mensagem": "Token JWT inválido ou expirado",
                      "caminho": "/api/responsaveis/1"
                    }
                    """)
                })),
        @ApiResponse(responseCode = "404", description = "Responsável não encontrado.",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(value = """
                    {
                      "timestamp": "2026-02-24T10:30:00",
                      "status": 404,
                      "erro": "ResponsavelNaoEncontradoException",
                      "mensagem": "Responsável com ID 1 não encontrado",
                      "caminho": "/api/responsaveis/1"
                    }
                    """)
                }))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
        @Parameter(description = "ID do responsável.", required = true, example = "1")
        @PathVariable Long id
    );
}
