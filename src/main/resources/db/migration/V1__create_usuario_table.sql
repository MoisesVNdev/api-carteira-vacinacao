-- Migration Flyway v1
-- Criação da tabela usuario

CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Comentários descritivos
COMMENT ON TABLE usuario IS 'Tabela de usuários do sistema';
COMMENT ON COLUMN usuario.nome_completo IS 'Nome completo do usuário';
COMMENT ON COLUMN usuario.email IS 'Email único do usuário (usado no login)';
COMMENT ON COLUMN usuario.senha IS 'Senha criptografada (BCrypt) do usuário';
COMMENT ON COLUMN usuario.data_cadastro IS 'Data e hora do cadastro do usuário';

-- Índices para melhor performance
CREATE INDEX idx_usuario_email ON usuario(email);
