// k6/config/smoke.js
// Configuração do Smoke Test — 1 VU, 1 minuto.
// Objetivo: validar que o script funciona e a API responde corretamente.

export const smokeOptions = {
    vus: 1,
    duration: '1m',
    thresholds: {
        http_req_duration: ['p(95)<800'],
        http_req_failed: ['rate<0.01'],
        checks: ['rate>0.99'],
    },
};
