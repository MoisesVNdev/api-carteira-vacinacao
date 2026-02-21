package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.LoginRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.LoginResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.RegisterRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.UsuarioResponseDTO;
import com.moisesvn.carteira_vacinacao_api.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    /**
     * POST /auth/login
     * Endpoint público. Recebe e-mail e senha; retorna token JWT em caso de sucesso.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    /**
     * POST /auth/register
     * Endpoint público. Cria novo usuário e retorna seus dados (sem senha).
     * Aplica validações mais rígidas de senha via RegisterRequestDTO.
     */
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registrar(request));
    }
}