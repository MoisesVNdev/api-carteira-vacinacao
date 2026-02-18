package com.moisesvn.carteira_vacinacao_api.security;

import com.moisesvn.carteira_vacinacao_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Carrega o usuário pelo e-mail (usado como "username" no sistema).
     * As authorities são fixas em ROLE_USER na V1; roles dinâmicas podem ser
     * introduzidas adicionando um campo à entidade Usuario no futuro.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                .map(usuario -> new User(
                        usuario.getEmail(),
                        usuario.getSenha(),
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }
}