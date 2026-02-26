// k6/scenarios/pessoa.scenario.js
// Cenário de teste — CRUD completo de Pessoas.
import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { gerarPessoa } from '../helpers/data.js';

/**
 * Executa o CRUD completo de Pessoa.
 * O ID da pessoa criada é capturado do POST e reusado nos demais verbos.
 *
 * @param {object} headers — Headers com Authorization Bearer
 * @param {string} baseUrl — URL base da API
 */
export function pessoaScenario(headers, baseUrl) {
    let pessoaId = null;

    group('Pessoa — Cadastrar', () => {
        const payload = gerarPessoa();

        const res = http.post(
            `${baseUrl}/api/v1/pessoas`,
            JSON.stringify(payload),
            { headers }
        );

        check(res, {
            'criar pessoa: status 201': (r) => r.status === 201,
            'criar pessoa: id retornado': (r) => !!r.json('id'),
        });

        if (res.status === 201) {
            pessoaId = res.json('id');
        }

        sleep(Math.random() * 2 + 1);
    });

    group('Pessoa — Listar Todas', () => {
        const res = http.get(`${baseUrl}/api/v1/pessoas`, { headers });

        check(res, {
            'listar pessoas: status 200': (r) => r.status === 200,
            'listar pessoas: body é array': (r) => Array.isArray(r.json()),
            'listar pessoas: array não vazio': (r) => r.json().length > 0,
        });

        sleep(Math.random() * 2 + 1);
    });

    if (pessoaId) {
        group('Pessoa — Buscar por ID', () => {
            const res = http.get(`${baseUrl}/api/v1/pessoas/${pessoaId}`, { headers });

            check(res, {
                'buscar pessoa: status 200': (r) => r.status === 200,
                'buscar pessoa: id correto': (r) => r.json('id') === pessoaId,
            });

            sleep(Math.random() * 2 + 1);
        });

        group('Pessoa — Atualizar', () => {
            const payload = gerarPessoa();
            payload.nomeCompleto = `Pessoa Atualizada ${Date.now()}`;

            const res = http.put(
                `${baseUrl}/api/v1/pessoas/${pessoaId}`,
                JSON.stringify(payload),
                { headers }
            );

            check(res, {
                'atualizar pessoa: status 200': (r) => r.status === 200,
            });

            sleep(Math.random() * 2 + 1);
        });

        group('Pessoa — Deletar', () => {
            const res = http.del(`${baseUrl}/api/v1/pessoas/${pessoaId}`, null, { headers });

            check(res, {
                'deletar pessoa: status 204': (r) => r.status === 204,
            });

            sleep(Math.random() * 1 + 0.5);
        });
    }
}
