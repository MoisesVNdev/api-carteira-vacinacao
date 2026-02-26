// k6/scenarios/registro.scenario.js
// Cenário de teste — Registros de Vacinação (🔴 ENDPOINT MAIS CRÍTICO).
// Inclui: calendário vacinal, histórico e CRUD de registros.
import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { gerarRegistroVacina } from '../helpers/data.js';

/**
 * Testa os endpoints de registro de vacinação, calendário e histórico.
 * Requer: pelo menos 1 pessoa e vacinas/esquemas populados (Flyway V5/V6).
 *
 * @param {object} headers   — Headers com Authorization Bearer
 * @param {string} baseUrl   — URL base da API
 * @param {number[]} pessoaIds — Array de IDs de pessoas disponíveis
 * @param {number[]} vacinaIds — Array de IDs de vacinas (para buscar esquemas)
 */
export function registroScenario(headers, baseUrl, pessoaIds, vacinaIds) {
    // Cada VU escolhe uma pessoa aleatória
    const pessoaId = pessoaIds[Math.floor(Math.random() * pessoaIds.length)];

    group('Calendário Vacinal', () => {
        const res = http.get(
            `${baseUrl}/api/v1/registros-vacina/pessoas/${pessoaId}/calendario`,
            { headers }
        );

        check(res, {
            'calendário: status 200': (r) => r.status === 200,
            'calendário: body é array': (r) => Array.isArray(r.json()),
            'calendário: itens com status': (r) =>
                Array.isArray(r.json()) && r.json().every((i) => i.status !== undefined),
        });

        sleep(Math.random() * 2 + 1);
    });

    group('Histórico de Vacinação', () => {
        const res = http.get(
            `${baseUrl}/api/v1/registros-vacina/pessoas/${pessoaId}/historico`,
            { headers }
        );

        check(res, {
            'histórico: status 200': (r) => r.status === 200,
            'histórico: body é array': (r) => Array.isArray(r.json()),
        });

        sleep(Math.random() * 1 + 0.5);
    });

    // Busca o primeiro esquema vacinal disponível para a vacina
    let esquemaVacinalId = null;

    group('Registro — Obter Esquema Vacinal', () => {
        const vacinaId = vacinaIds.length > 0
            ? vacinaIds[Math.floor(Math.random() * vacinaIds.length)]
            : 1;

        const res = http.get(
            `${baseUrl}/api/v1/vacinas/${vacinaId}/esquema`,
            { headers }
        );

        if (res.status === 200 && Array.isArray(res.json()) && res.json().length > 0) {
            esquemaVacinalId = res.json()[0].id;
        }

        sleep(Math.random() * 1 + 0.5);
    });

    if (esquemaVacinalId) {
        let registroId = null;

        group('Registro — Registrar Dose', () => {
            const payload = gerarRegistroVacina(pessoaId, esquemaVacinalId);

            const res = http.post(
                `${baseUrl}/api/v1/registros-vacina/registros`,
                JSON.stringify(payload),
                { headers }
            );

            check(res, {
                'registrar dose: status 201': (r) => r.status === 201,
                'registrar dose: id retornado': (r) => !!r.json('id'),
            });

            if (res.status === 201) {
                registroId = res.json('id');
            }

            sleep(Math.random() * 2 + 1);
        });

        if (registroId) {
            group('Registro — Deletar Dose', () => {
                const res = http.del(
                    `${baseUrl}/api/v1/registros-vacina/registros/${registroId}`,
                    null,
                    { headers }
                );

                check(res, {
                    'deletar dose: status 204': (r) => r.status === 204,
                });

                sleep(Math.random() * 1 + 0.5);
            });
        }
    }
}
