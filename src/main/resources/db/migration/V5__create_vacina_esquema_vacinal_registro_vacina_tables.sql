-- Migration Flyway v5
-- Criação das tabelas vacina, esquema_vacinal e registro_vacina

-- Tabela vacina (catálogo de vacinas)
CREATE TABLE vacina (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL UNIQUE,
    descricao TEXT,
    doenca_evitada VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Comentário descritivo da tabela
COMMENT ON TABLE vacina IS 'Catálogo de vacinas do PNI (Programa Nacional de Imunizações) - somente leitura via API';
COMMENT ON COLUMN vacina.nome IS 'Nome da vacina (único)';
COMMENT ON COLUMN vacina.descricao IS 'Descrição detalhada da vacina';
COMMENT ON COLUMN vacina.doenca_evitada IS 'Doença(s) que a vacina previne';
COMMENT ON COLUMN vacina.created_at IS 'Data de criação do registro';
COMMENT ON COLUMN vacina.updated_at IS 'Data da última atualização';

-- Índices para melhor performance
CREATE INDEX idx_vacina_nome ON vacina(nome);

-- Tabela esquema_vacinal (definição do esquema de doses para cada vacina)
CREATE TABLE esquema_vacinal (
    id SERIAL PRIMARY KEY,
    vacina_id BIGINT NOT NULL,
    descricao_dose VARCHAR(50) NOT NULL,
    idade_recomendada_meses INT NOT NULL,
    intervalo_minimo_dias INT,
    FOREIGN KEY (vacina_id) REFERENCES vacina(id) ON DELETE CASCADE,
    UNIQUE (vacina_id, descricao_dose)
);

-- Comentário descritivo da tabela
COMMENT ON TABLE esquema_vacinal IS 'Esquema de doses de cada vacina do calendário vacinal PNI';
COMMENT ON COLUMN esquema_vacinal.descricao_dose IS 'Identificação da dose (ex: 1ª Dose, 2ª Dose, 1º Reforço)';
COMMENT ON COLUMN esquema_vacinal.idade_recomendada_meses IS 'Idade recomendada para aplicação em meses a partir do nascimento';
COMMENT ON COLUMN esquema_vacinal.intervalo_minimo_dias IS 'Intervalo mínimo entre doses (opcional)';

-- Índices para melhor performance
CREATE INDEX idx_esquema_vacinal_vacina_id ON esquema_vacinal(vacina_id);
CREATE INDEX idx_esquema_vacinal_idade ON esquema_vacinal(idade_recomendada_meses);

-- Tabela registro_vacina (histórico de vacinação de cada pessoa)
CREATE TABLE registro_vacina (
    id SERIAL PRIMARY KEY,
    pessoa_id BIGINT NOT NULL,
    esquema_vacinal_id BIGINT NOT NULL,
    data_aplicacao DATE NOT NULL,
    lote VARCHAR(50) NOT NULL,
    fabricante VARCHAR(100),
    vacinador VARCHAR(150),
    local_aplicacao VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE,
    FOREIGN KEY (esquema_vacinal_id) REFERENCES esquema_vacinal(id) ON DELETE RESTRICT,
    UNIQUE (pessoa_id, esquema_vacinal_id)
);

-- Comentário descritivo da tabela
COMMENT ON TABLE registro_vacina IS 'Histórico de vacinação de cada pessoa (doses aplicadas)';
COMMENT ON COLUMN registro_vacina.pessoa_id IS 'Referência à pessoa que recebeu a vacina';
COMMENT ON COLUMN registro_vacina.esquema_vacinal_id IS 'Referência ao esquema vacinal (vacina + dose)';
COMMENT ON COLUMN registro_vacina.data_aplicacao IS 'Data em que a dose foi aplicada';
COMMENT ON COLUMN registro_vacina.lote IS 'Lote da vacina aplicada';
COMMENT ON COLUMN registro_vacina.fabricante IS 'Fabricante da vacina';
COMMENT ON COLUMN registro_vacina.vacinador IS 'Nome do profissional que aplicou a vacina';
COMMENT ON COLUMN registro_vacina.local_aplicacao IS 'Local onde a vacina foi aplicada (ex: UBS Centro)';
COMMENT ON COLUMN registro_vacina.created_at IS 'Data de criação do registro';

-- Índices para melhor performance
CREATE INDEX idx_registro_vacina_pessoa_id ON registro_vacina(pessoa_id);
CREATE INDEX idx_registro_vacina_esquema_vacinal_id ON registro_vacina(esquema_vacinal_id);
CREATE INDEX idx_registro_vacina_data_aplicacao ON registro_vacina(data_aplicacao);
