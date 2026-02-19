package com.moisesvn.carteira_vacinacao_api.service;

import com.moisesvn.carteira_vacinacao_api.dto.PessoaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.PessoaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.exception.PessoaNaoEncontradaException;
import com.moisesvn.carteira_vacinacao_api.mapper.PessoaMapper;
import com.moisesvn.carteira_vacinacao_api.model.Pessoa;
import com.moisesvn.carteira_vacinacao_api.repository.PessoaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public PessoaResponseDTO create(PessoaRequestDTO dto) {
        Pessoa pessoa = PessoaMapper.toEntity(dto);
        Pessoa saved = pessoaRepository.save(pessoa);
        return PessoaMapper.toResponseDto(saved);
    }

    public PessoaResponseDTO findById(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
            .orElseThrow(() -> new PessoaNaoEncontradaException(id));
        return PessoaMapper.toResponseDto(pessoa);
    }

    public List<PessoaResponseDTO> findAll() {
        return pessoaRepository.findAll().stream()
            .map(PessoaMapper::toResponseDto)
            .collect(Collectors.toList());
    }

    public PessoaResponseDTO update(Long id, PessoaRequestDTO dto) {
        Pessoa pessoa = pessoaRepository.findById(id)
            .orElseThrow(() -> new PessoaNaoEncontradaException(id));

        pessoa.setNomeCompleto(dto.nomeCompleto());
        pessoa.setDataNascimento(dto.dataNascimento());
        pessoa.setCns(dto.cns());
        pessoa.setCpf(dto.cpf());
        pessoa.setNomeMae(dto.nomeMae());
        pessoa.setGenero(dto.genero());
        pessoa.setNacionalidade(dto.nacionalidade());
        pessoa.setNaturalidade(dto.naturalidade());
        pessoa.setTipoSanguineo(dto.tipoSanguineo());
        pessoa.setFoto(dto.foto());

        Pessoa updated = pessoaRepository.save(pessoa);
        return PessoaMapper.toResponseDto(updated);
    }

    public void delete(Long id) {
        if (!pessoaRepository.existsById(id)) {
            throw new PessoaNaoEncontradaException(id);
        }
        pessoaRepository.deleteById(id);
    }
}
