package com.moisesvn.carteira_vacinacao_api.service;

import com.moisesvn.carteira_vacinacao_api.dto.VacinaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.exception.VacinaNotFoundException;
import com.moisesvn.carteira_vacinacao_api.mapper.VacinaMapper;
import com.moisesvn.carteira_vacinacao_api.repository.VacinaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar operações com a tabela `vacina`.
 * 
 * O catálogo de vacinas é gerenciado internamente via seed do Flyway
 * — não há criação, atualização ou exclusão via API pública.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VacinaService {

    private final VacinaRepository vacinaRepository;

    /**
     * Busca uma vacina por ID.
     *
     * @param id ID da vacina
     * @return DTO da vacina encontrada
     * @throws VacinaNotFoundException se a vacina não existe
     */
    @Transactional(readOnly = true)
    public VacinaResponseDTO findById(Long id) {
        log.debug("Buscando vacina por ID: {}", id);
        return vacinaRepository.findById(id)
                .map(VacinaMapper::toResponseDto)
                .orElseThrow(() -> new VacinaNotFoundException(id));
    }

    /**
     * Lista todas as vacinas cadastradas no catálogo.
     *
     * @return Lista de todas as vacinas
     */
    @Transactional(readOnly = true)
    public List<VacinaResponseDTO> findAll() {
        log.debug("Listando todas as vacinas do catálogo");
        return vacinaRepository.findAll().stream()
                .map(VacinaMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
