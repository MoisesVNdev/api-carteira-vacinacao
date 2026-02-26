package com.moisesvn.carteira_vacinacao_api.controller;

import com.moisesvn.carteira_vacinacao_api.dto.request.PessoaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.response.PessoaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.openapi.PessoaApi;
import com.moisesvn.carteira_vacinacao_api.service.PessoaService;
import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/pessoas")
@RequiredArgsConstructor
public class PessoaController implements PessoaApi {

    private final PessoaService pessoaService;

    @Override
    public ResponseEntity<PessoaResponseDTO> create(@Valid @RequestBody PessoaRequestDTO dto) {
        PessoaResponseDTO created = pessoaService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.id())
            .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Override
    public ResponseEntity<PessoaResponseDTO> getById(@PathVariable Long id) {
        PessoaResponseDTO dto = pessoaService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<List<PessoaResponseDTO>> listAll() {
        return ResponseEntity.ok(pessoaService.findAll());
    }

    @Override
    public ResponseEntity<PessoaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody PessoaRequestDTO dto) {
        PessoaResponseDTO updated = pessoaService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pessoaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PessoaResponseDTO> findByCpfAndCns(String cpf, String cns) {
        PessoaResponseDTO dto = pessoaService.findByCpfAndCns(cpf, cns);
        return ResponseEntity.ok(dto);
    }
}
