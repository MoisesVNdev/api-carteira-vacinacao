package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.ResponsavelResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.ResponsavelUpdateRequestDTO;
import com.moisesvn.carteira_vacinacao_api.openapi.ResponsavelApi;
import com.moisesvn.carteira_vacinacao_api.service.ResponsavelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class ResponsavelController implements ResponsavelApi {

    private final ResponsavelService responsavelService;

    @Override
    public ResponseEntity<List<ResponsavelResponseDTO>> listByUsuario(@PathVariable Long usuarioId) {
        List<ResponsavelResponseDTO> lista = responsavelService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(lista);
    }

    @Override
    public ResponseEntity<ResponsavelResponseDTO> getById(@PathVariable Long id) {
        ResponsavelResponseDTO dto = responsavelService.findById(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<ResponsavelResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid ResponsavelUpdateRequestDTO dto) {
        ResponsavelResponseDTO atualizado = responsavelService.update(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        responsavelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
