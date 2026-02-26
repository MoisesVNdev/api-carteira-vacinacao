// k6/scenarios/alergia.scenario.js
// Cenário de teste — Catálogo de Alergias e Vínculos Pessoa-Alergia.
import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { gerarPessoa } from '../helpers/data.js';

/**
 * Testa os endpoints do catálogo de alergias e vínculos pessoa-alergia.
 * Cria uma pessoa temporária para vincular/desvincular alergias.
 *
 * @param {object} headers — Headers com Authorization Bearer
 * @param {string} baseUrl — URL base da API
 */
export function alergiaScenario(headers, baseUrl) {

    group('Alergia — Listar Catálogo', () => {
        const res = http.get(`${baseUrl}/api/v1/alergias`, { headers });

        check(res, {
            'listar alergias: status 200': (r) => r.status === 200,
            'listar alergias: body é array': (r) => Array.isArray(r.json()),
        });

        sleep(Math.random() * 2 + 1);
    });

    group('Alergia — Buscar por ID', () => {
        const res = http.get(`${baseUrl}/api/v1/alergias/1`, { headers });

        check(res, {
            'buscar alergia: status 200': (r) => r.status === 200,
            'buscar alergia: id presente': (r) => !!r.json('id'),
        });

        sleep(Math.random() * 2 + 1);
    });

    group('Alergia — Busca em Lote', () => {
        const res = http.get(`${baseUrl}/api/v1/alergias/lote?ids=1,2,3`, { headers });

        check(res, {
            'lote alergias: status 200': (r) => r.status === 200,
            'lote alergias: 3 itens': (r) => Array.isArray(r.json()) && r.json().length === 3,
        });

        sleep(Math.random() * 2 + 1);
    });

    // --- Vínculo pessoa-alergia ---
    // Cria pessoa temporária para testar vínculos
    let pessoaId = null;

    group('Alergia — Criar Pessoa para Vínculo', () => {
        const payload = gerarPessoa();
        const res = http.post(
            `${baseUrl}/api/v1/pessoas`,
            JSON.stringify(payload),
            { headers }
        );

        if (res.status === 201) {
            pessoaId = res.json('id');
        }

        sleep(Math.random() * 1 + 0.5);
    });

    if (pessoaId) {
        group('Alergia — Vincular à Pessoa', () => {
            const res = http.post(
                `${baseUrl}/api/v1/pessoas/${pessoaId}/alergias`,
                JSON.stringify({ alergiaId: 1, observacao: 'Teste k6' }),
                { headers }
            );

            check(res, {
                'vincular alergia: status 201': (r) => r.status === 201,
                'vincular alergia: body não vazio': (r) => r.body.length > 0,
            });

            sleep(Math.random() * 2 + 1);
        });

        group('Alergia — Listar Vínculos da Pessoa', () => {
            const res = http.get(
                `${baseUrl}/api/v1/pessoas/${pessoaId}/alergias`,
                { headers }
            );

            check(res, {
                'listar vínculos: status 200': (r) => r.status === 200,
                'listar vínculos: body é array': (r) => Array.isArray(r.json()),
            });

            sleep(Math.random() * 2 + 1);
        });

        group('Alergia — Atualizar Observação', () => {
            const res = http.patch(
                `${baseUrl}/api/v1/pessoas/${pessoaId}/alergias/1/observacao`,
                JSON.stringify({ observacao: 'Observação atualizada via k6' }),
                { headers }
            );

            check(res, {
                'atualizar observação: status 200': (r) => r.status === 200,
            });

            sleep(Math.random() * 2 + 1);
        });

        group('Alergia — Remover Vínculo', () => {
            const res = http.del(
                `${baseUrl}/api/v1/pessoas/${pessoaId}/alergias/1`,
                null,
                { headers }
            );

            check(res, {
                'remover vínculo: status 204': (r) => r.status === 204,
            });

            sleep(Math.random() * 1 + 0.5);
        });

        // Limpa a pessoa temporária
        group('Alergia — Limpar Pessoa Temporária', () => {
            http.del(`${baseUrl}/api/v1/pessoas/${pessoaId}`, null, { headers });
            sleep(Math.random() * 0.5 + 0.3);
        });
    }
}
