package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.ResponsavelRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.ResponsavelResponseDTO;
import com.moisesvn.carteira_vacinacao_api.service.ResponsavelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/responsaveis")
@RequiredArgsConstructor
public class ResponsavelController {

    private final ResponsavelService responsavelService;

    @PostMapping
    public ResponseEntity<ResponsavelResponseDTO> create(@RequestBody @Valid ResponsavelRequestDTO dto) {
        ResponsavelResponseDTO criado = responsavelService.create(dto);
        return ResponseEntity.created(URI.create("/api/responsaveis/" + criado.id())).body(criado);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ResponsavelResponseDTO>> listByUsuario(@PathVariable Long usuarioId) {
        List<ResponsavelResponseDTO> lista = responsavelService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsavelResponseDTO> getById(@PathVariable Long id) {
        ResponsavelResponseDTO dto = responsavelService.findById(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        responsavelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
