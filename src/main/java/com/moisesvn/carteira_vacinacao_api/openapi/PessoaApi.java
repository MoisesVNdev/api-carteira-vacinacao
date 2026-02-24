package com.moisesvn.carteira_vacinacao_api.openapi;

import com.moisesvn.carteira_vacinacao_api.dto.PessoaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.PessoaResponseDTO;
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
 * Documentação OpenAPI para gerenciamento de pessoas.
 * Todos os endpoints exigem autenticação JWT.
 */
@Tag(name = "Pessoas", description = "Gerenciamento de pessoas vinculadas à carteira de vacinação.")
@SecurityRequirement(name = "bearerAuth")
public interface PessoaApi {

    @Operation(
        summary = "Cadastrar pessoa",
        description = "Registra uma nova pessoa e retorna o header Location com a URL do recurso criado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pessoa cadastrada com sucesso.",
            content = @Content(schema = @Schema(implementation = PessoaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou incompletos.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    ResponseEntity<PessoaResponseDTO> create(@Valid @RequestBody PessoaRequestDTO dto);

    @Operation(
        summary = "Buscar pessoa por ID",
        description = "Retorna os dados de uma pessoa específica."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pessoa encontrada.",
            content = @Content(schema = @Schema(implementation = PessoaResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    ResponseEntity<PessoaResponseDTO> getById(
        @Parameter(description = "ID da pessoa.", required = true, example = "1")
        @PathVariable Long id
    );

    @Operation(
        summary = "Listar pessoas",
        description = "Retorna todas as pessoas cadastradas pelo usuário autenticado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.",
            content = @Content(schema = @Schema(implementation = PessoaResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    ResponseEntity<List<PessoaResponseDTO>> listAll();

    @Operation(
        summary = "Atualizar pessoa",
        description = "Atualiza os dados cadastrais de uma pessoa existente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso.",
            content = @Content(schema = @Schema(implementation = PessoaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/{id}")
    ResponseEntity<PessoaResponseDTO> update(
        @Parameter(description = "ID da pessoa.", required = true, example = "1")
        @PathVariable Long id,
        @Valid @RequestBody PessoaRequestDTO dto
    );

    @Operation(
        summary = "Remover pessoa",
        description = "Remove permanentemente uma pessoa e todos os seus registros de vacinação e alergias."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pessoa removida com sucesso.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
        @Parameter(description = "ID da pessoa.", required = true, example = "1")
        @PathVariable Long id
    );
}
