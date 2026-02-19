package com.moisesvn.carteira_vacinacao_api.service;

import com.moisesvn.carteira_vacinacao_api.dto.ResponsavelRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.ResponsavelResponseDTO;
import com.moisesvn.carteira_vacinacao_api.exception.PessoaNaoEncontradaException;
import com.moisesvn.carteira_vacinacao_api.exception.ResponsavelJaCadastradoException;
import com.moisesvn.carteira_vacinacao_api.exception.UsuarioNaoEncontradoException;
import com.moisesvn.carteira_vacinacao_api.mapper.ResponsavelMapper;
import com.moisesvn.carteira_vacinacao_api.model.Pessoa;
import com.moisesvn.carteira_vacinacao_api.model.Responsavel;
import com.moisesvn.carteira_vacinacao_api.model.Usuario;
import com.moisesvn.carteira_vacinacao_api.repository.PessoaRepository;
import com.moisesvn.carteira_vacinacao_api.repository.ResponsavelRepository;
import com.moisesvn.carteira_vacinacao_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResponsavelService {

    private final ResponsavelRepository responsavelRepository;
    private final UsuarioRepository usuarioRepository;
    private final PessoaRepository pessoaRepository;

    @Transactional
    public ResponsavelResponseDTO create(ResponsavelRequestDTO dto) {
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
                .orElse(null);
    }

    @Transactional
    public void deleteById(Long id) {
        responsavelRepository.deleteById(id);
    }
}
