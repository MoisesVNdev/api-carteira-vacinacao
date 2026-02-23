package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.CalendarioVacinalItemResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.RegistroVacinaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.RegistroVacinaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.service.RegistroVacinaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de registros de vacinação.
 * 
 * Todos os endpoints exigem autenticação JWT.
 * O usuario_id é extraído do token — nunca recebido no body.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RegistroVacinaController {

    private final RegistroVacinaService registroVacinaService;

    /**
     * Retorna o calendário vacinal personalizado de uma pessoa.
     * 
     * Este endpoint é o coração da aplicação. Cruza o catálogo completo
     * de esquemas vacinais com os registros da pessoa, calculando dinamicamente:
     * - Data prevista de cada dose
     * - Status (APLICADA, PENDENTE, ATRASADA)
     * 
     * Exemplo: GET /api/v1/pessoas/10/calendario
     *
     * @param pessoaId ID da pessoa
     * @return Calendário vacinal completo com status calculados
     */
    @GetMapping("/pessoas/{pessoaId}/calendario")
    public ResponseEntity<List<CalendarioVacinalItemResponseDTO>> gerarCalendario(
            @PathVariable Long pessoaId) {
        return ResponseEntity.ok(registroVacinaService.gerarCalendarioVacinal(pessoaId));
    }

    /**
     * Lista apenas as doses com status APLICADA (histórico/comprovante de vacinação).
     * 
     * Exemplo: GET /api/v1/pessoas/10/historico
     *
     * @param pessoaId ID da pessoa
     * @return Lista de registros de vacina (doses aplicadas)
     */
    @GetMapping("/pessoas/{pessoaId}/historico")
    public ResponseEntity<List<RegistroVacinaResponseDTO>> listarHistorico(
            @PathVariable Long pessoaId) {
        return ResponseEntity.ok(registroVacinaService.findHistoricoByPessoaId(pessoaId));
    }

    /**
     * Registra a aplicação de uma dose de vacina.
     * 
     * Validações automáticas:
     * - Pessoa pertence ao usuário autenticado (via JWT)
     * - Dose não está duplicada
     * - Dose anterior da mesma vacina já foi aplicada (hierarquia)
     * 
     * Exemplo: POST /api/v1/registros
     * Body: {
     *   "pessoaId": 10,
     *   "esquemaVacinalId": 5,
     *   "dataAplicacao": "2024-06-01",
     *   "lote": "LOT-2024-XYZ",
     *   "fabricante": "Fiocruz",
     *   "vacinador": "Dr. Carlos",
     *   "localAplicacao": "UBS Centro"
     * }
     *
     * @param dto Dados do registro de vacina
     * @return Registro criado com status 201
     */
    @PostMapping("/registros")
    public ResponseEntity<RegistroVacinaResponseDTO> registrar(
            @Valid @RequestBody RegistroVacinaRequestDTO dto) {
        RegistroVacinaResponseDTO created = registroVacinaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Remove um registro de vacinação (para correção de erro de digitação).
     * 
     * Valida se o registro pertence a uma pessoa vinculada ao usuário autenticado.
     * 
     * Exemplo: DELETE /api/v1/registros/15
     *
     * @param id ID do registro
     * @return 204 No Content
     */
    @DeleteMapping("/registros/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        registroVacinaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
