package com.moisesvn.carteira_vacinacao_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok(new ApiInfoResponse(
            "API Carteira de Vacinação Digital",
            "v0.0.1-SNAPSHOT",
            "Endpoints disponíveis: /auth/register, /auth/login, /usuarios"
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(new HealthResponse("UP", "API está funcionando corretamente"));
    }

    // DTOs internos para resposta
    record ApiInfoResponse(String nome, String versao, String endpoints) {}
    record HealthResponse(String status, String mensagem) {}
}
