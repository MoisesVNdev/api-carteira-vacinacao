/**
 * Soak Test — Teste de Estabilidade
 *
 * Cenário: 20 VUs constantes por 15 minutos
 * Objetivo: detectar memory leaks, degradação gradual de performance
 *           e problemas de estabilidade que só aparecem com uso prolongado.
 *
 * Pré-condições:
 *  - API rodando (Docker Compose up)
 *  - Pelo menos 1 usuário com email/senha nos __ENV
 *  - Pelo menos 1 pessoa cadastrada vinculada ao usuário
 *  - Tabelas V5/V6 populadas via Flyway (vacinas + esquemas)
 *  - Smoke test executado com sucesso previamente
 */

import http from 'k6/http';
import { check, fail, sleep } from 'k6';
import { htmlReport } from 'https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.1/index.js';

import { obterToken, authHeaders } from './helpers/auth.js';
import { pessoaScenario } from './scenarios/pessoa.scenario.js';
import { vacinaScenario } from './scenarios/vacina.scenario.js';
import { registroScenario } from './scenarios/registro.scenario.js';

// ─── Variáveis de Ambiente ────────────────────────────────────────────────────
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const EMAIL    = __ENV.TEST_USER_EMAIL || 'teste@vacinacao.dev';
const PASSWORD = __ENV.TEST_USER_PASSWORD || 'Senha@123';

// ─── Configuração do Soak Test ────────────────────────────────────────────────
// NOTA: Valores ajustados para o hardware disponível:
// CPU: Ryzen 5 3600X (6c/12t), RAM: 16 GB (~5.5 GB livres)
// Container app: 1.5 GB de RAM | Container PG: 1 GB
// VUs e duração reduzidos de 40 VUs/30min → 20 VUs/15min.
export const options = {
    stages: [
        { duration: '1m', target: 20 },     // rampa de aquecimento
        { duration: '13m', target: 20 },    // carga constante sustentada
        { duration: '1m', target: 0 },      // resfriamento
    ],
    thresholds: {
        http_req_duration: ['p(95)<600'],
        http_req_failed: ['rate<0.005'],
        checks: ['rate>0.99'],
    },
};

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

    pessoaScenario(headers, BASE_URL);
    sleep(Math.random() * 2 + 1);

    vacinaScenario(headers, BASE_URL);
    sleep(Math.random() * 2 + 1);

    registroScenario(headers, BASE_URL, data.pessoaIds, data.vacinaIds);
    sleep(Math.random() * 2 + 1);
}

// ─── Relatório HTML ───────────────────────────────────────────────────────────
export function handleSummary(data) {
    return {
        'k6/results/summary.html': htmlReport(data),
        stdout: textSummary(data, { indent: ' ', enableColors: true }),
    };
}
