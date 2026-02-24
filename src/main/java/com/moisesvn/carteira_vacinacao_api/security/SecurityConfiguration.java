package com.moisesvn.carteira_vacinacao_api.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuração de segurança Spring Security com JWT.
 * 
 * Fluxo:
 * 1. JwtAuthenticationFilter intercepta requisição e extrai token JWT
 * 2. Valida token e popula SecurityContext com autenticação
 * 3. Requisição é autorizada/negada baseado nas regras em authorizeHttpRequests()
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("🔐 Configurando SecurityFilterChain");
        
        http
            // Desabilita CSRF para APIs stateless
            .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Define regras de autorização por endpoint
            .authorizeHttpRequests(authz -> authz
                // Endpoints públicos
                .requestMatchers("/health", "/actuator/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                // Swagger UI e documentação OpenAPI
                .requestMatchers(
                    "/api-docs/**",
                    "/api-docs.yaml",
                    "/v3/api-docs/**",
                    "/v3/api-docs.yaml",
                    "/swagger-ui.html",
                    "/swagger-ui/**"
                ).permitAll()
                
                // Qualquer outra requisição requer autenticação
                .anyRequest().authenticated()
            )
            // Retorna 401 com mensagem clara quando não autenticado
            .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
            
            // Configura provedor de autenticação ANTES de adicionar o filtro
            .authenticationProvider(authenticationProvider())
            
            // Adiciona filtro JWT ANTES do filtro padrão
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        log.info("✅ SecurityFilterChain configurado com sucesso");
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Expõe o AuthenticationManager para ser injetado no AuthenticationService.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}