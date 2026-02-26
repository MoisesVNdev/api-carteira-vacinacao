# 📋 API Carteira de Vacinação Digital

API REST desenvolvida em **Java 21** com **Spring Boot 4.0.3** projetada para ser uma ferramenta auxiliar no controle do calendário vacinal. O foco principal é apoiar pais e responsáveis no acompanhamento da saúde de crianças e adolescentes, oferecendo uma camada extra de segurança e organização aos registros físicos.

O projeto não tem o objetivo de substituir a carteira de vacinação física, mas sim complementá-la, oferecendo funcionalidades como alertas de próximas vacinas, histórico vacinal detalhado, registro de alergias e armazenamento de fotos da carteira física para consulta em caso de perda ou esquecimento.

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.3-brightgreen?logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 🎯 Sobre o Projeto

Este projeto nasceu da necessidade de modernizar o acompanhamento vacinal familiar. **Importante:** Esta aplicação não substitui a carteira de vacinação física oficial; ela atua como um suporte digital para garantir que nenhuma dose seja esquecida e que as informações críticas estejam sempre à mão.

Como desenvolvedor, utilizei este projeto para aplicar conceitos modernos de arquitetura, focando em:

- ✅ **Arquitetura REST** seguindo os princípios **SOLID**
- ✅ **Autenticação e Autorização** com Spring Security + JWT
- ✅ **Persistência de dados** com Spring Data JPA e PostgreSQL
- ✅ **Migrações versionadas** com Flyway
- ✅ **Containerização** com Docker e Docker Compose
- ✅ **Boas práticas de segurança** (BCrypt, usuário não-root, health checks)
- ✅ **Separação de camadas** (Controller → Service → Repository)
- ✅ **DTOs** para proteção das entidades
- ✅ **Tratamento centralizado de exceções** (@ControllerAdvice)
- ✅ **Documentação interativa** com SpringDoc OpenAPI

---

## ✨ Funcionalidades

### 🔐 Autenticação e Segurança
- **CRUD Completo**: Criação, leitura, atualização e exclusão de usuários, pessoas, responsáveis, vacinas, alergias e registros vacinais
- **Segurança**: Autenticação e autorização baseada em JWT utilizando Spring Security
- **Validação de Dados**: Uso de Bean Validation para garantir a integridade das requisições (`@Valid`, `@NotNull`, `@Size`, etc.)
- **Tratamento de Exceções**: Respostas de erro padronizadas (Global Exception Handler) com mensagens claras
- **Documentação Interativa**: Interface visual para testar os endpoints utilizando Swagger UI / SpringDoc
- **Registro de usuários** com validação de dados completa
- **Login seguro** com geração de token JWT (JJWT 0.11.5)
- **Proteção de endpoints** via Spring Security com filtro de autenticação
- **Hash de senhas** utilizando BCrypt (nunca armazenadas em texto puro)
- **Controle de acesso** baseado em roles/papéis

### 👥 Gestão de Usuários e Pessoas
- **CRUD completo de Usuários**: Criação, leitura, atualização e exclusão de contas de usuário
- **CRUD completo de Pessoas**: Gerenciamento de perfis pessoais (crianças, adolescentes, adultos)
- **Vínculo Responsável-Pessoa**: Relacionamento entre usuários responsáveis e as pessoas sob seus cuidados (ex.: pais, mães, tutores)

### 💉 Gestão Vacinal
- **CRUD de Vacinas**: Cadastro e gerenciamento de vacinas do calendário nacional (PNI)
- **Esquemas Vacinais**: Definição de esquemas de vacinação por faixa etária
- **Registro de Vacinas**: Histórico detalhado de vacinas aplicadas com datas e lotes
- **Status Vacinal**: Controle de doses pendentes, em dia ou atrasadas
- **Dados pré-populados**: Base do PNI (Programa Nacional de Imunizações) via migrations

### 🩺 Gestão de Alergias
- **CRUD de Alergias**: Cadastro de alergias conhecidas
- **Vínculo Pessoa-Alergia**: Registro de alergias específicas para cada pessoa
- **Dados pré-populados**: Base inicial com alergias comuns (via migration)

### 📊 Recursos Adicionais
- **Health Check**: Endpoint Actuator para monitoramento da saúde da aplicação
- **CORS configurado**: Suporte para requisições de diferentes origens
- **Auditoria**: Timestamps automáticos de criação e atualização
- **Testes REST integrados**: Arquivos `.http` para testar endpoints rapidamente

