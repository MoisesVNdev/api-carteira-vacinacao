package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.ResponsavelResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.ResponsavelUpdateRequestDTO;
import com.moisesvn.carteira_vacinacao_api.service.ResponsavelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de vínculos entre usuários e pessoas.
 * 
 * IMPORTANTE: A criação de responsáveis é feita AUTOMATICAMENTE ao criar uma pessoa.
 * Este controller expõe apenas operações de consulta, atualização e exclusão.
 */
@RestController
@RequestMapping("/api/responsaveis")
@RequiredArgsConstructor
public class ResponsavelController {

    private final ResponsavelService responsavelService;

    /**
     * Lista todos os responsáveis vinculados a um usuário específico.
     * 
     * @param usuarioId ID do usuário
     * @return Lista de vínculos de responsável
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ResponsavelResponseDTO>> listByUsuario(@PathVariable Long usuarioId) {
        List<ResponsavelResponseDTO> lista = responsavelService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Busca um vínculo de responsável por ID.
     * 
     * @param id ID do responsável
     * @return Dados do vínculo
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponsavelResponseDTO> getById(@PathVariable Long id) {
        ResponsavelResponseDTO dto = responsavelService.findById(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    /**
     * Atualiza o tipo de relação de um vínculo existente.
     * 
     * @param id ID do responsável
     * @param dto Dados de atualização (tipoRelacao)
     * @return Dados atualizados do vínculo
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponsavelResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid ResponsavelUpdateRequestDTO dto) {
        ResponsavelResponseDTO atualizado = responsavelService.update(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    /**
     * Remove um vínculo de responsável (desvincula usuário de pessoa).
     * 
     * @param id ID do responsável
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        responsavelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
