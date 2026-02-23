package com.moisesvn.carteira_vacinacao_api.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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

    @ExceptionHandler(PessoaNaoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> handlePessoaNaoEncontrada(
            PessoaNaoEncontradaException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "PessoaNaoEncontradaException", ex.getMessage(), request);
    }

    @ExceptionHandler(ResponsavelNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleResponsavelNaoEncontrado(
            ResponsavelNaoEncontradoException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "ResponsavelNaoEncontradoException", ex.getMessage(), request);
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Map<String, Object>> handleEmailDuplicado(
            EmailJaCadastradoException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "EmailJaCadastradoException", ex.getMessage(), request);
    }

    @ExceptionHandler(CnsJaCadastradoException.class)
    public ResponseEntity<Map<String, Object>> handleCnsDuplicado(
            CnsJaCadastradoException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "CnsJaCadastradoException", ex.getMessage(), request);
    }

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ResponseEntity<Map<String, Object>> handleCpfDuplicado(
            CpfJaCadastradoException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "CpfJaCadastradoException", ex.getMessage(), request);
    }

    @ExceptionHandler(ResponsavelJaCadastradoException.class)
    public ResponseEntity<Map<String, Object>> handleResponsavelDuplicado(
            ResponsavelJaCadastradoException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "ResponsavelJaCadastradoException", ex.getMessage(), request);
    }

    @ExceptionHandler(AlergiaNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAlergiaNaoEncontrada(
            AlergiaNotFoundException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "AlergiaNotFoundException", ex.getMessage(), request);
    }

    @ExceptionHandler(AlergiaJaCadastradaException.class)
    public ResponseEntity<Map<String, Object>> handleAlergiaJaCadastrada(
            AlergiaJaCadastradaException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "AlergiaJaCadastradaException", ex.getMessage(), request);
    }

    @ExceptionHandler(PessoaAlergiaNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePessoaAlergiaNaoEncontrada(
            PessoaAlergiaNotFoundException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "PessoaAlergiaNotFoundException", ex.getMessage(), request);
    }

    @ExceptionHandler(PessoaAlergiaJaCadastradoException.class)
    public ResponseEntity<Map<String, Object>> handlePessoaAlergiaJaCadastrada(
            PessoaAlergiaJaCadastradoException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "PessoaAlergiaJaCadastradoException", ex.getMessage(), request);
    }

    @ExceptionHandler(VacinaNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleVacinaNaoEncontrada(
            VacinaNotFoundException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "VacinaNotFoundException", ex.getMessage(), request);
    }

    @ExceptionHandler(EsquemaVacinalNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEsquemaVacinalNaoEncontrado(
            EsquemaVacinalNotFoundException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "EsquemaVacinalNotFoundException", ex.getMessage(), request);
    }

    @ExceptionHandler(RegistroVacinaNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRegistroVacinaNaoEncontrado(
            RegistroVacinaNotFoundException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "RegistroVacinaNotFoundException", ex.getMessage(), request);
    }

    @ExceptionHandler(RegistroVacinaDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleRegistroVacinaDuplicado(
            RegistroVacinaDuplicadoException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "RegistroVacinaDuplicadoException", ex.getMessage(), request);
    }

    @ExceptionHandler(DoseAnteriorNaoAplicadaException.class)
    public ResponseEntity<Map<String, Object>> handleDoseAnteriorNaoAplicada(
            DoseAnteriorNaoAplicadaException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.valueOf(422), "DoseAnteriorNaoAplicadaException", ex.getMessage(), request);
    }

    @ExceptionHandler(PessoaNaoPertenceAoUsuarioException.class)
    public ResponseEntity<Map<String, Object>> handlePessoaNaoPertenceAoUsuario(
            PessoaNaoPertenceAoUsuarioException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "PessoaNaoPertenceAoUsuarioException", ex.getMessage(), request);
    }

    /**
     * Trata violações de integridade do banco de dados (constraints).
     * Identifica constraints de CNS/CPF únicos para fornecer mensagens mais amigáveis.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        String mensagem = "Erro de integridade de dados";
        
        String exMessage = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
        
        // Detectar constraint unique de CNS
        if (exMessage.contains("cns") || exMessage.contains("uk_pessoa_cns")) {
            mensagem = "CNS já cadastrado no sistema";
        } 
        // Detectar constraint unique de CPF
        else if (exMessage.contains("cpf") || exMessage.contains("uk_pessoa_cpf")) {
            mensagem = "CPF já cadastrado no sistema";
        }
        // Detectar constraint unique de Email
        else if (exMessage.contains("email") || exMessage.contains("uk_usuario_email")) {
            mensagem = "E-mail já cadastrado no sistema";
        }
        
        log.warn("Violação de integridade de dados: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, "DataIntegrityViolationException", mensagem, request);
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

    /**
     * Retorna 404 quando a URL nao corresponde a nenhum endpoint registrado.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(
            NoResourceFoundException ex,
            HttpServletRequest request) {
        log.warn("Recurso nao encontrado: {}", request.getRequestURI());
        return buildResponse(HttpStatus.NOT_FOUND, "NoResourceFoundException", "Recurso nao encontrado", request);
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