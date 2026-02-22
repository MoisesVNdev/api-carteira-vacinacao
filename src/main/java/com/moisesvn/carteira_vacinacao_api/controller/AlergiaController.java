package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.AlergiaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.service.AlergiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
public class AlergiaController {

    private final AlergiaService alergiaService;

    /**
     * Lista todas as alergias cadastradas no catálogo.
     *
     * @return Lista completa de alergias
     */
    @GetMapping
    public ResponseEntity<List<AlergiaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(alergiaService.findAll());
    }

    /**
     * Busca uma alergia específica pelo ID.
     *
     * @param id ID da alergia
     * @return Dados da alergia encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlergiaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alergiaService.findById(id));
    }

    /**
     * Busca múltiplas alergias pelos IDs informados via query param.
     *
     * @param ids Identificadores das alergias, separados por vírgula
     * @return Lista de alergias correspondentes aos IDs fornecidos
     */
    @GetMapping("/lote")
    public ResponseEntity<List<AlergiaResponseDTO>> buscarPorIds(
            @RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(alergiaService.findByIds(ids));
    }
}
