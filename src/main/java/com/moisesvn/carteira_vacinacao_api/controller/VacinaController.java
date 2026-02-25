package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.response.EsquemaVacinalResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.response.VacinaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.openapi.VacinaApi;
import com.moisesvn.carteira_vacinacao_api.service.EsquemaVacinalService;
import com.moisesvn.carteira_vacinacao_api.service.VacinaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST para gerenciamento do catálogo de vacinas e esquemas vacinais.
 * 
 * IMPORTANTE: Este controller expõe APENAS operações de leitura (GET).
 * O catálogo de vacinas e esquemas vacinais é gerenciado internamente via seed do Flyway.
 */
@RestController
@RequestMapping("/api/v1/vacinas")
@RequiredArgsConstructor
public class VacinaController implements VacinaApi {

    private final VacinaService vacinaService;
    private final EsquemaVacinalService esquemaVacinalService;

    @Override
    public ResponseEntity<List<VacinaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(vacinaService.findAll());
    }

    @Override
    public ResponseEntity<VacinaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vacinaService.findById(id));
    }

    @Override
    public ResponseEntity<List<EsquemaVacinalResponseDTO>> listarEsquemasVacinais(@PathVariable Long id) {
        return ResponseEntity.ok(esquemaVacinalService.findByVacinaId(id));
    }
}
