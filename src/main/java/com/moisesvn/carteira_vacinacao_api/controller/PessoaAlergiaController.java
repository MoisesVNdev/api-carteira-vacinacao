package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.PessoaAlergiaListRequestItem;
import com.moisesvn.carteira_vacinacao_api.dto.PessoaAlergiaObservacaoRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.PessoaAlergiaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.PessoaAlergiaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.service.PessoaAlergiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class PessoaAlergiaController {

    private final PessoaAlergiaService pessoaAlergiaService;

    /**
     * Lista todas as alergias vinculadas à pessoa.
     *
     * @param pessoaId ID da pessoa
     * @return Lista de alergias vinculadas
     */
    @GetMapping
    public ResponseEntity<List<PessoaAlergiaResponseDTO>> listar(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(pessoaAlergiaService.findByPessoa(pessoaId));
    }

    /**
     * Vincula uma alergia à pessoa (POST simples).
     *
     * @param pessoaId ID da pessoa
     * @param dto Dados da alergia e observação
     * @return Vínculo criado com status 201
     */
    @PostMapping
    public ResponseEntity<PessoaAlergiaResponseDTO> criar(
            @PathVariable Long pessoaId,
            @Valid @RequestBody PessoaAlergiaRequestDTO dto) {
        PessoaAlergiaResponseDTO created = pessoaAlergiaService.create(pessoaId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Vincula múltiplas alergias à pessoa em uma única requisição (POST em lote).
     * 
     * Se qualquer alergia já estiver vinculada, a operação falha completamente (atômica).
     *
     * @param pessoaId ID da pessoa
     * @param items Lista de alergias a vincular
     * @return Lista de vínculos criados com status 201
     */
    @PostMapping("/lote")
    public ResponseEntity<List<PessoaAlergiaResponseDTO>> criarLote(
            @PathVariable Long pessoaId,
            @Valid @RequestBody List<PessoaAlergiaListRequestItem> items) {
        List<PessoaAlergiaResponseDTO> created = pessoaAlergiaService.createBatch(pessoaId, items);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Atualiza apenas o campo observacao do vínculo (PUT).
     *
     * @param pessoaId ID da pessoa
     * @param alergiaId ID da alergia
     * @param dto Novo valor da observação
     * @return Vínculo atualizado
     */
    @PutMapping("/{alergiaId}/observacao")
    public ResponseEntity<PessoaAlergiaResponseDTO> atualizarObservacao(
            @PathVariable Long pessoaId,
            @PathVariable Long alergiaId,
            @Valid @RequestBody PessoaAlergiaObservacaoRequestDTO dto) {
        PessoaAlergiaResponseDTO updated = pessoaAlergiaService.updateObservacao(pessoaId, alergiaId, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Remove (limpa) a observacao do vínculo, mantendo-o ativo (DELETE observacao).
     *
     * @param pessoaId ID da pessoa
     * @param alergiaId ID da alergia
     * @return 204 No Content
     */
    @DeleteMapping("/{alergiaId}/observacao")
    public ResponseEntity<Void> deletarObservacao(
            @PathVariable Long pessoaId,
            @PathVariable Long alergiaId) {
        pessoaAlergiaService.deleteObservacao(pessoaId, alergiaId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Remove completamente o vínculo entre pessoa e alergia (DELETE vínculo).
     *
     * @param pessoaId ID da pessoa
     * @param alergiaId ID da alergia
     * @return 204 No Content
     */
    @DeleteMapping("/{alergiaId}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long pessoaId,
            @PathVariable Long alergiaId) {
        pessoaAlergiaService.delete(pessoaId, alergiaId);
        return ResponseEntity.noContent().build();
    }
}
