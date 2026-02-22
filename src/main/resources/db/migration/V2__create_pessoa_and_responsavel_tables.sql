-- Migration Flyway v2
-- Criação das tabelas pessoa e responsavel

-- Tabela pessoa
CREATE TABLE pessoa (
    id BIGSERIAL PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    cns VARCHAR(15) NOT NULL UNIQUE,
    cpf VARCHAR(11) UNIQUE,
    nome_mae VARCHAR(255) NOT NULL,
    genero VARCHAR(20),
    nacionalidade VARCHAR(50),
    naturalidade VARCHAR(100),
    tipo_sanguineo VARCHAR(3),
    foto TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Comentários descritivos da tabela pessoa
COMMENT ON TABLE pessoa IS 'Tabela de pessoas (dependentes) gerenciadas pelos usuários';
COMMENT ON COLUMN pessoa.nome_completo IS 'Nome completo da pessoa';
COMMENT ON COLUMN pessoa.data_nascimento IS 'Data de nascimento';
COMMENT ON COLUMN pessoa.cns IS 'Cartão Nacional de Saúde (CNS) - único';
COMMENT ON COLUMN pessoa.cpf IS 'CPF da pessoa - único (opcional para menores)';
COMMENT ON COLUMN pessoa.nome_mae IS 'Nome completo da mãe';
COMMENT ON COLUMN pessoa.tipo_sanguineo IS 'Tipo sanguíneo (ex: A+, O-, AB+)';
COMMENT ON COLUMN pessoa.foto IS 'URL ou caminho da foto da pessoa';

-- Índices para melhor performance
CREATE INDEX idx_pessoa_cns ON pessoa(cns);
CREATE INDEX idx_pessoa_cpf ON pessoa(cpf);

-- Tabela responsavel (vínculo entre usuario e pessoa)
CREATE TABLE responsavel (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    pessoa_id BIGINT NOT NULL,
    tipo_relacao VARCHAR(50),
    data_criacao TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE,
    CONSTRAINT uk_usuario_pessoa UNIQUE (usuario_id, pessoa_id)
);

-- Comentários descritivos da tabela responsavel
COMMENT ON TABLE responsavel IS 'Vínculo entre usuário e pessoa (quem é responsável por quem)';
COMMENT ON COLUMN responsavel.usuario_id IS 'ID do usuário responsável';
COMMENT ON COLUMN responsavel.pessoa_id IS 'ID da pessoa gerenciada';
COMMENT ON COLUMN responsavel.tipo_relacao IS 'Tipo de relação (Pai, Mãe, Responsável Legal, etc.)';
COMMENT ON COLUMN responsavel.data_criacao IS 'Data de criação do vínculo';

-- Índices para melhor performance
CREATE INDEX idx_responsavel_usuario_id ON responsavel(usuario_id);
CREATE INDEX idx_responsavel_pessoa_id ON responsavel(pessoa_id);
