package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.request.ResponsavelCreateRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.response.ResponsavelResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.request.ResponsavelUpdateRequestDTO;
import com.moisesvn.carteira_vacinacao_api.openapi.ResponsavelApi;
import com.moisesvn.carteira_vacinacao_api.service.ResponsavelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para gerenciamento de vínculos entre usuários e pessoas.
 * 
 * CRIAÇÃO: Pode ser feita de duas formas:
 * 1. AUTOMÁTICA: ao criar uma nova pessoa (automaticamente vinculada ao usuário autenticado)
 * 2. MANUAL: ao vincular uma pessoa já existente via endpoint POST /api/v1/responsaveis
 */
@RestController
@RequestMapping("/api/v1/responsaveis")
@RequiredArgsConstructor
public class ResponsavelController implements ResponsavelApi {

    private final ResponsavelService responsavelService;

    @Override
    public ResponseEntity<ResponsavelResponseDTO> create(@Valid @RequestBody ResponsavelCreateRequestDTO dto) {
        ResponsavelResponseDTO created = responsavelService.criarVinculoExistente(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.id())
            .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Override
    public ResponseEntity<List<ResponsavelResponseDTO>> listByUsuario(@PathVariable Long usuarioId) {
        List<ResponsavelResponseDTO> lista = responsavelService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(lista);
    }

    @Override
    public ResponseEntity<ResponsavelResponseDTO> getById(@PathVariable Long id) {
        ResponsavelResponseDTO dto = responsavelService.findById(id);
        return ResponseEntity.ok(dto);
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
