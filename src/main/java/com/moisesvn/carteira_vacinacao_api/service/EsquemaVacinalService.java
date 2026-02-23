package com.moisesvn.carteira_vacinacao_api.service;

import com.moisesvn.carteira_vacinacao_api.dto.EsquemaVacinalResponseDTO;
import com.moisesvn.carteira_vacinacao_api.exception.VacinaNotFoundException;
import com.moisesvn.carteira_vacinacao_api.mapper.EsquemaVacinalMapper;
import com.moisesvn.carteira_vacinacao_api.repository.EsquemaVacinalRepository;
import com.moisesvn.carteira_vacinacao_api.repository.VacinaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar operações com a tabela `esquema_vacinal`.
 * 
 * Os esquemas vacinais são gerenciados internamente via seed do Flyway.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EsquemaVacinalService {

    private final EsquemaVacinalRepository esquemaVacinalRepository;
    private final VacinaRepository vacinaRepository;

    /**
     * Lista todos os esquemas vacinais (doses) de uma vacina específica.
     *
     * @param vacinaId ID da vacina
     * @return Lista de esquemas vacinais ordenados por idade recomendada
     * @throws VacinaNotFoundException se a vacina não existe
     */
    @Transactional(readOnly = true)
    public List<EsquemaVacinalResponseDTO> findByVacinaId(Long vacinaId) {
        log.debug("Listando esquemas vacinais da vacina ID: {}", vacinaId);
        
        // Valida se vacina existe
        if (!vacinaRepository.existsById(vacinaId)) {
            throw new VacinaNotFoundException(vacinaId);
        }
        
        return esquemaVacinalRepository.findByVacinaIdOrderByIdadeRecomendadaMesesAsc(vacinaId).stream()
                .map(EsquemaVacinalMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Lista todos os esquemas vacinais cadastrados (todas as doses de todas as vacinas).
     *
     * @return Lista completa de esquemas vacinais ordenados por idade recomendada
     */
    @Transactional(readOnly = true)
    public List<EsquemaVacinalResponseDTO> findAll() {
        log.debug("Listando todos os esquemas vacinais");
        return esquemaVacinalRepository.findAllByOrderByIdadeRecomendadaMesesAsc().stream()
                .map(EsquemaVacinalMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
