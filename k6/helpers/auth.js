// k6/helpers/auth.js
// Helper centralizado de autenticação JWT para todos os scripts k6.
import http from 'k6/http';
import { check, fail } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

/**
 * Realiza login na API e retorna o token JWT.
 * Usar no setup() de cada script.
 *
 * @param {string} email    — Email do usuário de teste
 * @param {string} password — Senha do usuário de teste
 * @returns {string} Token JWT válido
 */
export function obterToken(email, password) {
    const res = http.post(
        `${BASE_URL}/api/v1/auth/login`,
        JSON.stringify({ email, senha: password }),
        { headers: { 'Content-Type': 'application/json' } }
    );

    const ok = check(res, {
        '[auth] status 200': (r) => r.status === 200,
        '[auth] token retornado': (r) => !!r.json('token'),
    });

    if (!ok) {
        fail(`[auth] Login falhou: ${res.status} → ${res.body}`);
    }

    return res.json('token');
}

/**
 * Monta o header padrão com Bearer token.
 *
 * @param {string} token — Token JWT
 * @returns {object} Headers HTTP com Authorization e Content-Type
 */
export function authHeaders(token) {
    return {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
    };
}