---

## 💻 Tecnologias Utilizadas

### 🔧 Backend
- **Linguagem:** Java 21 LTS
- **Framework:** Spring Boot 4.0.3
  - Spring Web (REST API)
  - Spring Data JPA (ORM)
  - Spring Security (Autenticação e Autorização)
  - Spring Validation (Validação de dados)
  - Spring Actuator (Monitoramento)

### 🔒 Segurança
- **JSON Web Tokens (JWT):** JJWT 0.11.5
- **Criptografia:** BCrypt para hash de senhas

### 💾 Banco de Dados
- **SGBD:** PostgreSQL 16 (Imagem Alpine via Docker)
- **Driver:** PostgreSQL JDBC Driver
- **Pool de Conexões:** HikariCP (padrão do Spring Boot)

### 🗄️ Migrações
- **Flyway:** 10.8.1 (versionamento e controle de esquema do banco)

### 📝 Documentação
- **SpringDoc OpenAPI:** 2.8.2 (Swagger UI integrado)

### 🛠️ Gerenciador de Dependências
- **Maven:** 3.9+

### 🐳 DevOps
- **Docker:** Containerização da aplicação
- **Docker Compose:** Orquestração de containers (app + banco)
- **Multi-stage Build:** Otimização da imagem Docker

### 🧰 Ferramentas Auxiliares
- **Lombok:** Redução de boilerplate
- **H2 Database:** Banco em memória para testes

---

## 🛠️ Pré-requisitos

Para rodar este projeto localmente, você precisará ter instalado:

