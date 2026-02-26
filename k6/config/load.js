// k6/config/load.js
// Configuração do Load Test — Rampa até 100 VUs, 5 minutos total.
// Objetivo: simular carga realista de uso com rampa progressiva.

export const loadOptions = {
    stages: [
        { duration: '1m', target: 25 },   // aquecimento
        { duration: '3m', target: 100 },   // carga sustentada
        { duration: '1m', target: 0 },     // resfriamento
    ],
    thresholds: {
        http_req_duration: ['p(95)<500', 'p(99)<1500'],
        http_req_failed: ['rate<0.01'],
        checks: ['rate>0.99'],
    },
};
