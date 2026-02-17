-- ============================================================================
-- SCRIPT DE INICIALIZAÇÃO DO POSTGRESQL
-- ============================================================================
-- Este script é executado automaticamente na primeira inicialização do
-- container PostgreSQL.
--
-- Local: docker/postgres/init/01-init.sql
-- ============================================================================

-- ----------------------------------------------------------------------------
-- CRIAÇÃO DE EXTENSÕES
-- ----------------------------------------------------------------------------

-- UUID: Para geração de UUIDs
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- pgcrypto: Para criptografia
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- unaccent: Para buscas sem acento
CREATE EXTENSION IF NOT EXISTS "unaccent";

-- ----------------------------------------------------------------------------
-- CONFIGURAÇÕES DO BANCO
-- ----------------------------------------------------------------------------

-- Define o timezone do banco
SET timezone = 'America/Sao_Paulo';

-- ----------------------------------------------------------------------------
-- USUÁRIOS E PERMISSÕES (Opcional)
-- ----------------------------------------------------------------------------
-- Descomente se desejar criar usuários adicionais

-- Usuário somente leitura (para relatórios, BI, etc)
-- CREATE USER readonly_user WITH PASSWORD 'senha_readonly';
-- GRANT CONNECT ON DATABASE seu_banco TO readonly_user;
-- GRANT USAGE ON SCHEMA public TO readonly_user;
-- GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly_user;
-- ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO readonly_user;

-- ----------------------------------------------------------------------------
-- AUDITORIA (Opcional)
-- ----------------------------------------------------------------------------
-- Tabela de auditoria genérica
-- Descomente se desejar ter auditoria automática

-- CREATE TABLE IF NOT EXISTS auditoria (
--     id BIGSERIAL PRIMARY KEY,
--     tabela VARCHAR(100) NOT NULL,
--     operacao VARCHAR(10) NOT NULL,
--     usuario VARCHAR(100) NOT NULL,
--     data_hora TIMESTAMP NOT NULL DEFAULT NOW(),
--     dados_antigos JSONB,
--     dados_novos JSONB
-- );

-- CREATE INDEX idx_auditoria_tabela ON auditoria(tabela);
-- CREATE INDEX idx_auditoria_data_hora ON auditoria(data_hora);

-- ----------------------------------------------------------------------------
-- OBSERVAÇÕES
-- ----------------------------------------------------------------------------
-- 1. Este script é executado apenas na primeira inicialização
-- 2. Se precisar executar novamente, delete o volume: docker-compose down -v
-- 3. Múltiplos scripts podem ser adicionados (serão executados em ordem alfabética)
-- 4. Use prefixo numérico para controlar a ordem: 01-init.sql, 02-data.sql, etc
-- ============================================================================

-- Log de finalização
DO $$
BEGIN
    RAISE NOTICE 'Inicialização do banco de dados concluída com sucesso!';
END $$;
