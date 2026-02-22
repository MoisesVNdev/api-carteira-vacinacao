package com.moisesvn.carteira_vacinacao_api.service;

import com.moisesvn.carteira_vacinacao_api.dto.AlergiaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.exception.AlergiaNotFoundException;
import com.moisesvn.carteira_vacinacao_api.mapper.AlergiaMapper;
import com.moisesvn.carteira_vacinacao_api.repository.AlergiaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar operações com a tabela `alergia`.
 * 
 * O catálogo de alergias é gerenciado internamente — não há criação, atualização
 * ou exclusão via API pública.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlergiaService {

    private final AlergiaRepository alergiaRepository;

    /**
     * Busca uma alergia por ID.
     *
     * @param id ID da alergia
     * @return DTO da alergia encontrada
     * @throws AlergiaNotFoundException se a alergia não existe
     */
    @Transactional(readOnly = true)
    public AlergiaResponseDTO findById(Long id) {
        log.debug("Buscando alergia por ID: {}", id);
        return alergiaRepository.findById(id)
                .map(AlergiaMapper::toResponseDto)
                .orElseThrow(() -> new AlergiaNotFoundException(id));
    }

    /**
     * Lista todas as alergias cadastradas.
     *
     * @return Lista de todas as alergias
     */
    @Transactional(readOnly = true)
    public List<AlergiaResponseDTO> findAll() {
        log.debug("Listando todas as alergias");
        return alergiaRepository.findAll().stream()
                .map(AlergiaMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca múltiplas alergias por IDs.
     *
     * @param ids Lista de IDs das alergias
     * @return Lista de alergias encontradas
     */
    @Transactional(readOnly = true)
    public List<AlergiaResponseDTO> findByIds(List<Long> ids) {
        log.debug("Buscando alergias por IDs: {}", ids);
        return alergiaRepository.findAllById(ids).stream()
                .map(AlergiaMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
