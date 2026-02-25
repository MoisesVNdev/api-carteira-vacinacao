package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.response.AlergiaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.openapi.AlergiaApi;
import com.moisesvn.carteira_vacinacao_api.service.AlergiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST para gerenciamento do catálogo de alergias.
 * 
 * IMPORTANTE: Este controller expõe APENAS operações de leitura (GET).
 * O catálogo de alergias é gerenciado internamente, sem criação via API.
 */
@RestController
@RequestMapping("/api/v1/alergias")
@RequiredArgsConstructor
public class AlergiaController implements AlergiaApi {

    private final AlergiaService alergiaService;

    @Override
    public ResponseEntity<List<AlergiaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(alergiaService.findAll());
    }

    @Override
    public ResponseEntity<AlergiaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alergiaService.findById(id));
    }

    @Override
    public ResponseEntity<List<AlergiaResponseDTO>> buscarPorIds(
            @RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(alergiaService.findByIds(ids));
    }
}
