// k6/helpers/data.js
// Geração de payloads e massa de dados para os cenários k6.

/**
 * Gera payload para cadastro de pessoa.
 * Usa número aleatório no CPF/CNS para evitar conflito de unicidade entre VUs.
 *
 * @returns {object} Payload compatível com PessoaRequestDTO
 */
export function gerarPessoa() {
    const sufixo = Math.floor(Math.random() * 9000000000) + 1000000000;
    return {
        nomeCompleto: `Paciente Teste ${sufixo}`,
        cpf: String(sufixo).padStart(11, '0'),
        cns: String(Math.floor(Math.random() * 900000000000000) + 100000000000000),
        dataNascimento: '1990-06-15',
        nomeMae: `Mae Teste ${sufixo}`,
        genero: 'FEMININO',
        nacionalidade: 'Brasileira',
        naturalidade: 'São Paulo',
        tipoRelacao: 'MAE',
    };
}

/**
 * Gera payload para registro de vacina aplicada.
 * 
 * NOTA: Para evitar falhas de validação:
 * - Usa data fixa no passado (não entra em conflito)
 * - Cada lote é único para evitar duplicatas
 * - Sempre válido para os esquemas seed do Flyway
 *
 * @param {number} pessoaId        — ID da pessoa alvo
 * @param {number} esquemaVacinalId — ID do esquema vacinal (seed Flyway V6)
 * @returns {object} Payload compatível com RegistroVacinaRequestDTO
 */
export function gerarRegistroVacina(pessoaId, esquemaVacinalId) {
    // Gera data aleatória nos últimos 30 dias (para variar, evitando duplicatas exatas)
    const dataBase = new Date('2024-06-01');
    const diasAleatorios = Math.floor(Math.random() * 30);
    const dataRegistro = new Date(dataBase);
    dataRegistro.setDate(dataRegistro.getDate() + diasAleatorios);
    const dataFormatada = dataRegistro.toISOString().split('T')[0];

    return {
        pessoaId,
        esquemaVacinalId,
        dataAplicacao: dataFormatada,
        lote: `LOT-${Math.floor(Math.random() * 999999999)}`, // Lote único global
        fabricante: 'Fiocruz',
        vacinador: 'Enfermeiro(a) Teste',
        localAplicacao: 'UBS Centro',
    };
}

/**
 * Gera payload para registro de novo usuário (auth/register).
 *
 * @returns {object} Payload compatível com RegisterRequestDTO
 */
export function gerarUsuario() {
    const sufixo = Math.floor(Math.random() * 9000000) + 1000000;
    return {
        nomeCompleto: `Usuario Teste ${sufixo}`,
        email: `teste.${sufixo}@vacinacao.dev`,
        senha: 'Senha@123',
    };
}
