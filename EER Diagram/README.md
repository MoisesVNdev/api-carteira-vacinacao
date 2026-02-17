# Modelo de Dados – Carteira de Vacinação V1.0

Este diretório contém a versão 1.0 do modelo entidade‑relacionamento estendido (EER) desenvolvido para o aplicativo **Carteira de Vacinação Digital**. O modelo foi criado no MySQL Workbench e serve como base para a implementação da API RESTful (Java/Spring Boot).

---

## Visão Geral

O modelo de dados tem como objetivo principal armazenar:

- As contas dos usuários do aplicativo.
- Os dados das pessoas (titulares das carteiras de vacinação) que serão gerenciadas.
- O vínculo entre usuários e as pessoas pelas quais são responsáveis.
- Informações clínicas relevantes (tipo sanguíneo, alergias).
- Um histórico completo das vacinas aplicadas em cada pessoa.
- Catálogos reutilizáveis de vacinas e alergias.

A modelagem foi projetada para ser escalável, normalizada e preparada para futuras expansões, como controle de lotes, fabricantes ou notificações de doses pendentes.

---

## Entidades (Tabelas)

### `usuario`
Armazena as contas de acesso ao aplicativo.

| Atributo       | Tipo         | Descrição                             | Restrições                |
|----------------|--------------|---------------------------------------|---------------------------|
| id             | INT          | Identificador único do usuário        | PK, AUTO_INCREMENT        |
| nome_completo  | VARCHAR(255) | Nome do usuário (dono da conta)       | NOT NULL                  |
| email          | VARCHAR(255) | E‑mail utilizado para login           | NOT NULL, UNIQUE          |
| senha          | VARCHAR(255) | Hash da senha (bcrypt/Argon2)         | NOT NULL                  |
| data_cadastro  | TIMESTAMP    | Momento da criação da conta           | DEFAULT CURRENT_TIMESTAMP |

### `pessoa`
Representa o titular da carteira de vacinação (a pessoa vacinada). Seus dados são cadastrados por um usuário responsável.

| Atributo          | Tipo         | Descrição                                        | Restrições                |
|-------------------|--------------|--------------------------------------------------|---------------------------|
| id                | INT          | Identificador único da pessoa                    | PK, AUTO_INCREMENT        |
| nome_completo     | VARCHAR(255) | Nome completo                                     | NOT NULL                  |
| data_nascimento   | DATE         | Data de nascimento                                | NOT NULL                  |
| cns               | VARCHAR(20)  | Número do Cartão SUS                              | NOT NULL, UNIQUE          |
| cpf               | VARCHAR(11)  | CPF (apenas dígitos)                              | UNIQUE, NULL              |
| nome_mae          | VARCHAR(255) | Nome da mãe – auxilia na desambiguação            | NOT NULL                  |
| genero            | VARCHAR(20)  | Gênero (ex.: Masculino, Feminino, Outro)          | NULL                      |
| nacionalidade     | VARCHAR(100) | País de nacionalidade                             | NULL                      |
| naturalidade      | VARCHAR(100) | Cidade/UF de nascimento                           | NULL                      |
| tipo_sanguineo    | VARCHAR(3)   | Ex.: A+, O–, AB (recomenda‑se ENUM ou CHECK)      | NULL                      |
| foto              | VARCHAR(255) | Caminho (URL) da foto do titular                  | NULL                      |
| created_at        | TIMESTAMP    | Data de criação do registro                       | DEFAULT CURRENT_TIMESTAMP |
| updated_at        | TIMESTAMP    | Data da última alteração                          | DEFAULT CURRENT_TIMESTAMP ON UPDATE |

### `alergia`
Catálogo de alergias conhecidas, permitindo padronização e reuso.

