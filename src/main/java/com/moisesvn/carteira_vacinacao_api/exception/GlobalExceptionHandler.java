package com.moisesvn.carteira_vacinacao_api.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleNaoEncontrado(
            UsuarioNaoEncontradoException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "UsuarioNaoEncontradoException", ex.getMessage(), request);
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Map<String, Object>> handleEmailDuplicado(
            EmailJaCadastradoException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "EmailJaCadastradoException", ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleCredenciaisInvalidas(
            InvalidCredentialsException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "InvalidCredentialsException", ex.getMessage(), request);
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handleTokenInvalido(
            TokenInvalidoException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "TokenInvalidoException", ex.getMessage(), request);
    }

    /**
     * Captura exceções de autenticação lançadas pelo próprio Spring Security
     * (ex: BadCredentialsException) que possam escapar do filtro JWT.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthException(
            AuthenticationException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "AuthenticationException", "Credenciais inválidas", request);
    }

    /**
     * Captura tentativas de acesso a recursos sem permissão suficiente.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "AccessDeniedException", "Acesso negado", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacao(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, "MethodArgumentNotValidException", mensagem, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenerico(
            Exception ex,
            HttpServletRequest request) {
        log.error("Erro não tratado na aplicação:", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getClass().getSimpleName(), "Erro interno do servidor", request);
    }

    /**
     * Constrói uma resposta padronizada de erro com todos os campos obrigatórios.
     *
     * @param status Status HTTP
     * @param erro Nome da exceção
     * @param mensagem Mensagem de erro descritiva
     * @param request Requisição HTTP (para extrair o caminho)
     * @return ResponseEntity com mapa contendo timestamp, status, erro, mensagem e caminho
     */
    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String erro,
            String mensagem,
            HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("erro", erro);
        body.put("mensagem", mensagem);
        body.put("caminho", request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}