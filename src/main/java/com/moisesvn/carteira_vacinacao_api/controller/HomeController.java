package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.response.ApiInfoResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.response.HealthResponseDTO;
import com.moisesvn.carteira_vacinacao_api.openapi.HomeApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class HomeController implements HomeApi {

    @Override
    public ResponseEntity<ApiInfoResponseDTO> home() {
        return ResponseEntity.ok(new ApiInfoResponseDTO(
            "API Carteira de Vacinação Digital",
            "v0.0.1-SNAPSHOT",
            "Endpoints disponíveis: /api/v1/auth/register, /api/v1/auth/login, /api/v1/usuarios, /api/v1/pessoas, /api/v1/alergias, /api/v1/vacinas"
        ));
    }

    @Override
    public ResponseEntity<HealthResponseDTO> health() {
        return ResponseEntity.ok(new HealthResponseDTO("UP", "API está funcionando corretamente"));
    }
}
