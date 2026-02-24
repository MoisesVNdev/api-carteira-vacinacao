package com.moisesvn.carteira_vacinacao_api.openapi;

import com.moisesvn.carteira_vacinacao_api.dto.UsuarioRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.UsuarioResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.UsuarioUpdateRequestDTO;
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
 * Documentação OpenAPI para gerenciamento de usuários.
 * Todos os endpoints exigem autenticação JWT.
 */
@Tag(name = "Usuários", description = "Gerenciamento de contas de usuário da plataforma.")
@SecurityRequirement(name = "bearerAuth")
public interface UsuarioApi {

    @Operation(
        summary = "Criar usuário",
        description = "Cadastra um novo usuário diretamente (fluxo administrativo). Para auto-cadastro, use /auth/register."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso.",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou incompletos.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "409", description = "E-mail já cadastrado no sistema.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO dto);

    @Operation(
        summary = "Listar usuários",
        description = "Retorna todos os usuários cadastrados na plataforma."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    ResponseEntity<List<UsuarioResponseDTO>> listarTodos();

    @Operation(
        summary = "Buscar usuário por ID",
        description = "Retorna os dados de um usuário específico."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário encontrado.",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    ResponseEntity<UsuarioResponseDTO> buscarPorId(
        @Parameter(description = "ID do usuário.", required = true, example = "1")
        @PathVariable Long id
    );

    @Operation(
        summary = "Atualizar usuário",
        description = "Atualiza os dados de um usuário existente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso.",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/{id}")
    ResponseEntity<UsuarioResponseDTO> atualizar(
        @Parameter(description = "ID do usuário.", required = true, example = "1")
        @PathVariable Long id,
        @Valid @RequestBody UsuarioUpdateRequestDTO dto
    );

    @Operation(
        summary = "Excluir usuário",
        description = "Remove permanentemente uma conta de usuário."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> excluir(
        @Parameter(description = "ID do usuário.", required = true, example = "1")
        @PathVariable Long id
    );
}
