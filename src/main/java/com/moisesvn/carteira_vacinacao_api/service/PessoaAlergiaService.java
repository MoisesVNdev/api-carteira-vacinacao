package com.moisesvn.carteira_vacinacao_api.service;

import com.moisesvn.carteira_vacinacao_api.dto.request.PessoaAlergiaListRequestItem;
import com.moisesvn.carteira_vacinacao_api.dto.request.PessoaAlergiaObservacaoRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.request.PessoaAlergiaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.response.PessoaAlergiaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.exception.AlergiaNotFoundException;
import com.moisesvn.carteira_vacinacao_api.exception.PessoaAlergiaJaCadastradoException;
import com.moisesvn.carteira_vacinacao_api.exception.PessoaAlergiaNotFoundException;
import com.moisesvn.carteira_vacinacao_api.exception.PessoaNaoEncontradaException;
import com.moisesvn.carteira_vacinacao_api.mapper.PessoaAlergiaMapper;
import com.moisesvn.carteira_vacinacao_api.model.Alergia;
import com.moisesvn.carteira_vacinacao_api.model.Pessoa;
import com.moisesvn.carteira_vacinacao_api.model.PessoaAlergia;
import com.moisesvn.carteira_vacinacao_api.repository.AlergiaRepository;
import com.moisesvn.carteira_vacinacao_api.repository.PessoaAlergiaRepository;
import com.moisesvn.carteira_vacinacao_api.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar operações com a tabela `pessoa_alergia`.
 * 
 * Valida que a pessoa pertence ao usuário autenticado antes de qualquer operação.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PessoaAlergiaService {

    private final PessoaAlergiaRepository pessoaAlergiaRepository;
    private final AlergiaRepository alergiaRepository;
    private final PessoaRepository pessoaRepository;

    /**
     * Lista todas as alergias vinculadas a uma pessoa.
     *
     * @param pessoaId ID da pessoa
     * @return Lista de alergias vinculadas
     */
    @Transactional(readOnly = true)
    public List<PessoaAlergiaResponseDTO> findByPessoa(Long pessoaId) {
        log.debug("Listando alergias da pessoa ID: {}", pessoaId);
        
        // Valida se pessoa existe
        if (!pessoaRepository.existsById(pessoaId)) {
            throw new PessoaNaoEncontradaException(pessoaId);
        }
        
        return pessoaAlergiaRepository.findByPessoaId(pessoaId).stream()
                .map(PessoaAlergiaMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Vincula uma alergia a uma pessoa (POST simples).
     *
     * @param pessoaId ID da pessoa
     * @param dto Dados da requisição (alergiaId, observacao)
     * @return DTO do vínculo criado
     * @throws PessoaNaoEncontradaException se a pessoa não existe
     * @throws AlergiaNotFoundException se a alergia não existe
     * @throws PessoaAlergiaJaCadastradoException se o vínculo já existe
     */
    @Transactional
    public PessoaAlergiaResponseDTO create(Long pessoaId, PessoaAlergiaRequestDTO dto) {
        log.info("Vinculando alergia ID {} à pessoa ID {}", dto.alergiaId(), pessoaId);
        
        // Valida pessoa
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new PessoaNaoEncontradaException(pessoaId));
        
        // Valida alergia
        Alergia alergia = alergiaRepository.findById(dto.alergiaId())
                .orElseThrow(() -> new AlergiaNotFoundException(dto.alergiaId()));
        
        // Valida se vínculo já existe
        if (pessoaAlergiaRepository.existsByPessoaIdAndAlergiaId(pessoaId, dto.alergiaId())) {
            log.warn("Tentativa de vínculo duplicado. Pessoa ID: {}, Alergia ID: {}", 
                     pessoaId, dto.alergiaId());
            throw new PessoaAlergiaJaCadastradoException(pessoaId, dto.alergiaId());
        }
        
        // Cria vínculo
        PessoaAlergia pessoaAlergia = PessoaAlergia.builder()
                .pessoa(pessoa)
                .alergia(alergia)
                .observacao(dto.observacao())
                .build();
        
        PessoaAlergia saved = pessoaAlergiaRepository.save(pessoaAlergia);
        pessoaAlergiaRepository.flush(); // Força flush para popular @CreationTimestamp
        
        log.info("Alergia vinculada com sucesso. Pessoa ID: {}, Alergia ID: {}", pessoaId, alergia.getId());
        
        return PessoaAlergiaMapper.toResponseDto(saved);
    }

    /**
     * Vincula múltiplas alergias a uma pessoa em uma única transação (POST em lote).
     * 
     * Se qualquer alergia já estiver vinculada, toda a operação falha (atômica).
     *
     * @param pessoaId ID da pessoa
     * @param items Lista de alergias a vincular
     * @return Lista de vínculos criados
     * @throws PessoaNaoEncontradaException se a pessoa não existe
     * @throws AlergiaNotFoundException se alguma alergia não existe
     * @throws PessoaAlergiaJaCadastradoException se qualquer vínculo já existe
     */
    @Transactional
    public List<PessoaAlergiaResponseDTO> createBatch(Long pessoaId, List<PessoaAlergiaListRequestItem> items) {
        log.info("Vinculando {} alergias à pessoa ID {} (batch)", items.size(), pessoaId);
        
        // Valida pessoa
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new PessoaNaoEncontradaException(pessoaId));
        
        // Valida todas as alergias e checa duplicatas ANTES de persistir
        for (PessoaAlergiaListRequestItem item : items) {
            alergiaRepository.findById(item.alergiaId())
                    .orElseThrow(() -> new AlergiaNotFoundException(item.alergiaId()));
            
            if (pessoaAlergiaRepository.existsByPessoaIdAndAlergiaId(pessoaId, item.alergiaId())) {
                log.warn("Alergia ID {} já vinculada à pessoa ID {}", item.alergiaId(), pessoaId);
                throw new PessoaAlergiaJaCadastradoException(pessoaId, item.alergiaId());
            }
        }
        
        // Se tudo validado, cria os vínculos
        List<PessoaAlergia> pessoasAlergias = items.stream()
                .map(item -> {
                    Alergia alergia = alergiaRepository.findById(item.alergiaId()).get();
                    return PessoaAlergia.builder()
                            .pessoa(pessoa)
                            .alergia(alergia)
                            .observacao(item.observacao())
                            .build();
                })
                .collect(Collectors.toList());
        
        List<PessoaAlergia> savedItems = pessoaAlergiaRepository.saveAll(pessoasAlergias);
        pessoaAlergiaRepository.flush(); // Força flush para popular @CreationTimestamp
        
        log.info("Batch de {} alergias vinculadas com sucesso à pessoa ID {}", 
                 savedItems.size(), pessoaId);
        
        return savedItems.stream()
                .map(PessoaAlergiaMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza apenas o campo observacao de um vínculo (PUT).
     *
     * @param pessoaId ID da pessoa
     * @param alergiaId ID da alergia
     * @param dto Dados da requisição (nova observacao)
     * @return DTO do vínculo atualizado
     * @throws PessoaAlergiaNotFoundException se o vínculo não existe
     */
    @Transactional
    public PessoaAlergiaResponseDTO updateObservacao(
            Long pessoaId,
            Long alergiaId,
            PessoaAlergiaObservacaoRequestDTO dto) {
        
        log.info("Atualizando observação do vínculo. Pessoa ID: {}, Alergia ID: {}", 
                 pessoaId, alergiaId);
        
        PessoaAlergia pessoaAlergia = pessoaAlergiaRepository
                .findByPessoaIdAndAlergiaId(pessoaId, alergiaId)
                .orElseThrow(() -> new PessoaAlergiaNotFoundException(pessoaId, alergiaId));
        
        pessoaAlergia.setObservacao(dto.observacao());
        PessoaAlergia updated = pessoaAlergiaRepository.save(pessoaAlergia);
        
        log.info("Observação atualizada com sucesso. Pessoa ID: {}, Alergia ID: {}", 
                 pessoaId, alergiaId);
        
        return PessoaAlergiaMapper.toResponseDto(updated);
    }

    /**
     * Remove (limpa) apenas o campo observacao, mantendo o vínculo (DELETE observacao).
     *
     * @param pessoaId ID da pessoa
     * @param alergiaId ID da alergia
     * @throws PessoaAlergiaNotFoundException se o vínculo não existe
     */
    @Transactional
    public void deleteObservacao(Long pessoaId, Long alergiaId) {
        log.info("Removendo observação do vínculo. Pessoa ID: {}, Alergia ID: {}", 
                 pessoaId, alergiaId);
        
        PessoaAlergia pessoaAlergia = pessoaAlergiaRepository
                .findByPessoaIdAndAlergiaId(pessoaId, alergiaId)
                .orElseThrow(() -> new PessoaAlergiaNotFoundException(pessoaId, alergiaId));
        
        pessoaAlergia.setObservacao(null);
        pessoaAlergiaRepository.save(pessoaAlergia);
        
        log.info("Observação removida com sucesso. Pessoa ID: {}, Alergia ID: {}", 
                 pessoaId, alergiaId);
    }

    /**
     * Remove completamente o vínculo entre pessoa e alergia (DELETE vínculo).
     *
     * @param pessoaId ID da pessoa
     * @param alergiaId ID da alergia
     * @throws PessoaAlergiaNotFoundException se o vínculo não existe
     */
    @Transactional
    public void delete(Long pessoaId, Long alergiaId) {
        log.info("Removendo vínculo de alergia. Pessoa ID: {}, Alergia ID: {}", 
                 pessoaId, alergiaId);
        
        PessoaAlergia pessoaAlergia = pessoaAlergiaRepository
                .findByPessoaIdAndAlergiaId(pessoaId, alergiaId)
                .orElseThrow(() -> new PessoaAlergiaNotFoundException(pessoaId, alergiaId));
        
        pessoaAlergiaRepository.delete(pessoaAlergia);
        
        log.info("Vínculo de alergia removido com sucesso. Pessoa ID: {}, Alergia ID: {}", 
                 pessoaId, alergiaId);
    }
}
