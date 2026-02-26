/**
 * Smoke Test — Validação completa dos scripts k6
 *
 * Cenário: 1 VU / 1 minuto
 * Objetivo: verificar que a API responde e os scripts funcionam corretamente.
 * Rode SEMPRE antes de qualquer load ou stress test.
 *
 * Pré-condições:
 *  - API rodando (Docker Compose up)
 *  - Pelo menos 1 usuário com email/senha nos __ENV
 *  - Pelo menos 1 pessoa cadastrada vinculada ao usuário
 *  - Tabelas V5/V6 populadas via Flyway (vacinas + esquemas)
 */

import http from 'k6/http';
import { check, fail, sleep } from 'k6';
import { htmlReport } from 'https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.1/index.js';

import { smokeOptions } from './config/smoke.js';
import { obterToken, authHeaders } from './helpers/auth.js';
import { authScenario } from './scenarios/auth.scenario.js';
import { pessoaScenario } from './scenarios/pessoa.scenario.js';
import { vacinaScenario } from './scenarios/vacina.scenario.js';
import { registroScenario } from './scenarios/registro.scenario.js';

// ─── Variáveis de Ambiente ────────────────────────────────────────────────────
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const EMAIL    = __ENV.TEST_USER_EMAIL || 'teste@vacinacao.dev';
const PASSWORD = __ENV.TEST_USER_PASSWORD || 'Senha@123';

// ─── Configuração do Smoke Test ───────────────────────────────────────────────
export const options = smokeOptions;

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

    // Verificar massa de dados — vacinas
    const vacinasRes = http.get(`${BASE_URL}/api/v1/vacinas`, { headers });
    const vacinaIds = (vacinasRes.status === 200 && Array.isArray(vacinasRes.json()))
        ? vacinasRes.json().map((v) => v.id)
        : [1];

    return { token, pessoaIds, vacinaIds };
}

// ─── Função Principal das VUs ─────────────────────────────────────────────────
export default function (data) {
    const headers = authHeaders(data.token);

    authScenario(headers, BASE_URL);
    sleep(Math.random() * 2 + 1);

    pessoaScenario(headers, BASE_URL);
    sleep(Math.random() * 2 + 1);

    vacinaScenario(headers, BASE_URL);
    sleep(Math.random() * 2 + 1);

    registroScenario(headers, BASE_URL, data.pessoaIds, data.vacinaIds);
}

// ─── Relatório HTML ───────────────────────────────────────────────────────────
export function handleSummary(data) {
    return {
        'k6/results/summary.html': htmlReport(data),
        stdout: textSummary(data, { indent: ' ', enableColors: true }),
    };
}
