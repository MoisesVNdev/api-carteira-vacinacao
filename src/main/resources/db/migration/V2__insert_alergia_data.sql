-- Migration Flyway v2
-- Inserção de dados de alergias no catálogo

-- Dados iniciais: catálogo base de alergias comuns
-- Estas alergias podem ser vinculadas às pessoas pelos usuários
INSERT INTO alergia (descricao) VALUES
    ('Dipirona'),
    ('Penicilina'),
    ('Amoxicilina'),
    ('Cefalosporina'),
    ('Enrofloxacina'),
    ('Sulfametoxazol'),
    ('Ácido Acetilsalicílico'),
    ('Ibuprofeno'),
    ('Naproxeno'),
    ('Tramadol'),
    ('Morfina'),
    ('Codeína'),
    ('Sedação Geral'),
    ('Sedação Local'),
    ('Látex'),
    ('Iodo'),
    ('Contraste Radiológico'),
    ('Ovo'),
    ('Leite'),
    ('Amendoim'),
    ('Castanha de caju'),
    ('Peixe'),
    ('Marisco'),
    ('Glúten'),
    ('Soja'),
    ('Melancia'),
    ('Abacaxi'),
    ('Maçã'),
    ('Kiwi'),
    ('Cereja');
