package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.PessoaAlergiaListRequestItem;
import com.moisesvn.carteira_vacinacao_api.dto.PessoaAlergiaObservacaoRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.PessoaAlergiaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.PessoaAlergiaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.openapi.PessoaAlergiaApi;
import com.moisesvn.carteira_vacinacao_api.service.PessoaAlergiaService;
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
 * Controller REST para gerenciamento de vínculos entre pessoas e alergias.
 * 
 * Todos os endpoints exigem autenticação JWT.
 * O usuario_id é extraído do token — nunca recebido no body.
 */
@RestController
@RequestMapping("/api/v1/pessoas/{pessoaId}/alergias")
@RequiredArgsConstructor
public class PessoaAlergiaController implements PessoaAlergiaApi {

    private final PessoaAlergiaService pessoaAlergiaService;

    @Override
    public ResponseEntity<List<PessoaAlergiaResponseDTO>> listar(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(pessoaAlergiaService.findByPessoa(pessoaId));
    }

    @Override
    public ResponseEntity<PessoaAlergiaResponseDTO> criar(
            @PathVariable Long pessoaId,
            @Valid @RequestBody PessoaAlergiaRequestDTO dto) {
        PessoaAlergiaResponseDTO created = pessoaAlergiaService.create(pessoaId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<List<PessoaAlergiaResponseDTO>> criarLote(
            @PathVariable Long pessoaId,
            @Valid @RequestBody List<PessoaAlergiaListRequestItem> items) {
        List<PessoaAlergiaResponseDTO> created = pessoaAlergiaService.createBatch(pessoaId, items);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<PessoaAlergiaResponseDTO> atualizarObservacao(
            @PathVariable Long pessoaId,
            @PathVariable Long alergiaId,
            @Valid @RequestBody PessoaAlergiaObservacaoRequestDTO dto) {
        PessoaAlergiaResponseDTO updated = pessoaAlergiaService.updateObservacao(pessoaId, alergiaId, dto);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> deletarObservacao(
            @PathVariable Long pessoaId,
            @PathVariable Long alergiaId) {
        pessoaAlergiaService.deleteObservacao(pessoaId, alergiaId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deletar(
            @PathVariable Long pessoaId,
            @PathVariable Long alergiaId) {
        pessoaAlergiaService.delete(pessoaId, alergiaId);
        return ResponseEntity.noContent().build();
    }
}