| Atributo   | Tipo         | Descrição                      | Restrições                |
|------------|--------------|--------------------------------|---------------------------|
| id         | INT          | Identificador único da alergia | PK, AUTO_INCREMENT        |
| descricao  | VARCHAR(100) | Nome da alergia (ex.: "Ovo")   | NOT NULL, UNIQUE          |
| created_at | TIMESTAMP    | Data de criação                | DEFAULT CURRENT_TIMESTAMP |

### `pessoa_alergia`
Tabela associativa que liga uma pessoa a uma ou mais alergias (relacionamento N:N). Permite incluir observações específicas sobre a reação.

| Atributo    | Tipo      | Descrição                                  | Restrições                     |
|-------------|-----------|--------------------------------------------|--------------------------------|
| pessoa_id   | INT       | Chave estrangeira para `pessoa`            | PK, FK (ON DELETE CASCADE)     |
| alergia_id  | INT       | Chave estrangeira para `alergia`           | PK, FK (ON DELETE CASCADE)     |
| observacao  | TEXT      | Detalhes adicionais (ex.: reação anafilática) | NULL                          |
| created_at  | TIMESTAMP | Data de criação do vínculo                 | DEFAULT CURRENT_TIMESTAMP      |

### `vacina`
Catálogo de vacinas (imunobiológicos). Futuramente pode ser expandido com fabricante, lote, etc.

| Atributo   | Tipo         | Descrição                            | Restrições                |
|------------|--------------|--------------------------------------|---------------------------|
| id         | INT          | Identificador único da vacina        | PK, AUTO_INCREMENT        |
| nome       | VARCHAR(150) | Nome comercial ou científico         | NOT NULL, UNIQUE          |
| descricao  | TEXT         | Informações complementares           | NULL                      |
| created_at | TIMESTAMP    | Data de criação                      | DEFAULT CURRENT_TIMESTAMP |
| updated_at | TIMESTAMP    | Data da última alteração             | DEFAULT CURRENT_TIMESTAMP ON UPDATE |

### `registro_vacina`
Histórico de vacinas aplicadas em cada pessoa. É a tabela central para o acompanhamento vacinal.

| Atributo           | Tipo         | Descrição                                   | Restrições                |
|--------------------|--------------|---------------------------------------------|---------------------------|
| id                 | INT          | Identificador único do registro             | PK, AUTO_INCREMENT        |
| pessoa_id          | INT          | Pessoa que recebeu a dose                   | NOT NULL, FK              |
| vacina_id          | INT          | Vacina aplicada                             | NOT NULL, FK              |
| dose               | VARCHAR(30)  | Ex.: "1ª Dose", "Reforço", "Dose Única"     | NOT NULL                  |
| data_aplicacao     | DATE         | Data da administração                       | NOT NULL                  |
| local              | VARCHAR(255) | Estabelecimento onde foi aplicada           | NULL                      |
| data_proxima_dose  | DATE         | Data agendada para a próxima dose (se houver) | NULL                  |
| via_administracao  | VARCHAR(30)  | Ex.: "Intramuscular", "Oral", "Subcutânea"  | NULL                      |
| created_at         | TIMESTAMP    | Data de criação do registro                 | DEFAULT CURRENT_TIMESTAMP |
| updated_at         | TIMESTAMP    | Data da última alteração                    | DEFAULT CURRENT_TIMESTAMP ON UPDATE |

### `responsavel`
Vincula um usuário a uma pessoa que ele gerencia. Permite que a mesma pessoa seja acompanhada por múltiplos responsáveis (ex.: pai e mãe).

| Atributo     | Tipo      | Descrição                             | Restrições                     |
|--------------|-----------|---------------------------------------|--------------------------------|
| id           | INT       | Identificador único do vínculo        | PK, AUTO_INCREMENT             |
| usuario_id   | INT       | Chave estrangeira para `usuario`      | NOT NULL, FK (ON DELETE CASCADE) |
| pessoa_id    | INT       | Chave estrangeira para `pessoa`       | NOT NULL, FK (ON DELETE CASCADE) |
| tipo_relacao | VARCHAR(50) | Ex.: "próprio", "filho(a)", "cônjuge" | NULL                           |
| data_criacao | TIMESTAMP | Data do vínculo                       | DEFAULT CURRENT_TIMESTAMP      |
| UNIQUE(usuario_id, pessoa_id) | Garante que o mesmo usuário não vincule a mesma pessoa duas vezes. |