- [Java 21](https://adoptium.net/) ou superior
- [Maven](https://maven.apache.org/) 3.9+
- [Docker](https://www.docker.com/) e [Docker Compose](https://docs.docker.com/compose/)
- [Git](https://git-scm.com/)

> **Nota:** Se você usar Docker Compose, não é necessário instalar PostgreSQL localmente.

---

## 🚀 Como Executar o Projeto

### 1️⃣ Clone o repositório

```bash
git clone https://github.com/MoisesVNdev/api-carteira-vacinacao.git
cd api-carteira-vacinacao
```

### 2️⃣ Configure as variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
# Nome do projeto (usado como prefixo nos containers)
COMPOSE_PROJECT_NAME=carteira-vacinacao

# PostgreSQL - Credenciais
POSTGRES_DB=carteira_vacinacao_db
POSTGRES_USER=admin_vacinas
POSTGRES_PASSWORD=sua_senha_segura_aqui

# Spring Boot - Configurações
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/carteira_vacinacao_db
SPRING_DATASOURCE_USERNAME=admin_vacinas
SPRING_DATASOURCE_PASSWORD=sua_senha_segura_aqui

# JWT - Chave secreta (mínimo 256 bits / 32 caracteres)
JWT_SECRET=sua_chave_secreta_jwt_muito_longa_e_segura_aqui_com_minimo_32_chars

# Timezone
TZ=America/Sao_Paulo
```

> **⚠️ IMPORTANTE:** Nunca commite o arquivo `.env` no Git! Ele já está listado no `.gitignore`.

### 3️⃣ Execução com Docker Compose (Recomendado)

#### Iniciar a aplicação
```bash
docker-compose up -d
```

#### Verificar logs
```bash
docker-compose logs -f
```

#### Parar a aplicação
```bash
docker-compose down
```

#### Parar e remover volumes (limpa o banco de dados)
```bash
docker-compose down -v
```

#### Reconstruir a imagem após mudanças no código
```bash
docker-compose up -d --build
```

### 4️⃣ Execução Local (Sem Docker)

Se preferir rodar localmente sem Docker:

#### 1. Instale e configure o PostgreSQL 16

#### 2. Configure o banco de dados

```sql
CREATE DATABASE carteira_vacinacao_db;
CREATE USER admin_vacinas WITH PASSWORD 'sua_senha';
GRANT ALL PRIVILEGES ON DATABASE carteira_vacinacao_db TO admin_vacinas;
```

#### 3. Execute a aplicação com Maven

```bash
mvn clean install
mvn spring-boot:run
```

Ou usando o wrapper do Maven:
```bash
./mvnw clean install
./mvnw spring-boot:run
```

---

## 🌐 Acessando a Aplicação

Após iniciar, a aplicação estará disponível em:

- **API Base URL:** http://localhost:8080
- **Swagger UI (Documentação):** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs
- **Health Check:** http://localhost:8080/actuator/health

---

## 🏗️ Arquitetura do Projeto

### Estrutura de Pacotes

```
src/main/java/com/moisesvn/carteira_vacinacao_api/
├── config/               → Configurações do Spring (CORS, Beans, etc.)
├── controller/           → Camada HTTP: recebe requisições, delega ao Service
│   ├── AlergiaController.java
│   ├── AuthController.java
│   ├── HomeController.java
│   ├── PessoaAlergiaController.java
│   ├── PessoaController.java
│   ├── RegistroVacinaController.java
│   ├── ResponsavelController.java
│   ├── UsuarioController.java
│   └── VacinaController.java
├── dto/                  → Objetos de transferência (Request/Response)
├── exception/            → Handlers globais (@ControllerAdvice), exceções customizadas
├── mapper/               → Conversores entre Entidade ↔ DTO
├── model/                → Entidades JPA (@Entity)
│   ├── Alergia.java
│   ├── EsquemaVacinal.java
│   ├── Pessoa.java
│   ├── PessoaAlergia.java
│   ├── RegistroVacina.java
│   ├── Responsavel.java
│   ├── StatusVacinal.java
│   ├── Usuario.java
│   └── Vacina.java
├── openapi/              → Configuração e customização do SpringDoc
├── repository/           → Interfaces Spring Data JPA
├── security/             → Configuração JWT, filtros, UserDetailsService
└── service/              → Regras de negócio, validações, orquestração
```

### Princípio de Camadas
- **Controller** → Recebe requisições HTTP, valida entrada, chama Service
- **Service** → Contém regras de negócio, orquestra operações
- **Repository** → Acessa o banco de dados via Spring Data JPA
- **DTO** → Objetos de entrada/saída da API, protegem as entidades
- **Model** → Entidades JPA que representam as tabelas do banco

> **Regra de Ouro:** Nenhuma camada deve pular outra. Controller nunca acessa Repository diretamente.

### Fluxo de Requisição

```
Cliente HTTP
    ↓
[Controller] → Valida entrada (DTO)
    ↓
[Service] → Aplica regras de negócio
    ↓
[Repository] → Acessa o banco de dados
    ↓
[Entity] → Modelo de dados persistido
    ↓
[Mapper] → Converte Entity → DTO
    ↓
Resposta HTTP (DTO)
```

### Segurança

```
Requisição HTTP
    ↓
[JwtAuthenticationFilter] → Valida o token JWT
    ↓
[Spring Security] → Verifica permissões
    ↓
[Controller] → Processa a requisição autenticada
```

---

## 🔌 Principais Endpoints da API

### 🔓 Autenticação (Público)
| Método | Endpoint          | Descrição                    |
|--------|-------------------|------------------------------|
| POST   | `/auth/register`  | Registrar novo usuário       |
| POST   | `/auth/login`     | Autenticar e obter token JWT |

### 🔐 Usuários (Autenticado)
| Método | Endpoint              | Descrição                    |
|--------|-----------------------|------------------------------|
| GET    | `/api/usuarios`       | Listar todos os usuários     |
| GET    | `/api/usuarios/{id}`  | Buscar usuário por ID        |
| PUT    | `/api/usuarios/{id}`  | Atualizar usuário            |
| DELETE | `/api/usuarios/{id}`  | Deletar usuário              |

### 👥 Pessoas (Autenticado)
| Método | Endpoint             | Descrição                  |
|--------|----------------------|----------------------------|
| POST   | `/api/pessoas`       | Criar nova pessoa          |
| GET    | `/api/pessoas`       | Listar todas as pessoas    |
| GET    | `/api/pessoas/{id}`  | Buscar pessoa por ID       |
| PUT    | `/api/pessoas/{id}`  | Atualizar pessoa           |
| DELETE | `/api/pessoas/{id}`  | Deletar pessoa             |

### 💉 Vacinas (Autenticado)
| Método | Endpoint            | Descrição                 |
|--------|---------------------|---------------------------|
| POST   | `/api/vacinas`      | Cadastrar nova vacina     |
| GET    | `/api/vacinas`      | Listar todas as vacinas   |
| GET    | `/api/vacinas/{id}` | Buscar vacina por ID      |
| PUT    | `/api/vacinas/{id}` | Atualizar vacina          |
| DELETE | `/api/vacinas/{id}` | Deletar vacina            |

### 🩺 Alergias (Autenticado)
| Método | Endpoint             | Descrição                   |
|--------|----------------------|-----------------------------|
| POST   | `/api/alergias`      | Cadastrar nova alergia      |
| GET    | `/api/alergias`      | Listar todas as alergias    |
| GET    | `/api/alergias/{id}` | Buscar alergia por ID       |
| PUT    | `/api/alergias/{id}` | Atualizar alergia           |
| DELETE | `/api/alergias/{id}` | Deletar alergia             |

### 📋 Registros Vacinais (Autenticado)
| Método | Endpoint                     | Descrição                         |
|--------|------------------------------|-----------------------------------|
| POST   | `/api/registros-vacina`      | Registrar aplicação de vacina     |
| GET    | `/api/registros-vacina`      | Listar todos os registros         |
| GET    | `/api/registros-vacina/{id}` | Buscar registro por ID            |
| PUT    | `/api/registros-vacina/{id}` | Atualizar registro                |
| DELETE | `/api/registros-vacina/{id}` | Deletar registro                  |

### 👨‍👩‍👧 Responsáveis (Autenticado)
| Método | Endpoint                        | Descrição                         |
|--------|---------------------------------|-----------------------------------|
| POST   | `/api/responsaveis`             | Vincular responsável              |
| GET    | `/api/responsaveis`             | Listar todos os responsáveis      |
| GET    | `/api/responsaveis/{id}`        | Buscar responsável por ID         |
| DELETE | `/api/responsaveis/{id}`        | Deletar vínculo                   |

> **Nota:** Para detalhes completos sobre requisições e respostas, acesse a documentação interativa no Swagger UI em http://localhost:8080/swagger-ui.html

---

## 🗄️ Modelo de Dados

O projeto utiliza as seguintes tabelas principais:

- **usuario**: Contas de acesso ao sistema
- **pessoa**: Perfis de indivíduos (crianças, adolescentes, adultos)
- **responsavel**: Relacionamento entre usuários e pessoas (ex.: pai → filho)
- **alergia**: Cadastro de alergias conhecidas
- **pessoa_alergia**: Alergias específicas de cada pessoa
- **vacina**: Catálogo de vacinas disponíveis (PNI)
- **esquema_vacinal**: Esquemas de vacinação por faixa etária
- **registro_vacina**: Histórico de vacinas aplicadas

### Migrations Flyway

```
src/main/resources/db/migration/
├── V1__create_usuario_table.sql
├── V2__create_pessoa_and_responsavel_tables.sql
├── V3__create_alergia_and_pessoa_alergia_tables.sql
├── V4__insert_alergia_data.sql
├── V5__create_vacina_esquema_vacinal_registro_vacina_tables.sql
└── V6__insert_vacina_esquema_vacinal_pni_data.sql
```

> **Diagrama EER disponível em:** `EER Diagram/V2.0/`

---

## 🧪 Testando a API

### Usando Cliente REST (VS Code)

Há arquivos `.http` na pasta `Client-REST-testes/` que você pode executar diretamente no VS Code com a extensão REST Client:

```
Client-REST-testes/
├── API-Fluxo-Corrigido-Modificado.http
├── API-Testes-Vacinacao.http
└── API-TestesValidos.http
```

### Usando Swagger UI

Acesse http://localhost:8080/swagger-ui.html e teste todos os endpoints de forma interativa.

### Usando cURL

#### Registrar usuário
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "senha": "senha123"
  }'
```

#### Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "senha": "senha123"
  }'
```

#### Listar vacinas (com token)
```bash
curl -X GET http://localhost:8080/api/vacinas \
  -H "Authorization: Bearer SEU_TOKEN_JWT_AQUI"
```

---

## 🐳 Comandos Docker Úteis

Consulte o arquivo [docker/COMANDOS-UTEIS.md](docker/COMANDOS-UTEIS.md) para lista completa de comandos Docker úteis.

### Comandos Rápidos

```bash
# Ver containers em execução
docker ps

# Ver logs da aplicação
docker logs -f carteira-vacinacao-app

# Ver logs do banco de dados
docker logs -f carteira-vacinacao-postgres

# Acessar o shell do container da aplicação
docker exec -it carteira-vacinacao-app sh

# Acessar o PostgreSQL via psql
docker exec -it carteira-vacinacao-postgres psql -U admin_vacinas -d carteira_vacinacao_db

# Remover todos os containers, volumes e imagens do projeto
docker-compose down -v --rmi all
```

---

## 🔧 Desenvolvimento

### Executar testes
```bash
mvn test
```

### Gerar relatório de cobertura
```bash
mvn clean verify
```

### Compilar sem executar testes
```bash
mvn clean install -DskipTests
```

### Verificar se há dependências desatualizadas
```bash
mvn versions:display-dependency-updates
```

---

## 📊 Diagrama Entidade-Relacionamento (EER)

O projeto inclui diagramas EER criados no MySQL Workbench:

```
EER Diagram/
├── V1.0/
│   ├── EER - Carteira de Vacinação.mwb
│   └── EER-Diagram-carteira-de-vacinacao.sql
└── V2.0/
    ├── EER-Diagram-carteira-de-vacinacao.mwb
    └── EER-Diagram-carteira-de-vacinacao.sql
```

---

## 🔒 Segurança

### Boas Práticas Implementadas

✅ **Senhas hasheadas** com BCrypt (senhas nunca são armazenadas em texto puro)  
✅ **Tokens JWT** com expiração configurável  
✅ **Usuário não-root** nos containers Docker  
✅ **Variáveis de ambiente** para credenciais sensíveis  
✅ **DTOs** para evitar a exposição direta das entidades  
✅ **Validação de entrada** com Bean Validation  
✅ **Health checks** para monitoramento da saúde da aplicação  
✅ **CORS configurado** adequadamente  
✅ **HTTPS** recomendado em produção (configurar via proxy reverso)

### Melhorias Sugeridas para Produção

- [ ] Implementar refresh tokens
- [ ] Adicionar rate limiting (ex.: Bucket4J)
- [ ] Implementar auditoria de operações
- [ ] Adicionar logs estruturados (JSON)
- [ ] Configurar SSL/TLS no PostgreSQL
- [ ] Implementar backup automático do banco de dados
- [ ] Adicionar testes de carga (JMeter, Gatling)
- [ ] Implementar monitoramento com Prometheus + Grafana

---

## 🛤️ Roadmap

### ✅ Funcionalidades Implementadas
- [x] Autenticação JWT
- [x] CRUD de Usuários
- [x] CRUD de Pessoas
- [x] Vínculo de Responsáveis
- [x] CRUD de Vacinas
- [x] CRUD de Alergias
- [x] Registro de Vacinas
- [x] Esquemas Vacinais
- [x] Migrations com dados do PNI

### 🚧 Funcionalidades Planejadas
- [ ] Armazenamento de foto da carteira de vacinação física
- [ ] Alertas e notificações de doses pendentes (e-mail)
- [ ] Geração de certificados em PDF
- [ ] Dashboard com estatísticas vacinais
- [ ] API de consulta pública (carteira digital com QR Code)
- [ ] Integração com sistemas de saúde (HL7/FHIR)
- [ ] App mobile (React Native ou Flutter)
- [ ] Suporte a múltiplos idiomas

---

## 📚 Documentação Adicional

- **Instruções de Camadas:** `.github/instructions/`
- **Comandos Úteis Docker:** `docker/COMANDOS-UTEIS.md`
- **Diagrama EER:** `EER Diagram/README.md`
- **Exemplos de Requisições:** `Client REST testes/`

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👨‍💻 Autor

**Moisés Vila Nova de Oliveira**

Este projeto foi desenvolvido como parte do meu portfólio de desenvolvedor back-end. Com ele, busco demonstrar minha capacidade de construir APIs RESTful bem estruturadas, seguras e documentadas, aplicando boas práticas do mercado desde o início do desenvolvimento.

**Competências demonstradas neste projeto:**

- ✅ Desenvolvimento de APIs REST com Java e Spring Boot
- ✅ Arquitetura em camadas e princípios SOLID
- ✅ Segurança com JWT e Spring Security
- ✅ Persistência de dados com JPA/Hibernate
- ✅ Migrações de banco de dados com Flyway
- ✅ Containerização com Docker
- ✅ Código limpo e documentação técnica

**Contato:**

- GitHub: [@MoisesVNdev](https://github.com/MoisesVNdev)
- LinkedIn: [linkedin.com/in/moisesvnoliveira](https://www.linkedin.com/in/moisesvnoliveira/)
- E-mail: [moisesvn.dev@gmail.com](mailto:moisesvn.dev@gmail.com)

> "Este software é uma ferramenta de apoio. Sempre mantenha sua carteira de vacinação física em local seguro."

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou pull requests.

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

---

## ⭐ Apoie o Projeto

Se este projeto foi útil para você, considere dar uma ⭐ no repositório!

---

**Desenvolvido com ☕ e Java **
