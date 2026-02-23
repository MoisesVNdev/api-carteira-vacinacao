package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.EsquemaVacinalResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.VacinaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.service.EsquemaVacinalService;
import com.moisesvn.carteira_vacinacao_api.service.VacinaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
public class VacinaController {

    private final VacinaService vacinaService;
    private final EsquemaVacinalService esquemaVacinalService;

    /**
     * Lista todas as vacinas cadastradas no catálogo do PNI.
     *
     * @return Lista completa de vacinas
     */
    @GetMapping
    public ResponseEntity<List<VacinaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(vacinaService.findAll());
    }

    /**
     * Busca uma vacina específica pelo ID.
     *
     * @param id ID da vacina
     * @return Dados da vacina encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<VacinaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vacinaService.findById(id));
    }

    /**
     * Lista todos os esquemas vacinais (doses) de uma vacina específica.
     * 
     * Exemplo: GET /api/v1/vacinas/5/esquema
     * Retorna: [{"id": 10, "descricaoDose": "1ª Dose", ...}, ...]
     *
     * @param id ID da vacina
     * @return Lista de esquemas vacinais (doses) da vacina
     */
    @GetMapping("/{id}/esquema")
    public ResponseEntity<List<EsquemaVacinalResponseDTO>> listarEsquemasVacinais(@PathVariable Long id) {
        return ResponseEntity.ok(esquemaVacinalService.findByVacinaId(id));
    }
}
