package com.moisesvn.carteira_vacinacao_api.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Retorna resposta padronizada quando o usuário não está autenticado.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String json = "{" +
                "\"timestamp\":\"" + LocalDateTime.now() + "\"," +
                "\"status\":" + HttpServletResponse.SC_UNAUTHORIZED + "," +
                "\"erro\":\"Unauthorized\"," +
                "\"mensagem\":\"Token ausente ou invalido. Envie Authorization: Bearer <token>.\"," +
                "\"caminho\":\"" + escapeJson(request.getRequestURI()) + "\"" +
                "}";

        response.getWriter().write(json);
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
