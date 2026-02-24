package com.moisesvn.carteira_vacinacao_api.openapi;

import com.moisesvn.carteira_vacinacao_api.dto.CalendarioVacinalItemResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.RegistroVacinaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.RegistroVacinaResponseDTO;
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
 * Documentação OpenAPI para registros de vacinação e calendário vacinal.
 * Todos os endpoints exigem autenticação JWT.
 */
@Tag(name = "Registros de Vacinação", description = "Calendário vacinal personalizado, histórico e registro de doses aplicadas.")
@SecurityRequirement(name = "bearerAuth")
public interface RegistroVacinaApi {

    @Operation(
        summary = "Calendário vacinal da pessoa",
        description = "Gera o calendário vacinal completo e personalizado. Cruza o catálogo PNI com os registros da pessoa e calcula dinamicamente: data prevista de cada dose e status (APLICADA, PENDENTE ou ATRASADA)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Calendário gerado com sucesso.",
            content = @Content(schema = @Schema(implementation = CalendarioVacinalItemResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/pessoas/{pessoaId}/calendario")
    ResponseEntity<List<CalendarioVacinalItemResponseDTO>> gerarCalendario(
        @Parameter(description = "ID da pessoa.", required = true, example = "10")
        @PathVariable Long pessoaId
    );

    @Operation(
        summary = "Histórico de vacinação",
        description = "Retorna apenas as doses com status APLICADA. Funciona como comprovante ou carteirinha de vacinação digital."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso.",
            content = @Content(schema = @Schema(implementation = RegistroVacinaResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/pessoas/{pessoaId}/historico")
    ResponseEntity<List<RegistroVacinaResponseDTO>> listarHistorico(
        @Parameter(description = "ID da pessoa.", required = true, example = "10")
        @PathVariable Long pessoaId
    );

    @Operation(
        summary = "Registrar dose aplicada",
        description = "Registra a aplicação de uma dose de vacina. Valida automaticamente: se a pessoa pertence ao usuário autenticado, se a dose já foi registrada e se a dose anterior da mesma vacina já foi aplicada."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Dose registrada com sucesso.",
            content = @Content(schema = @Schema(implementation = RegistroVacinaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou validações falharam.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "409", description = "Dose já registrada ou hierarquia de doses violada.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/registros")
    ResponseEntity<RegistroVacinaResponseDTO> registrar(
        @Valid @RequestBody RegistroVacinaRequestDTO dto
    );

    @Operation(
        summary = "Remover registro de vacinação",
        description = "Remove um registro de vacinação (para correção de erro de digitação). Valida se o registro pertence a uma pessoa do usuário autenticado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Registro removido com sucesso.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Acesso negado — registro não pertence a uma pessoa do usuário.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado.",
            content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/registros/{id}")
    ResponseEntity<Void> deletar(
        @Parameter(description = "ID do registro de vacinação.", required = true, example = "15")
        @PathVariable Long id
    );
}
