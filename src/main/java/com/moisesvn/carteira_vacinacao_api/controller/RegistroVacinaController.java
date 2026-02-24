package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.CalendarioVacinalItemResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.RegistroVacinaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.RegistroVacinaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.openapi.RegistroVacinaApi;
import com.moisesvn.carteira_vacinacao_api.service.RegistroVacinaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class RegistroVacinaController implements RegistroVacinaApi {

    private final RegistroVacinaService registroVacinaService;

    @Override
    public ResponseEntity<List<CalendarioVacinalItemResponseDTO>> gerarCalendario(
            @PathVariable Long pessoaId) {
        return ResponseEntity.ok(registroVacinaService.gerarCalendarioVacinal(pessoaId));
    }

    @Override
    public ResponseEntity<List<RegistroVacinaResponseDTO>> listarHistorico(
            @PathVariable Long pessoaId) {
        return ResponseEntity.ok(registroVacinaService.findHistoricoByPessoaId(pessoaId));
    }

    @Override
    public ResponseEntity<RegistroVacinaResponseDTO> registrar(
            @Valid @RequestBody RegistroVacinaRequestDTO dto) {
        RegistroVacinaResponseDTO created = registroVacinaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        registroVacinaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
