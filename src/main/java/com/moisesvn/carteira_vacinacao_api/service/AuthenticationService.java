package com.moisesvn.carteira_vacinacao_api.service;

import com.moisesvn.carteira_vacinacao_api.dto.LoginRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.LoginResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.RegisterRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.UsuarioResponseDTO;
import com.moisesvn.carteira_vacinacao_api.exception.InvalidCredentialsException;
import com.moisesvn.carteira_vacinacao_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    /**
     * Autentica o usuário e retorna um JWT.
     * Delega a validação de credenciais ao AuthenticationManager do Spring Security,
     * que por sua vez usa o DaoAuthenticationProvider + BCrypt configurados em
     * SecurityConfiguration.
     */
    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
            );
        } catch (BadCredentialsException ex) {
            // Log sem expor a senha; mensagem genérica ao cliente
            log.warn("Tentativa de login com credenciais inválidas para: {}", request.getEmail());
            throw new InvalidCredentialsException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);

        log.info("Login bem-sucedido para: {}", request.getEmail());

        return new LoginResponseDTO(
                token,
                "Bearer",
                jwtService.getExpirationTimestamp()
        );
    }

    /**
     * Registra um novo usuário reutilizando a lógica já existente no UsuarioService.
     * O RegisterRequestDTO possui validações mais rígidas de senha do que o
     * UsuarioRequestDTO genérico.
     */
    public UsuarioResponseDTO registrar(RegisterRequestDTO request) {
        // Reutiliza UsuarioService para manter DRY e Single Responsibility
        com.moisesvn.carteira_vacinacao_api.dto.UsuarioRequestDTO dto =
                new com.moisesvn.carteira_vacinacao_api.dto.UsuarioRequestDTO();
        dto.setNomeCompleto(request.getNomeCompleto());
        dto.setEmail(request.getEmail());
        dto.setSenha(request.getSenha());

        return usuarioService.criar(dto);
    }
}