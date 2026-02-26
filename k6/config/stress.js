// k6/config/stress.js
// Configuração do Stress Test — Rampa até 1000 VUs, ~15 minutos.
// Objetivo: encontrar o ponto de ruptura da API.

// NOTA: Valores ajustados para o hardware disponível:
// CPU: Ryzen 5 3600X (6c/12t), RAM: 16 GB (~5.5 GB livres)
// Container app: 1.5 GB de RAM, 4 CPUs | Container PG: 1 GB, 2 CPUs
// VUs máx reduzido de 1000→300 para evitar colapsar a máquina.
export const stressOptions = {
    stages: [
        { duration: '2m', target: 50 },     // base
        { duration: '2m', target: 100 },    // pressão
        { duration: '2m', target: 200 },    // estresse
        { duration: '2m', target: 300 },    // ruptura
        { duration: '3m', target: 300 },    // sustentação no pico
        { duration: '2m', target: 0 },      // recuperação
    ],
    thresholds: {
        // No stress test o SLA é mais tolerante — buscamos o limite
        http_req_duration: ['p(95)<2000'],
        http_req_failed: ['rate<0.05'],
    },
};
