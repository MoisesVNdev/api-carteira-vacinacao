-- Migration Flyway v6
-- Inserção de dados do PNI (Programa Nacional de Imunizações)
-- Vacinas e esquemas vacinais do calendário vacinal brasileiro

-- ================================================
-- INSERÇÃO DE VACINAS
-- ================================================

INSERT INTO vacina (nome, descricao, doenca_evitada) VALUES
    ('BCG', 'Vacina Bacilo Calmette-Guérin', 'Formas graves de tuberculose (miliar e meníngea)'),
    ('Hepatite B', 'Vacina contra Hepatite B', 'Hepatite B'),
    ('Pentavalente', 'Vacina contra Difteria, Tétano, Coqueluche, Haemophilus influenzae tipo b e Hepatite B', 'Difteria, Tétano, Coqueluche, Haemophilus influenzae tipo b, Hepatite B'),
    ('VIP (Poliomielite inativada)', 'Vacina Inativada contra Poliomielite', 'Poliomielite'),
    ('VOP (Poliomielite oral)', 'Vacina Oral contra Poliomielite', 'Poliomielite'),
    ('Pneumocócica 10-valente', 'Vacina Pneumocócica 10-valente (conjugada)', 'Doenças invasivas causadas por Streptococcus pneumoniae'),
    ('Rotavírus', 'Vacina Oral contra Rotavírus Humano', 'Diarreia por rotavírus'),
    ('Meningocócica C (conjugada)', 'Vacina Meningocócica C (conjugada)', 'Doença invasiva causada por Neisseria meningitidis do sorogrupo C'),
    ('Febre Amarela', 'Vacina contra Febre Amarela (vírus vivo atenuado)', 'Febre Amarela'),
    ('Tríplice Viral (SCR)', 'Vacina contra Sarampo, Caxumba e Rubéola', 'Sarampo, Caxumba, Rubéola'),
    ('Tetraviral (SCRV)', 'Vacina contra Sarampo, Caxumba, Rubéola e Varicela', 'Sarampo, Caxumba, Rubéola, Varicela'),
    ('Hepatite A', 'Vacina contra Hepatite A', 'Hepatite A'),
    ('DTP (Tríplice Bacteriana)', 'Vacina contra Difteria, Tétano e Coqueluche', 'Difteria, Tétano, Coqueluche'),
    ('Varicela', 'Vacina contra Varicela', 'Varicela (Catapora)'),
    ('HPV Quadrivalente', 'Vacina contra Papilomavírus Humano (tipos 6, 11, 16 e 18)', 'Câncer de colo do útero, vulva, vagina, ânus, pênis e verrugas genitais'),
    ('dTpa (Tríplice Bacteriana Acelular)', 'Vacina contra Difteria, Tétano e Coqueluche (acelular)', 'Difteria, Tétano, Coqueluche'),
    ('dT (Dupla Adulto)', 'Vacina contra Difteria e Tétano', 'Difteria, Tétano'),
    ('Influenza (Gripe)', 'Vacina contra Influenza', 'Gripe (Influenza)'),
    ('COVID-19', 'Vacina contra COVID-19', 'COVID-19 (SARS-CoV-2)'),
    ('Meningocócica ACWY', 'Vacina Meningocócica ACWY (conjugada)', 'Doença invasiva causada por Neisseria meningitidis dos sorogrupos A, C, W e Y');

-- ================================================
-- INSERÇÃO DE ESQUEMAS VACINAIS
-- ================================================

-- BCG (ao nascer)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'BCG'), 'Dose Única', 0, NULL);

-- Hepatite B (ao nascer, 1 mês, 6 meses)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'Hepatite B'), 'Dose ao Nascer', 0, NULL),
    ((SELECT id FROM vacina WHERE nome = 'Hepatite B'), '2ª Dose', 1, 30),
    ((SELECT id FROM vacina WHERE nome = 'Hepatite B'), '3ª Dose', 6, 150);

-- Pentavalente (2, 4, 6 meses)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'Pentavalente'), '1ª Dose', 2, NULL),
    ((SELECT id FROM vacina WHERE nome = 'Pentavalente'), '2ª Dose', 4, 60),
    ((SELECT id FROM vacina WHERE nome = 'Pentavalente'), '3ª Dose', 6, 60);

-- VIP - Poliomielite inativada (2, 4, 6 meses)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'VIP (Poliomielite inativada)'), '1ª Dose', 2, NULL),
    ((SELECT id FROM vacina WHERE nome = 'VIP (Poliomielite inativada)'), '2ª Dose', 4, 60),
    ((SELECT id FROM vacina WHERE nome = 'VIP (Poliomielite inativada)'), '3ª Dose', 6, 60);

-- VOP - Poliomielite oral (15 meses e 4 anos)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'VOP (Poliomielite oral)'), '1º Reforço', 15, NULL),
    ((SELECT id FROM vacina WHERE nome = 'VOP (Poliomielite oral)'), '2º Reforço', 48, 180);

-- Pneumocócica 10-valente (2, 4, 12 meses)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'Pneumocócica 10-valente'), '1ª Dose', 2, NULL),
    ((SELECT id FROM vacina WHERE nome = 'Pneumocócica 10-valente'), '2ª Dose', 4, 60),
    ((SELECT id FROM vacina WHERE nome = 'Pneumocócica 10-valente'), 'Reforço', 12, 180);

-- Rotavírus (2 e 4 meses)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'Rotavírus'), '1ª Dose', 2, NULL),
    ((SELECT id FROM vacina WHERE nome = 'Rotavírus'), '2ª Dose', 4, 60);

-- Meningocócica C (3, 5, 12 meses)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'Meningocócica C (conjugada)'), '1ª Dose', 3, NULL),
    ((SELECT id FROM vacina WHERE nome = 'Meningocócica C (conjugada)'), '2ª Dose', 5, 60),
    ((SELECT id FROM vacina WHERE nome = 'Meningocócica C (conjugada)'), 'Reforço', 12, 180);

-- Febre Amarela (9 meses - dose única)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'Febre Amarela'), 'Dose Única', 9, NULL);

-- Tríplice Viral (12 meses)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'Tríplice Viral (SCR)'), '1ª Dose', 12, NULL);

-- Tetraviral (15 meses)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'Tetraviral (SCRV)'), 'Dose Única', 15, NULL);

-- Hepatite A (15 meses)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'Hepatite A'), 'Dose Única', 15, NULL);

-- DTP - Tríplice Bacteriana (15 meses e 4 anos)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'DTP (Tríplice Bacteriana)'), '1º Reforço', 15, NULL),
    ((SELECT id FROM vacina WHERE nome = 'DTP (Tríplice Bacteriana)'), '2º Reforço', 48, 180);

-- Varicela (4 anos)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'Varicela'), 'Dose Única', 48, NULL);

-- HPV Quadrivalente (9 anos - 2 doses com intervalo de 6 meses)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'HPV Quadrivalente'), '1ª Dose', 108, NULL),
    ((SELECT id FROM vacina WHERE nome = 'HPV Quadrivalente'), '2ª Dose', 114, 180);

-- Meningocócica ACWY (11-12 anos)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'Meningocócica ACWY'), 'Dose Única', 132, NULL);

-- COVID-19 (6 meses - esquema básico)
INSERT INTO esquema_vacinal (vacina_id, descricao_dose, idade_recomendada_meses, intervalo_minimo_dias) VALUES
    ((SELECT id FROM vacina WHERE nome = 'COVID-19'), '1ª Dose', 6, NULL),
    ((SELECT id FROM vacina WHERE nome = 'COVID-19'), '2ª Dose', 7, 30);
