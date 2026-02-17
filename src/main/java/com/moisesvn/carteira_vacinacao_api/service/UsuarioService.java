package com.moisesvn.carteira_vacinacao_api.service;

import com.moisesvn.carteira_vacinacao_api.dto.UsuarioRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.UsuarioResponseDTO;
import com.moisesvn.carteira_vacinacao_api.exception.EmailJaCadastradoException;
import com.moisesvn.carteira_vacinacao_api.exception.UsuarioNaoEncontradoException;
import com.moisesvn.carteira_vacinacao_api.model.Usuario;
import com.moisesvn.carteira_vacinacao_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
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

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarEntidadePorId(id));
    }

    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = buscarEntidadePorId(id);

        // Verifica duplicidade de e-mail apenas se mudou
        if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException(dto.getEmail());
        }

        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        return toResponseDTO(usuarioRepository.save(usuario));
    }

    public void excluir(Long id) {
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
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nomeCompleto(usuario.getNomeCompleto())
                .email(usuario.getEmail())
                .dataCadastro(usuario.getDataCadastro())
                .build();
    }
}