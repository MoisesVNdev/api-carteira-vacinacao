// k6/scenarios/auth.scenario.js
// Cenário de teste — Autenticação (login e registro).
import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { gerarUsuario } from '../helpers/data.js';

/**
 * Executa os cenários de autenticação.
 *
 * @param {object} headers — Headers com Authorization Bearer
 * @param {string} baseUrl — URL base da API
 */
export function authScenario(headers, baseUrl) {

    group('Auth — Login', () => {
        const email = __ENV.TEST_USER_EMAIL || 'teste@vacinacao.dev';
        const senha = __ENV.TEST_USER_PASSWORD || 'Senha@123';

        const res = http.post(
            `${baseUrl}/api/v1/auth/login`,
            JSON.stringify({ email, senha }),
            { headers: { 'Content-Type': 'application/json' } }
        );

        check(res, {
            'login: status 200': (r) => r.status === 200,
            'login: token presente': (r) => !!r.json('token'),
            'login: tipo Bearer': (r) => r.json('tipo') === 'Bearer',
        });

        sleep(Math.random() * 2 + 1);
    });

    group('Auth — Registro', () => {
        const novoUsuario = gerarUsuario();

        const res = http.post(
            `${baseUrl}/api/v1/auth/register`,
            JSON.stringify(novoUsuario),
            { headers: { 'Content-Type': 'application/json' } }
        );

        check(res, {
            'registro: status 201': (r) => r.status === 201,
            'registro: body não vazio': (r) => r.body.length > 0,
        });

        sleep(Math.random() * 2 + 1);
    });
}
