package com.moisesvn.carteira_vacinacao_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT que intercepta requisições HTTP e valida o token Bearer.
 * Estende OncePerRequestFilter para garantir execução única por requisição.
 * 
 * @see OncePerRequestFilter
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTH_HEADER   = "Authorization";

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Executa a validação do token JWT a cada requisição.
     * Os parâmetros são garantidos como não-nulos pelo contrato do OncePerRequestFilter.
     * 
     * @param request  requisição HTTP
     * @param response resposta HTTP
     * @param filterChain cadeia de filtros
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader(AUTH_HEADER);

        // Se não há header ou não começa com "Bearer ", passa adiante sem autenticar
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            String email = jwtService.extractUsername(token);

            // Autentica apenas se o contexto ainda estiver vazio (evita reprocessamento)
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.debug("Usuário autenticado via JWT: {}", email);
                }
            }
        } catch (Exception ex) {
            // Apenas log de debug para falhas, não warn
            // Requisições sem token é normal e não deve gerar warnings
            log.debug("Token inválido ou expirado: {}", ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
