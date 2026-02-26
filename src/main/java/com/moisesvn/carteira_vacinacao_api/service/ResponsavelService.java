package com.moisesvn.carteira_vacinacao_api.service;

import com.moisesvn.carteira_vacinacao_api.dto.request.ResponsavelRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.response.ResponsavelResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.request.ResponsavelUpdateRequestDTO;
import com.moisesvn.carteira_vacinacao_api.exception.PessoaNaoEncontradaException;
import com.moisesvn.carteira_vacinacao_api.exception.ResponsavelJaCadastradoException;
import com.moisesvn.carteira_vacinacao_api.exception.ResponsavelNaoEncontradoException;
import com.moisesvn.carteira_vacinacao_api.exception.UsuarioNaoEncontradoException;
import com.moisesvn.carteira_vacinacao_api.mapper.ResponsavelMapper;
import com.moisesvn.carteira_vacinacao_api.model.Pessoa;
import com.moisesvn.carteira_vacinacao_api.model.Responsavel;
import com.moisesvn.carteira_vacinacao_api.model.Usuario;
import com.moisesvn.carteira_vacinacao_api.repository.PessoaRepository;
import com.moisesvn.carteira_vacinacao_api.repository.ResponsavelRepository;
import com.moisesvn.carteira_vacinacao_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResponsavelService {

    private final ResponsavelRepository responsavelRepository;
    private final UsuarioRepository usuarioRepository;
    private final PessoaRepository pessoaRepository;

    /**
     * Cria automaticamente um vínculo de responsável após a criação de uma pessoa.
     * Esta é uma operação interna (chamada pelo PessoaService).
     * 
     * @param usuario Usuário responsável (já validado)
     * @param pessoa Pessoa criada (já persistida)
     * @param tipoRelacao Tipo de relação (ex: MAE, PAI, RESPONSAVEL, etc)
     */
    @Transactional
    public void criarAutomaticamente(Usuario usuario, Pessoa pessoa, String tipoRelacao) {
        log.info("Criando vínculo de responsável automaticamente. Usuario ID: {}, Pessoa ID: {}, Tipo: {}", 
                usuario.getId(), pessoa.getId(), tipoRelacao);
        
        Responsavel responsavel = Responsavel.builder()
                .usuario(usuario)
                .pessoa(pessoa)
                .tipoRelacao(tipoRelacao)
                .build();
        responsavelRepository.save(responsavel);
        
        log.info("Vínculo de responsável criado com sucesso");
    }

    /**
     * Cria um vínculo de responsável manualmente com usuarioId explícito.
     * Uso interno apenas.
     */
    @Transactional
    ResponsavelResponseDTO create(ResponsavelRequestDTO dto) {
        Long usuarioId = dto.usuarioId();
        Long pessoaId = dto.pessoaId();

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));

        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new PessoaNaoEncontradaException(pessoaId));

        if (responsavelRepository.existsByUsuarioIdAndPessoaId(usuarioId, pessoaId)) {
            throw new ResponsavelJaCadastradoException(usuarioId, pessoaId);
        }

        Responsavel r = Responsavel.builder()
                .usuario(usuario)
                .pessoa(pessoa)
                .tipoRelacao(dto.tipoRelacao())
                .build();

        Responsavel salvo = responsavelRepository.save(r);
        return ResponsavelMapper.toResponseDto(salvo);
    }

    /**
     * Cria um novo vínculo de responsável para uma pessoa já existente.
     * O usuário responsável é extraído do JWT (SecurityContext).
     * 
     * Caso de uso: Vincular uma pessoa já cadastrada a outro responsável.
     * Exemplo: João quer adicionar sua filha Ana (já cadastrada por Maria) ao seu cadastro.
     * 
     * @param dto Dados de criação (pessoaId e tipoRelacao)
     * @return DTO com os dados do responsável criado
     * @throws UsuarioNaoEncontradoException se o usuário autenticado não for encontrado
     * @throws PessoaNaoEncontradaException se a pessoa não for encontrada
     * @throws ResponsavelJaCadastradoException se o vínculo já existir
     */
    @Transactional
    public ResponsavelResponseDTO criarVinculoExistente(com.moisesvn.carteira_vacinacao_api.dto.request.ResponsavelCreateRequestDTO dto) {
        log.info("Criando vínculo de responsável para pessoa ID: {}, tipo: {}", dto.pessoaId(), dto.tipoRelacao());
        
        // 1. Extrair usuário do SecurityContext (JWT)
        String emailAutenticado = org.springframework.security.core.context.SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();
        
        Usuario usuario = usuarioRepository.findByEmail(emailAutenticado)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário autenticado não encontrado"));
        
        log.debug("Usuário autenticado: {} (ID: {})", emailAutenticado, usuario.getId());
        
        // 2. Validar pessoa existe
        Pessoa pessoa = pessoaRepository.findById(dto.pessoaId())
            .orElseThrow(() -> new PessoaNaoEncontradaException(dto.pessoaId()));
        
        // 3. Verificar se vínculo já existe
        if (responsavelRepository.existsByUsuarioIdAndPessoaId(usuario.getId(), dto.pessoaId())) {
            log.warn("Tentativa de criar vínculo duplicado. Usuario ID: {}, Pessoa ID: {}", 
                usuario.getId(), dto.pessoaId());
            throw new ResponsavelJaCadastradoException(usuario.getId(), dto.pessoaId());
        }
        
        // 4. Criar e persistir vínculo
        Responsavel responsavel = Responsavel.builder()
            .usuario(usuario)
            .pessoa(pessoa)
            .tipoRelacao(dto.tipoRelacao())
            .build();
        
        Responsavel salvo = responsavelRepository.save(responsavel);
        
        log.info("Vínculo de responsável criado com sucesso. ID: {}, Usuario ID: {}, Pessoa ID: {}", 
            salvo.getId(), usuario.getId(), pessoa.getId());
        
        return ResponsavelMapper.toResponseDto(salvo);
    }

    /**
     * Atualiza o tipo de relação de um vínculo existente.
     * 
     * @param id ID do responsável
     * @param dto Dados de atualização (contém apenas tipoRelacao)
     * @return DTO com os dados atualizados
     * @throws ResponsavelNaoEncontradoException se o responsável não for encontrado
     */
    @Transactional
    public ResponsavelResponseDTO update(Long id, ResponsavelUpdateRequestDTO dto) {
        log.info("Atualizando tipo de relação do responsável ID: {}", id);
        
        Responsavel responsavel = responsavelRepository.findById(id)
                .orElseThrow(() -> new ResponsavelNaoEncontradoException(id));
        
        responsavel.setTipoRelacao(dto.tipoRelacao());
        Responsavel atualizado = responsavelRepository.save(responsavel);
        
        log.info("Tipo de relação atualizado com sucesso. ID: {}, Novo tipo: {}", id, dto.tipoRelacao());
        
        return ResponsavelMapper.toResponseDto(atualizado);
    }

    @Transactional(readOnly = true)
    public List<ResponsavelResponseDTO> findByUsuarioId(Long usuarioId) {
        return responsavelRepository.findByUsuarioId(usuarioId).stream()
                .map(ResponsavelMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponsavelResponseDTO findById(Long id) {
        return responsavelRepository.findById(id)
                .map(ResponsavelMapper::toResponseDto)
                .orElseThrow(() -> new ResponsavelNaoEncontradoException(id));
    }

    @Transactional
    public void deleteById(Long id) {
        responsavelRepository.deleteById(id);
    }
}
