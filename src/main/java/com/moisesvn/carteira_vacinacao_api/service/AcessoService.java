package com.moisesvn.carteira_vacinacao_api.service;

import com.moisesvn.carteira_vacinacao_api.exception.PessoaNaoPertenceAoUsuarioException;
import com.moisesvn.carteira_vacinacao_api.exception.UsuarioNaoEncontradoException;
import com.moisesvn.carteira_vacinacao_api.model.Usuario;
import com.moisesvn.carteira_vacinacao_api.repository.ResponsavelRepository;
import com.moisesvn.carteira_vacinacao_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Centraliza as regras de acesso aos dados (proteção contra IDOR).
 *
 * Regras:
 * - Uma pessoa só pode ser acessada pelos usuários vinculados a ela como responsáveis.
 * - Um usuário só pode consultar, alterar ou excluir a própria conta.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AcessoService {

    private final UsuarioRepository usuarioRepository;
    private final ResponsavelRepository responsavelRepository;

    /**
     * Retorna o usuário autenticado (email armazenado no subject do JWT).
     *
     * @return Usuário autenticado
     * @throws UsuarioNaoEncontradoException se o usuário do token não existe mais
     */
    @Transactional(readOnly = true)
    public Usuario usuarioAutenticado() {
        String emailAutenticado = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return usuarioRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário autenticado não encontrado"));
    }

    /**
     * Valida se a pessoa está vinculada ao usuário autenticado.
     * Pessoa inexistente também resulta em 403, para não revelar quais IDs existem.
     *
     * @param pessoaId ID da pessoa
     * @throws PessoaNaoPertenceAoUsuarioException se a pessoa não pertence ao usuário
     */
    @Transactional(readOnly = true)
    public void validarPosseDaPessoa(Long pessoaId) {
        Usuario usuario = usuarioAutenticado();

        if (!responsavelRepository.existsByUsuarioIdAndPessoaId(usuario.getId(), pessoaId)) {
            log.warn("Acesso negado. Pessoa ID {} não pertence ao usuário ID {}", pessoaId, usuario.getId());
            throw new PessoaNaoPertenceAoUsuarioException(pessoaId);
        }
    }

    /**
     * Valida se o ID informado é o do próprio usuário autenticado.
     *
     * @param usuarioId ID do usuário alvo da operação
     * @throws AccessDeniedException se o ID for de outro usuário
     */
    @Transactional(readOnly = true)
    public void validarProprioUsuario(Long usuarioId) {
        Usuario usuario = usuarioAutenticado();

        if (!usuario.getId().equals(usuarioId)) {
            log.warn("Acesso negado. Usuário ID {} tentou acessar o usuário ID {}", usuario.getId(), usuarioId);
            throw new AccessDeniedException("Acesso negado");
        }
    }
}
