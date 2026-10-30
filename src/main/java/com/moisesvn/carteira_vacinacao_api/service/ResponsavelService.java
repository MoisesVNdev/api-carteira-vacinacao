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
import com.moisesvn.carteira_vacinacao_api.model.Responsavel;package com.moisesvn.carteira_vacinacao_api.service;

import com.moisesvn.carteira_vacinacao_api.dto.request.ResponsavelCreateRequestDTO;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResponsavelService {

    private final ResponsavelRepository responsavelRepository;
    private final UsuarioRepository usuarioRepository;
    private final PessoaRepository pessoaRepository;
    private final AcessoService acessoService;

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
     * O CNS informado precisa ser o da pessoa: sem essa prova, qualquer usuário
     * poderia se vincular a qualquer pessoa só pelo ID e ganhar acesso aos dados dela.
     *
     * @param dto Dados de criação (pessoaId, cns e tipoRelacao)
     * @return DTO com os dados do responsável criado
     * @throws UsuarioNaoEncontradoException se o usuário autenticado não for encontrado
     * @throws PessoaNaoEncontradaException se a pessoa não for encontrada
     * @throws AccessDeniedException se o CNS não for o da pessoa
     * @throws ResponsavelJaCadastradoException se o vínculo já existir
     */
    @Transactional
    public ResponsavelResponseDTO criarVinculoExistente(ResponsavelCreateRequestDTO dto) {
        log.info("Criando vínculo de responsável para pessoa ID: {}, tipo: {}", dto.pessoaId(), dto.tipoRelacao());

        // 1. Extrair usuário do SecurityContext (JWT)
        Usuario usuario = acessoService.usuarioAutenticado();

        // 2. Validar pessoa existe
        Pessoa pessoa = pessoaRepository.findById(dto.pessoaId())
            .orElseThrow(() -> new PessoaNaoEncontradaException(dto.pessoaId()));

        // 3. Confirmar que o usuário conhece a pessoa (CNS precisa conferir)
        if (!Objects.equals(pessoa.getCns(), dto.cns())) {
            log.warn("CNS não confere ao vincular pessoa ID {} (usuário ID {})", pessoa.getId(), usuario.getId());
            throw new AccessDeniedException("Acesso negado");
        }

        // 4. Verificar se vínculo já existe
        if (responsavelRepository.existsByUsuarioIdAndPessoaId(usuario.getId(), dto.pessoaId())) {
            log.warn("Tentativa de criar vínculo duplicado. Usuario ID: {}, Pessoa ID: {}",
                usuario.getId(), dto.pessoaId());
            throw new ResponsavelJaCadastradoException(usuario.getId(), dto.pessoaId());
        }

        // 5. Criar e persistir vínculo
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
     * Atualiza o tipo de relação de um vínculo do próprio usuário.
     *
     * @param id ID do responsável
     * @param dto Dados de atualização (contém apenas tipoRelacao)
     * @return DTO com os dados atualizados
     * @throws ResponsavelNaoEncontradoException se o responsável não for encontrado
     * @throws AccessDeniedException se o vínculo for de outro usuário
     */
    @Transactional
    public ResponsavelResponseDTO update(Long id, ResponsavelUpdateRequestDTO dto) {
        log.info("Atualizando tipo de relação do responsável ID: {}", id);

        Responsavel responsavel = buscarVinculoDoUsuario(id);

        responsavel.setTipoRelacao(dto.tipoRelacao());
        Responsavel atualizado = responsavelRepository.save(responsavel);

        log.info("Tipo de relação atualizado com sucesso. ID: {}, Novo tipo: {}", id, dto.tipoRelacao());

        return ResponsavelMapper.toResponseDto(atualizado);
    }

    /**
     * Lista os vínculos de um usuário. Só o próprio usuário pode consultar.
     *
     * @throws AccessDeniedException se o usuarioId não for o do usuário autenticado
     */
    @Transactional(readOnly = true)
    public List<ResponsavelResponseDTO> findByUsuarioId(Long usuarioId) {
        acessoService.validarProprioUsuario(usuarioId);
        return responsavelRepository.findByUsuarioId(usuarioId).stream()
                .map(ResponsavelMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponsavelResponseDTO findById(Long id) {
        return ResponsavelMapper.toResponseDto(buscarVinculoDoUsuario(id));
    }

    @Transactional
    public void deleteById(Long id) {
        responsavelRepository.delete(buscarVinculoDoUsuario(id));
    }

    /**
     * Busca um vínculo e garante que ele pertence ao usuário autenticado.
     *
     * @throws ResponsavelNaoEncontradoException se o vínculo não existe
     * @throws AccessDeniedException se o vínculo for de outro usuário
     */
    private Responsavel buscarVinculoDoUsuario(Long id) {
        Responsavel responsavel = responsavelRepository.findById(id)
                .orElseThrow(() -> new ResponsavelNaoEncontradoException(id));

        Usuario usuario = acessoService.usuarioAutenticado();
        if (!responsavel.getUsuario().getId().equals(usuario.getId())) {
            log.warn("Acesso negado. Vínculo ID {} não pertence ao usuário ID {}", id, usuario.getId());
            throw new AccessDeniedException("Acesso negado");
        }
        return responsavel;
    }
}

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
