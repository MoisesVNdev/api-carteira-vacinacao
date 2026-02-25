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
     * Cria um vínculo de responsável manualmente.
     * 
     * NOTA: Este método é mantido para uso interno, mas o endpoint público POST
     * foi removido. A criação normal de responsáveis é automática via PessoaService.
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
