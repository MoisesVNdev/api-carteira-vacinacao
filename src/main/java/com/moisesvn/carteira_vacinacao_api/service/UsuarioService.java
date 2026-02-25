package com.moisesvn.carteira_vacinacao_api.service;

import com.moisesvn.carteira_vacinacao_api.dto.request.UsuarioRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.response.UsuarioResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.request.UsuarioUpdateRequestDTO;
import com.moisesvn.carteira_vacinacao_api.exception.EmailJaCadastradoException;
import com.moisesvn.carteira_vacinacao_api.exception.UsuarioNaoEncontradoException;
import com.moisesvn.carteira_vacinacao_api.model.Usuario;
import com.moisesvn.carteira_vacinacao_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Cria um novo usuário validando unicidade de email.
     * A senha é codificada com BCrypt antes de persistir.
     *
     * @param dto Dados do novo usuário
     * @return DTO com o usuário criado
     * @throws EmailJaCadastradoException se o email já existe
     */
    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        log.info("Criando novo usuário com email: {}", dto.getEmail());
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException(dto.getEmail());
        }
        Usuario usuario = Usuario.builder()
                .nomeCompleto(dto.getNomeCompleto())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .build();
        return toResponseDTO(usuarioRepository.save(usuario));
    }

    /**
     * Lista todos os usuários cadastrados.
     *
     * @return Lista de usuários em formato DTO
     */
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        log.debug("Listando todos os usuários");
        return usuarioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca um usuário por ID.
     *
     * @param id ID do usuário
     * @return DTO do usuário encontrado
     * @throws UsuarioNaoEncontradoException se o usuário não existe
     */
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        log.debug("Buscando usuário por id: {}", id);
        return toResponseDTO(buscarEntidadePorId(id));
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id ID do usuário
     * @param dto Dados a serem atualizados
     * @return DTO do usuário atualizado
     * @throws UsuarioNaoEncontradoException se o usuário não existe
     */
    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioUpdateRequestDTO dto) {
        log.info("Atualizando usuário com id: {}", id);
        Usuario usuario = buscarEntidadePorId(id);
        usuario.setNomeCompleto(dto.getNomeCompleto());
        return toResponseDTO(usuarioRepository.save(usuario));
    }

    /**
     * Deleta um usuário pelo ID.
     *
     * @param id ID do usuário a deletar
     * @throws UsuarioNaoEncontradoException se o usuário não existe
     */
    @Transactional
    public void excluir(Long id) {
        log.info("Deletando usuário com id: {}", id);
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontradoException(id);
        }
        usuarioRepository.deleteById(id);
    }

    // --- helpers privados ---

    private Usuario buscarEntidadePorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNomeCompleto(),
                usuario.getEmail(),
                usuario.getDataCadastro()
        );
    }
}