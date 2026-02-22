-- Migration Flyway v1
-- Criação das tabelas alergia e pessoa_alergia

-- Tabela alergia (catálogo de alergias)
CREATE TABLE alergia (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Comentário descritivo da tabela
COMMENT ON TABLE alergia IS 'Catálogo de alergias disponíveis no sistema (somente leitura via API)';
COMMENT ON COLUMN alergia.descricao IS 'Descrição única da alergia';
COMMENT ON COLUMN alergia.created_at IS 'Data de criação do registro';

-- Índices para melhor performance
CREATE INDEX idx_alergia_descricao ON alergia(descricao);

-- Tabela pessoa_alergia (vínculo entre pessoa e alergia)
CREATE TABLE pessoa_alergia (
    pessoa_id BIGINT NOT NULL,
    alergia_id BIGINT NOT NULL,
    observacao TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (pessoa_id, alergia_id),
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE,
    FOREIGN KEY (alergia_id) REFERENCES alergia(id) ON DELETE CASCADE
);

-- Comentário descritivo da tabela
COMMENT ON TABLE pessoa_alergia IS 'Vínculo entre pessoas e alergias gerenciado pelo usuário autenticado';
COMMENT ON COLUMN pessoa_alergia.observacao IS 'Observações adicionais sobre a alergia (ex: sintomas)';
COMMENT ON COLUMN pessoa_alergia.created_at IS 'Data de criação do vínculo';

-- Índices para melhor performance
CREATE INDEX idx_pessoa_alergia_pessoa_id ON pessoa_alergia(pessoa_id);
CREATE INDEX idx_pessoa_alergia_alergia_id ON pessoa_alergia(alergia_id);
