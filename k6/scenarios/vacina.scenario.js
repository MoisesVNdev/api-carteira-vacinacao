// k6/scenarios/vacina.scenario.js
// Cenário de teste — Catálogo de Vacinas e Esquemas Vacinais (leitura).
import http from 'k6/http';
import { check, group, sleep } from 'k6';

/**
 * Testa os endpoints de consulta do catálogo de vacinas.
 * Os dados são pré-populados via Flyway (V5 e V6).
 *
 * @param {object} headers — Headers com Authorization Bearer
 * @param {string} baseUrl — URL base da API
 */
export function vacinaScenario(headers, baseUrl) {
    let vacinaId = null;

    group('Vacina — Listar Catálogo', () => {
        const res = http.get(`${baseUrl}/api/v1/vacinas`, { headers });

        check(res, {
            'listar vacinas: status 200': (r) => r.status === 200,
            'listar vacinas: body é array': (r) => Array.isArray(r.json()),
            'listar vacinas: array não vazio': (r) => r.json().length > 0,
        });

        if (res.status === 200 && Array.isArray(res.json()) && res.json().length > 0) {
            vacinaId = res.json()[0].id;
        }

        sleep(Math.random() * 2 + 1);
    });

    group('Vacina — Buscar por ID', () => {
        const id = vacinaId || 1;
        const res = http.get(`${baseUrl}/api/v1/vacinas/${id}`, { headers });

        check(res, {
            'buscar vacina: status 200': (r) => r.status === 200,
            'buscar vacina: id presente': (r) => !!r.json('id'),
        });

        sleep(Math.random() * 2 + 1);
    });

    group('Vacina — Esquema Vacinal', () => {
        const id = vacinaId || 1;
        const res = http.get(`${baseUrl}/api/v1/vacinas/${id}/esquema`, { headers });

        check(res, {
            'esquema vacinal: status 200': (r) => r.status === 200,
            'esquema vacinal: body é array': (r) => Array.isArray(r.json()),
        });

        sleep(Math.random() * 2 + 1);
    });
}
