-- Tabela usuario
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela pessoa
CREATE TABLE pessoa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    cns VARCHAR(20) NOT NULL UNIQUE,
    cpf VARCHAR(11) UNIQUE NULL,
    nome_mae VARCHAR(255) NOT NULL,
    genero VARCHAR(20) NULL,
    nacionalidade VARCHAR(100) NULL,
    naturalidade VARCHAR(100) NULL,
    tipo_sanguineo VARCHAR(3) NULL,
    foto VARCHAR(255) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela alergia (catálogo)
CREATE TABLE alergia (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela associativa pessoa_alergia
CREATE TABLE pessoa_alergia (
    pessoa_id INT NOT NULL,
    alergia_id INT NOT NULL,
    observacao TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (pessoa_id, alergia_id),
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE,
    FOREIGN KEY (alergia_id) REFERENCES alergia(id) ON DELETE CASCADE
);

-- Tabela vacina (catálogo)
CREATE TABLE vacina (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL UNIQUE,
    descricao TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela registro_vacina (histórico)
CREATE TABLE registro_vacina (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pessoa_id INT NOT NULL,
    vacina_id INT NOT NULL,
    dose VARCHAR(30) NOT NULL,
    data_aplicacao DATE NOT NULL,
    local VARCHAR(255) NULL,
    data_proxima_dose DATE NULL,
    via_administracao VARCHAR(30) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE,
    FOREIGN KEY (vacina_id) REFERENCES vacina(id) ON DELETE RESTRICT
);

-- Tabela responsavel (vínculo usuário-pessoa)
CREATE TABLE responsavel (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    pessoa_id INT NOT NULL,
    tipo_relacao VARCHAR(50) NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_usuario_pessoa (usuario_id, pessoa_id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE
);