/**
 * Stress Test — Ponto de Ruptura
 *
 * Cenário: Rampa 2min→50, 2min→100, 2min→200, 2min→300, 3min sustentado, 2min→0
 * Objetivo: encontrar o ponto de ruptura da API sob pressão.
 * Foco nos endpoints 🔴 críticos: calendário vacinal, registrar dose, listar pessoas.
 *
 * Pré-condições:
 *  - API rodando (Docker Compose up)
 *  - Pelo menos 1 usuário com email/senha nos __ENV
 *  - Pelo menos 1 pessoa cadastrada vinculada ao usuário
 *  - Tabelas V5/V6 populadas via Flyway (vacinas + esquemas)
 *  - Smoke test e Load test executados com sucesso previamente
 */

import http from 'k6/http';
import { check, fail, group, sleep } from 'k6';
import { htmlReport } from 'https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.1/index.js';

import { stressOptions } from './config/stress.js';
import { obterToken, authHeaders } from './helpers/auth.js';
import { gerarRegistroVacina } from './helpers/data.js';

// ─── Variáveis de Ambiente ────────────────────────────────────────────────────
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const EMAIL    = __ENV.TEST_USER_EMAIL || 'teste@vacinacao.dev';
const PASSWORD = __ENV.TEST_USER_PASSWORD || 'Senha@123';

// ─── Configuração do Stress Test ──────────────────────────────────────────────
export const options = stressOptions;

// ─── Setup: Login, Verificação de Pré-condições ──────────────────────────────
export function setup() {
    // Verificar se a API está de pé
    const health = http.get(`${BASE_URL}/actuator/health`);
    check(health, { '[pré-condição] API UP': (r) => r.status === 200 })
        || fail('[pré-condição] API não respondeu. Verifique o Docker Compose.');

    // Login e obtenção do token
    const token = obterToken(EMAIL, PASSWORD);
    const headers = authHeaders(token);

    // Verificar massa de dados — pessoas
    const pessoasRes = http.get(`${BASE_URL}/api/v1/pessoas`, { headers });
    check(pessoasRes, {
        '[pré-condição] pessoas no banco': (r) => r.status === 200,
        '[pré-condição] pelo menos 1 pessoa': (r) =>
            Array.isArray(r.json()) && r.json().length > 0,
    }) || fail('[pré-condição] Nenhuma pessoa encontrada. Popule o banco via seed.');

    const pessoaIds = pessoasRes.json().map((p) => p.id);

    // Verificar massa de dados — vacinas e esquemas
    const vacinasRes = http.get(`${BASE_URL}/api/v1/vacinas`, { headers });
    const vacinaIds = (vacinasRes.status === 200 && Array.isArray(vacinasRes.json()))
        ? vacinasRes.json().map((v) => v.id)
        : [1];

    // Pré-carregar um esquemaVacinalId para uso no stress de escrita
    let esquemaVacinalId = 1;
    if (vacinaIds.length > 0) {
        const esquemaRes = http.get(
            `${BASE_URL}/api/v1/vacinas/${vacinaIds[0]}/esquema`,
            { headers }
        );
        if (esquemaRes.status === 200 && Array.isArray(esquemaRes.json()) && esquemaRes.json().length > 0) {
            esquemaVacinalId = esquemaRes.json()[0].id;
        }
    }

    return { token, pessoaIds, vacinaIds, esquemaVacinalId };
}

// ─── Função Principal — Foco nos endpoints 🔴 críticos ───────────────────────
export default function (data) {
    const headers = authHeaders(data.token);
    const pessoaId = data.pessoaIds[Math.floor(Math.random() * data.pessoaIds.length)];

    // 🔴 Endpoint crítico: Calendário Vacinal (consulta complexa multi-tabela)
    group('Stress — Calendário Vacinal', () => {
        const res = http.get(
            `${BASE_URL}/api/v1/registros-vacina/pessoas/${pessoaId}/calendario`,
            { headers }
        );

        check(res, {
            'calendário: status 200': (r) => r.status === 200,
            'calendário: body é array': (r) => Array.isArray(r.json()),
        });

        sleep(Math.random() * 2 + 1);
    });

    // 🔴 Endpoint crítico: Registrar Dose (escrita com validação de hierarquia)
    group('Stress — Registrar Dose', () => {
        const payload = gerarRegistroVacina(pessoaId, data.esquemaVacinalId);

        const res = http.post(
            `${BASE_URL}/api/v1/registros-vacina/registros`,
            JSON.stringify(payload),
            { headers }
        );

        check(res, {
            'registrar dose: status 201': (r) => r.status === 201,
        });

        // Limpa o registro criado para não poluir o banco durante o stress
        if (res.status === 201 && res.json('id')) {
            http.del(
                `${BASE_URL}/api/v1/registros-vacina/registros/${res.json('id')}`,
                null,
                { headers }
            );
        }

        sleep(Math.random() * 2 + 1);
    });

    // 🟡 Endpoint médio: Listar Pessoas (listagem sem paginação)
    group('Stress — Listar Pessoas', () => {
        const res = http.get(`${BASE_URL}/api/v1/pessoas`, { headers });

        check(res, {
            'listar pessoas: status 200': (r) => r.status === 200,
            'listar pessoas: body é array': (r) => Array.isArray(r.json()),
        });

        sleep(Math.random() * 2 + 1);
    });
}

// ─── Relatório HTML ───────────────────────────────────────────────────────────
export function handleSummary(data) {
    return {
        'k6/results/summary.html': htmlReport(data),
        stdout: textSummary(data, { indent: ' ', enableColors: true }),
    };
}