---

## Relacionamentos e Fluxo de Dados

O modelo reflete o seguinte fluxo típico:

1. Um **usuário** se cadastra no aplicativo (tabela `usuario`).
2. Esse usuário pode adicionar **pessoas** (titulares de carteiras) – ele próprio, filhos, pais, etc. O vínculo é registrado na tabela `responsavel`.
3. Para cada pessoa, é possível:
   - Registrar informações clínicas, incluindo **tipo sanguíneo** e **alergias** (via tabela `pessoa_alergia`).
   - Lançar vacinas aplicadas na tabela `registro_vacina`, utilizando o catálogo de vacinas (`vacina`).
4. O catálogo de vacinas e alergias é independente, garantindo consistência (evita digitar "tríplice viral" de formas diferentes).

**Cardinalidades resumidas:**

- `usuario` **1** : **N** `responsavel`
- `pessoa` **1** : **N** `responsavel`
- `pessoa` **1** : **N** `registro_vacina`
- `vacina` **1** : **N** `registro_vacina`
- `pessoa` **N** : **N** `alergia` (via `pessoa_alergia`)

---

## Considerações de Design

- **Normalização**: A separação em catálogos (`vacina`, `alergia`) evita redundância e facilita manutenção. A tabela associativa `pessoa_alergia` permite que uma pessoa tenha múltiplas alergias sem repetir texto.
- **Chaves estrangeiras**:
  - `ON DELETE CASCADE` nas relações que dependem fortemente da entidade pai (`pessoa`, `usuario`). Se uma pessoa for removida, seus registros de vacina, alergias e vínculos de responsabilidade também são removidos automaticamente.
  - `ON DELETE RESTRICT` na chave `vacina_id` de `registro_vacina`: impede que uma vacina seja excluída enquanto houver registros de aplicação associados, preservando a integridade histórica.
- **Unicidade**: O CNS é único por pessoa; o CPF também, quando informado. O par (`usuario_id`, `pessoa_id`) em `responsavel` é único para evitar vínculos duplicados.
- **Campos de auditoria**: Todas as tabelas possuem `created_at` e, quando pertinente, `updated_at`, permitindo rastrear criação e modificações.
- **Extensibilidade**: O modelo já prevê a inclusão futura de campos como lote, fabricante, validade da vacina, ou um módulo de notificações baseado em `data_proxima_dose`.

---

## Arquivos do Diagrama

Na pasta `V1.0` estão disponíveis:

| Arquivo          | Descrição                                                                 |
|------------------|---------------------------------------------------------------------------|
| `diagrama.mwb`   | Arquivo nativo do MySQL Workbench – permite edição completa do modelo.    |
| `diagrama.pdf`   | Versão em PDF para visualização e impressão.                              |
| `diagrama.svg`   | Vetor escalável, ideal para inclusão em documentação web.                 |
| `script.sql`     | Script SQL com todas as instruções `CREATE TABLE` e constraints.          |
| `README.md`      | Este arquivo de documentação.                                             |

---

## Como Utilizar

Para recriar o banco de dados em um ambiente MySQL:

1. Certifique‑se de ter um servidor MySQL em execução.
2. Execute o script `script.sql` em uma ferramenta como MySQL Workbench, linha de comando ou através de migrações (Flyway/Liquibase) na aplicação Spring Boot.
3. O banco de dados estará pronto para receber os dados da API.

Exemplo de comando via terminal:
```bash
mysql -u seu_usuario -p nome_do_banco < script.sql
```

---

Este modelo reflete a versão inicial do projeto e está aberto a melhorias conforme a evolução dos requisitos. Contribuições e sugestões são bem‑vindas!