package com.moisesvn.carteira_vacinacao_api.service;

import com.moisesvn.carteira_vacinacao_api.dto.request.PessoaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.response.PessoaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.exception.CnsJaCadastradoException;
import com.moisesvn.carteira_vacinacao_api.exception.CpfJaCadastradoException;
import com.moisesvn.carteira_vacinacao_api.exception.PessoaNaoEncontradaException;
import com.moisesvn.carteira_vacinacao_api.exception.PessoaNaoPertenceAoUsuarioException;
import com.moisesvn.carteira_vacinacao_api.exception.UsuarioNaoEncontradoException;
import com.moisesvn.carteira_vacinacao_api.mapper.PessoaMapper;
import com.moisesvn.carteira_vacinacao_api.model.Pessoa;
import com.moisesvn.carteira_vacinacao_api.model.Responsavel;
import com.moisesvn.carteira_vacinacao_api.model.Usuario;
import com.moisesvn.carteira_vacinacao_api.repository.PessoaRepository;
import com.moisesvn.carteira_vacinacao_api.repository.ResponsavelRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço de pessoas (crianças, adolescentes e adultos acompanhados).
 *
 * Toda leitura e alteração por ID exige que a pessoa esteja vinculada
 * ao usuário autenticado (ver {@link AcessoService}).
 * CPF e CNS não são gravados em log (dados pessoais - LGPD).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final ResponsavelRepository responsavelRepository;
    private final ResponsavelService responsavelService;
    private final AcessoService acessoService;

    /**
     * Cria uma nova pessoa e automaticamente cria o vínculo de responsável
     * com o usuário autenticado (extraído do JWT via SecurityContext).
     *
     * Delega a criação do Responsavel para ResponsavelService,
     * mantendo cada serviço com responsabilidade única.
     *
     * @param dto Dados da pessoa a ser criada (inclui tipoRelacao)
     * @return DTO com os dados da pessoa criada
     * @throws UsuarioNaoEncontradoException se o usuário autenticado não for encontrado
     * @throws CnsJaCadastradoException se o CNS já estiver cadastrado
     * @throws CpfJaCadastradoException se o CPF já estiver cadastrado
     */
    @Transactional
    public PessoaResponseDTO create(PessoaRequestDTO dto) {
        // 1. Obter o usuário autenticado (email armazenado no JWT subject)
        Usuario usuario = acessoService.usuarioAutenticado();
        log.info("Criando nova pessoa para o usuário ID {}", usuario.getId());

        // 2. Validar unicidade de CNS/CPF ANTES de persistir
        if (pessoaRepository.findByCns(dto.cns()).isPresent()) {
            log.warn("Tentativa de cadastrar CNS duplicado (usuário ID {})", usuario.getId());
            throw new CnsJaCadastradoException(dto.cns());
        }

        if (dto.cpf() != null && !dto.cpf().isBlank() && pessoaRepository.findByCpf(dto.cpf()).isPresent()) {
            log.warn("Tentativa de cadastrar CPF duplicado (usuário ID {})", usuario.getId());
            throw new CpfJaCadastradoException(dto.cpf());
        }

        // 3. Criar e persistir Pessoa
        Pessoa pessoa = PessoaMapper.toEntity(dto);
        Pessoa saved = pessoaRepository.save(pessoa);

        log.info("Pessoa criada com sucesso. ID: {}", saved.getId());

        // 4. Delegar criação do Responsavel para ResponsavelService (SRP)
        responsavelService.criarAutomaticamente(usuario, saved, dto.tipoRelacao());

        return PessoaMapper.toResponseDto(saved);
    }

    /**
     * Busca uma pessoa vinculada ao usuário autenticado.
     *
     * @throws PessoaNaoPertenceAoUsuarioException se a pessoa não pertence ao usuário
     */
    @Transactional(readOnly = true)
    public PessoaResponseDTO findById(Long id) {
        acessoService.validarPosseDaPessoa(id);
        Pessoa pessoa = pessoaRepository.findById(id)
            .orElseThrow(() -> new PessoaNaoEncontradaException(id));
        return PessoaMapper.toResponseDto(pessoa);
    }

    /**
     * Busca uma pessoa por CPF e CNS.
     *
     * Usado para localizar uma pessoa já cadastrada antes de vincular um novo
     * responsável. Exige os dois documentos, que só quem conhece a pessoa tem.
     *
     * @param cpf CPF da pessoa
     * @param cns CNS da pessoa
     * @return DTO com os dados da pessoa encontrada
     * @throws PessoaNaoEncontradaException se nenhuma pessoa for encontrada
     */
    @Transactional(readOnly = true)
    public PessoaResponseDTO findByCpfAndCns(String cpf, String cns) {
        log.info("Buscando pessoa por CPF e CNS");
        Pessoa pessoa = pessoaRepository.findByCpfAndCns(cpf, cns)
            .orElseThrow(() -> new PessoaNaoEncontradaException(
                "Nenhuma pessoa encontrada com o CPF e o CNS informados"
            ));
        return PessoaMapper.toResponseDto(pessoa);
    }

    /**
     * Lista apenas as pessoas vinculadas ao usuário autenticado.
     */
    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> findAll() {
        Usuario usuario = acessoService.usuarioAutenticado();
        return responsavelRepository.findByUsuarioId(usuario.getId()).stream()
            .map(Responsavel::getPessoa)
            .map(PessoaMapper::toResponseDto)
            .collect(Collectors.toList());
    }

    /**
     * Atualiza uma pessoa vinculada ao usuário autenticado.
     *
     * @throws PessoaNaoPertenceAoUsuarioException se a pessoa não pertence ao usuário
     */
    @Transactional
    public PessoaResponseDTO update(Long id, PessoaRequestDTO dto) {
        acessoService.validarPosseDaPessoa(id);
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

    /**
     * Exclui uma pessoa vinculada ao usuário autenticado.
     *
     * @throws PessoaNaoPertenceAoUsuarioException se a pessoa não pertence ao usuário
     */
    @Transactional
    public void delete(Long id) {
        acessoService.validarPosseDaPessoa(id);
        if (!pessoaRepository.existsById(id)) {
            throw new PessoaNaoEncontradaException(id);
        }
        pessoaRepository.deleteById(id);
    }
}
