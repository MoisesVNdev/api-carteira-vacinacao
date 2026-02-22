package com.moisesvn.carteira_vacinacao_api.service;

import com.moisesvn.carteira_vacinacao_api.dto.PessoaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.PessoaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.exception.CnsJaCadastradoException;
import com.moisesvn.carteira_vacinacao_api.exception.CpfJaCadastradoException;
import com.moisesvn.carteira_vacinacao_api.exception.PessoaNaoEncontradaException;
import com.moisesvn.carteira_vacinacao_api.exception.UsuarioNaoEncontradoException;
import com.moisesvn.carteira_vacinacao_api.mapper.PessoaMapper;
import com.moisesvn.carteira_vacinacao_api.model.Pessoa;
import com.moisesvn.carteira_vacinacao_api.model.Responsavel;
import com.moisesvn.carteira_vacinacao_api.model.Usuario;
import com.moisesvn.carteira_vacinacao_api.repository.PessoaRepository;
import com.moisesvn.carteira_vacinacao_api.repository.ResponsavelRepository;
import com.moisesvn.carteira_vacinacao_api.repository.UsuarioRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final ResponsavelRepository responsavelRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Cria uma nova pessoa e automaticamente cria o vínculo de responsável
     * com o usuário autenticado (extraído do JWT via SecurityContext).
     * 
     * @param dto Dados da pessoa a ser criada (inclui tipoRelacao)
     * @return DTO com os dados da pessoa criada
     * @throws UsuarioNaoEncontradoException se o usuário autenticado não for encontrado
     * @throws CnsJaCadastradoException se o CNS já estiver cadastrado
     * @throws CpfJaCadastradoException se o CPF já estiver cadastrado
     */
    @Transactional
    public PessoaResponseDTO create(PessoaRequestDTO dto) {
        log.info("Criando nova pessoa com CNS: {}", dto.cns());
        
        // 1. Obter usuario_id do SecurityContext (email armazenado no JWT subject)
        String emailAutenticado = SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();
        
        Usuario usuario = usuarioRepository.findByEmail(emailAutenticado)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário autenticado não encontrado"));
        
        log.debug("Usuário autenticado: {} (ID: {})", emailAutenticado, usuario.getId());
        
        // 2. Validar unicidade de CNS/CPF ANTES de persistir
        if (pessoaRepository.findByCns(dto.cns()).isPresent()) {
            log.warn("Tentativa de cadastrar CNS duplicado: {}", dto.cns());
            throw new CnsJaCadastradoException(dto.cns());
        }
        
        if (dto.cpf() != null && !dto.cpf().isBlank() && pessoaRepository.findByCpf(dto.cpf()).isPresent()) {
            log.warn("Tentativa de cadastrar CPF duplicado: {}", dto.cpf());
            throw new CpfJaCadastradoException(dto.cpf());
        }
        
        // 3. Criar e persistir Pessoa
        Pessoa pessoa = PessoaMapper.toEntity(dto);
        Pessoa saved = pessoaRepository.save(pessoa);
        
        log.info("Pessoa criada com sucesso. ID: {}, CNS: {}", saved.getId(), saved.getCns());
        
        // 4. Criar automaticamente o registro de Responsavel
        Responsavel responsavel = Responsavel.builder()
            .usuario(usuario)
            .pessoa(saved)
            .tipoRelacao(dto.tipoRelacao())
            .build();
        responsavelRepository.save(responsavel);
        
        log.info("Vínculo de responsável criado automaticamente. Usuario ID: {}, Pessoa ID: {}, Tipo: {}", 
                 usuario.getId(), saved.getId(), dto.tipoRelacao());
        
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

    @Transactional
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

    @Transactional
    public void delete(Long id) {
        if (!pessoaRepository.existsById(id)) {
            throw new PessoaNaoEncontradaException(id);
        }
        pessoaRepository.deleteById(id);
    }
}
