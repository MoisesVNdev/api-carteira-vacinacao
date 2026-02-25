package com.moisesvn.carteira_vacinacao_api.openapi;

import com.moisesvn.carteira_vacinacao_api.dto.ErrorResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.request.LoginRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.response.LoginResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.request.RegisterRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.response.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Documentação OpenAPI para autenticação e registro de usuários.
 * Endpoints públicos — nenhum token JWT necessário.
 */
@Tag(name = "Autenticação", description = "Registro e login de usuários. Endpoints públicos, sem necessidade de token.")
public interface AuthApi {

    @Operation(
        summary = "Login",
        description = "Autentica o usuário com e-mail e senha. Retorna um token JWT para uso nos demais endpoints."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso.",
            content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "E-mail ou senha incorretos.",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2026-02-24T10:30:00",
                      "status": 401,
                      "erro": "InvalidCredentialsException",
                      "mensagem": "E-mail ou senha incorretos",
                      "caminho": "/auth/login"
                    }
                    """)))
    })
    @PostMapping("/login")
    ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request);

    @Operation(
        summary = "Registrar usuário",
        description = "Cria uma nova conta de usuário no sistema. Retorna os dados cadastrados sem a senha."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso.",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou incompletos.",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2026-02-24T10:30:00",
                      "status": 400,
                      "erro": "MethodArgumentNotValidException",
                      "mensagem": "email: Formato de e-mail inválido",
                      "caminho": "/auth/register"
                    }
                    """))),
        @ApiResponse(responseCode = "409", description = "E-mail já cadastrado no sistema.",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2026-02-24T10:30:00",
                      "status": 409,
                      "erro": "EmailJaCadastradoException",
                      "mensagem": "E-mail já cadastrado no sistema",
                      "caminho": "/auth/register"
                    }
                    """)))
    })
    @PostMapping("/register")
    ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody RegisterRequestDTO request);
}
