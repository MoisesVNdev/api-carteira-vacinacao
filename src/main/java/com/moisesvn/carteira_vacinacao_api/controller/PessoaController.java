package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.PessoaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.PessoaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.service.PessoaService;
import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/pessoas")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService pessoaService;

    @PostMapping
    public ResponseEntity<PessoaResponseDTO> create(@Valid @RequestBody PessoaRequestDTO dto) {
        PessoaResponseDTO created = pessoaService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.id())
            .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> getById(@PathVariable Long id) {
        PessoaResponseDTO dto = pessoaService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<PessoaResponseDTO>> listAll() {
        return ResponseEntity.ok(pessoaService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody PessoaRequestDTO dto) {
        PessoaResponseDTO updated = pessoaService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pessoaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
