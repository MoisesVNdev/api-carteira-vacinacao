package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.LoginRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.LoginResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.RegisterRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.UsuarioResponseDTO;
import com.moisesvn.carteira_vacinacao_api.openapi.AuthApi;
import com.moisesvn.carteira_vacinacao_api.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @Override
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registrar(request));
    }
}