# 📋 API Carteira de Vacinação Digital

API REST desenvolvida em **Java 21** com **Spring Boot 4.0.2** projetada para ser uma ferramenta auxiliar no controle do calendário vacinal. O foco principal é apoiar pais e responsáveis no acompanhamento da saúde de crianças e adolescentes, oferecendo uma camada extra de segurança e organização aos registros físicos.

O projeto não tem o objetivo de substituir a carteira de vacinação física, mas sim complementá-la, oferecendo funcionalidades como alertas de próximas vacinas por notificação/e-mail, histórico vacinal, registro de alergias e armazenamento de foto da carteira física para consulta em caso de perda ou esquecimento.

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.2-brightgreen?logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 🎯 Sobre o Projeto

Este projeto nasceu da necessidade de modernizar o acompanhamento vacinal familiar. **Importante:** Esta aplicação não substitui a carteira de vacinação física oficial; ela atua como um suporte digital para garantir que nenhuma dose seja esquecida e que as informações críticas estejam sempre à mão.

Como desenvolvedor, utilizei este projeto para aplicar conceitos modernos de arquitetura, focando em:

- ✅ Arquitetura REST seguindo os princípios SOLID
- ✅ Autenticação e autorização com Spring Security + JWT
- ✅ Persistência de dados com Spring Data JPA e PostgreSQL
- ✅ Containerização com Docker e Docker Compose
- ✅ Boas práticas de segurança (BCrypt, usuário não-root, health checks)
- ✅ Separação de camadas (Controller → Service → Repository)
- ✅ DTOs para proteção das entidades
- ✅ Tratamento centralizado de exceções

---

## ✨ Funcionalidades Planejadas e Implementadas

## Auxílio ao Responsável

* 🔔 **Alertas de Vacinação:** Sistema de notificações via e-mail e alertas internos para lembrar a data das próximas doses.
* 📋 **Histórico e Alergias:** Registro detalhado de vacinas aplicadas e controle de alergias para evitar reações adversas.
* 📸 **Cópia de Segurança:** Possibilidade de armazenar fotos da carteira física, prevenindo a perda de informações caso o documento original seja extraviado ou esquecido.

### Autenticação e Autorização
- 🔐 **Registro de usuários** com validação de dados
- 🔑 **Login** com geração de token JWT
- 🛡️ **Proteção de endpoints** via Spring Security
- 🔒 **Hash de senhas** com BCrypt

### Gestão de Dados
- 👤 **Usuários**: CRUD completo com validações
- 👥 **Pessoas**: Gerenciamento de perfis pessoais
- 👨‍👩‍👧 **Responsáveis**: Vínculo entre usuários e pessoas (ex.: pais, mães, tutores)
- 💉 **Registros vacinais**: Histórico de vacinas e alergias *(em desenvolvimento)*
- 📷 **Foto da carteira**: Armazenamento de imagem da carteira física *(em desenvolvimento)*

### Notificações e Alertas
- 🔔 **Alertas de próximas vacinas** via notificação e/ou e-mail *(em desenvolvimento)*
- 📅 **Calendário vacinal** com datas previstas para cada dose *(em desenvolvimento)*

### Monitoramento
- ❤️ **Health checks** via Spring Actuator
- 📊 **Métricas da aplicação** (CPU, memória, requisições)

---

## 🛠️ Stack Tecnológica

### Back-end
| Tecnologia              | Versão     | Finalidade                                    |
|-------------------------|------------|-----------------------------------------------|
| **Java**                | 21         | Linguagem principal                           |
| **Spring Boot**         | 4.0.2      | Framework para aplicações Java                |
| **Spring Data JPA**     | (incluído) | ORM para persistência de dados                |
| **Spring Security**     | (incluído) | Autenticação e autorização                    |
| **Spring Validation**   | (incluído) | Validação de entrada                          |
| **Spring Actuator**     | (incluído) | Monitoramento e métricas                      |
| **JJWT**                | 0.11.5     | Geração e validação de tokens JWT             |
| **Lombok**              | (incluído) | Redução de código boilerplate                 |

### Banco de Dados
| Tecnologia      | Versão     | Finalidade                                    |
|-----------------|------------|-----------------------------------------------|
| **PostgreSQL**  | 16 Alpine  | Banco de dados relacional principal           |
| **H2 Database** | (test)     | Banco em memória para testes                  |
| **HikariCP**    | (incluído) | Pool de conexões de alta performance          |

### DevOps
| Tecnologia         | Versão | Finalidade                                    |
|--------------------|--------|-----------------------------------------------|
| **Docker**         | 20+    | Containerização da aplicação                  |
| **Docker Compose** | 2.0+   | Orquestração de múltiplos containers          |
| **Maven**          | 3.9+   | Gerenciamento de dependências e build         |

---

## 🏗️ Arquitetura do Projeto

### Estrutura de Pacotes

```
src/main/java/com/moisesvn/carteira_vacinacao_api/
├── config/             → Configurações (CORS, beans, etc.)
├── controller/         → Camada HTTP (recebe requisições e delega ao Service)
├── dto/                → Data Transfer Objects (entrada/saída da API)
├── exception/          → Tratamento centralizado de exceções
├── mapper/             → Conversão entre entidades e DTOs
├── model/              → Entidades JPA (mapeamento com o banco de dados)
├── repository/         → Interfaces Spring Data JPA
├── security/           → Configuração JWT, filtros e UserDetailsService
└── service/            → Regras de negócio e orquestração
```

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

## 📦 Modelo de Dados

### Principais Entidades

**Usuario**
- `id` (PK)
- `email` (único)
- `password` (hash BCrypt)
- `role` (enum: USER, ADMIN)

**Pessoa**
- `id` (PK)
- `nome`
- `dataNascimento`
- `cpf` (único)
- `genero`

**Responsavel**
- `id` (PK)
- `usuarioId` (FK → Usuario)
- `pessoaId` (FK → Pessoa)
- `tipoRelacao` (ex.: PAI, MAE, TUTOR)

> **Diagrama EER disponível em:** `/EER Diagram/V1.0/`

---

## 🚀 Como Executar

### Pré-requisitos

- **Java 21** ou superior ([Download OpenJDK](https://openjdk.org/))
- **Docker** e **Docker Compose** ([Instalação](https://docs.docker.com/get-docker/))
- **Maven 3.9+** (opcional, apenas para build manual)

### Variáveis de Ambiente (Opcional)

Crie um arquivo `.env` na raiz do projeto (já está no `.gitignore`):

```bash
# Identificador do projeto
COMPOSE_PROJECT_NAME=carteira-vacinacao

# PostgreSQL
POSTGRES_DB=carteira_vacinacao_db
POSTGRES_USER=usuario_app
POSTGRES_PASSWORD=senha_segura_123
POSTGRES_PORT=5432

# Spring Boot
SPRING_PROFILE=dev
JPA_DDL_AUTO=update
JPA_SHOW_SQL=true

# JWT
JWT_SECRET=sua-chave-secreta-base64-aqui-com-minimo-256-bits
JWT_EXPIRATION=86400000

# Aplicação
APP_PORT=8080
TZ=America/Sao_Paulo
```

### Execução com Docker (Recomendado)

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/MoisesVNdev/api-carteira-vacinacao.git
   cd api-carteira-vacinacao
   ```

2. **Inicie os containers:**
   ```bash
   docker-compose up -d
   ```

3. **Verifique os logs:**
   ```bash
   docker-compose logs -f app
   ```

4. **Acesse a aplicação:**
   - API: `http://localhost:8080`
   - Health Check: `http://localhost:8080/actuator/health`

5. **Pare os containers:**
   ```bash
   docker-compose down
   ```

### Execução Manual (Sem Docker)

1. **Configure o PostgreSQL:**
   ```sql
   CREATE DATABASE carteira_vacinacao_db;
   CREATE USER usuario_app WITH PASSWORD 'senha_segura_123';
   GRANT ALL PRIVILEGES ON DATABASE carteira_vacinacao_db TO usuario_app;
   ```

2. **Configure o `application.yml`:**
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/carteira_vacinacao_db
       username: usuario_app
       password: senha_segura_123
   ```

3. **Compile e execute:**
   ```bash
   ./mvnw clean package -DskipTests
   java -jar target/carteira-vacinacao-api-0.0.1-SNAPSHOT.jar
   ```

---

## 📡 Endpoints da API

### Autenticação (Público)

#### Registro de Usuário
```bash
POST /auth/register
Content-Type: application/json

{
  "email": "usuario@exemplo.com",
  "password": "senha123",
  "confirmPassword": "senha123"
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "email": "usuario@exemplo.com",
  "role": "USER",
  "createdAt": "2026-02-20T10:30:00"
}
```

#### Login
```bash
POST /auth/login
Content-Type: application/json

{
  "email": "usuario@exemplo.com",
  "password": "senha123"
}
```

**Resposta (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000
}
```

---

### Usuários (Autenticado)

#### Listar Usuários
```bash
GET /api/usuarios
Authorization: Bearer {token}
```

#### Buscar Usuário por ID
```bash
GET /api/usuarios/1
Authorization: Bearer {token}
```

#### Atualizar Usuário
```bash
PUT /api/usuarios/1
Authorization: Bearer {token}
Content-Type: application/json

{
  "email": "novo-email@exemplo.com",
  "password": "novaSenha123"
}
```

#### Deletar Usuário
```bash
DELETE /api/usuarios/1
Authorization: Bearer {token}
```

---

### Pessoas (Autenticado)

#### Criar Pessoa
```bash
POST /api/pessoas
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "João Silva",
  "dataNascimento": "1990-05-15",
  "cpf": "12345678901",
  "genero": "MASCULINO"
}
```

#### Listar Todas as Pessoas
```bash
GET /api/pessoas
Authorization: Bearer {token}
```

#### Buscar Pessoa por ID
```bash
GET /api/pessoas/1
Authorization: Bearer {token}
```

#### Atualizar Pessoa
```bash
PUT /api/pessoas/1
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "João Silva Santos",
  "dataNascimento": "1990-05-15",
  "cpf": "12345678901",
  "genero": "MASCULINO"
}
```

#### Deletar Pessoa
```bash
DELETE /api/pessoas/1
Authorization: Bearer {token}
```

---

### Responsáveis (Autenticado)

#### Vincular Responsável
```bash
POST /api/responsaveis
Authorization: Bearer {token}
Content-Type: application/json

{
  "usuarioId": 1,
  "pessoaId": 2,
  "tipoRelacao": "PAI"
}
```

#### Listar Responsáveis de um Usuário
```bash
GET /api/responsaveis/usuario/1
Authorization: Bearer {token}
```

#### Buscar Responsável por ID
```bash
GET /api/responsaveis/1
Authorization: Bearer {token}
```

#### Deletar Vínculo
```bash
DELETE /api/responsaveis/1
Authorization: Bearer {token}
```

---

### Monitoramento (Público)

#### Health Check
```bash
GET /actuator/health
```

**Resposta:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP"
    }
  }
}
```

#### Métricas
```bash
GET /actuator/metrics
```

---

## 🧪 Testes

### Executar Testes Unitários

```bash
# Com Maven Wrapper
./mvnw test

# Com Maven instalado
mvn test
```

### Executar Testes com Cobertura

```bash
./mvnw test jacoco:report
```

> **Relatório de cobertura:** `target/site/jacoco/index.html`

---

## 📁 Estrutura de Arquivos

```
carteira-vacinacao-api/
├── Client REST testes/      → Arquivos .http para testar os endpoints
├── docker/
│   ├── postgres/init/       → Scripts SQL de inicialização
│   └── COMANDOS-UTEIS.md
├── EER Diagram/             → Diagrama Entidade-Relacionamento
├── src/
│   ├── main/
│   │   ├── java/.../        → Código-fonte da aplicação
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application.properties
│   └── test/                → Testes unitários e de integração
├── target/                  → Arquivos compilados (não versionado)
├── .gitignore
├── docker-compose.yml       → Orquestração de containers
├── Dockerfile               → Imagem multi-stage da aplicação
├── pom.xml                  → Dependências Maven
└── README.md                → Este arquivo
```

---

## 🚢 Deploy

### Heroku

```bash
# Instalar o Heroku CLI
heroku login

# Criar a aplicação
heroku create nome-da-sua-app

# Adicionar o PostgreSQL
heroku addons:create heroku-postgresql:mini

# Configurar variáveis de ambiente
heroku config:set JWT_SECRET="sua-chave-secreta"
heroku config:set SPRING_PROFILES_ACTIVE=prod

# Deploy
git push heroku main
```

### Docker Hub

```bash
# Build da imagem
docker build -t seu-usuario/carteira-vacinacao-api:latest .

# Login no Docker Hub
docker login

# Push da imagem
docker push seu-usuario/carteira-vacinacao-api:latest
```

### Railway / Render

1. Conecte o repositório do GitHub
2. Configure as variáveis de ambiente
3. Defina o comando de build: `mvn clean package -DskipTests`
4. Defina o comando de start: `java -jar target/*.jar`

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
✅ **HTTPS** recomendado em produção (configurar via proxy reverso)

### Melhorias Sugeridas para Produção

- [ ] Implementar refresh tokens
- [ ] Adicionar rate limiting (ex.: Bucket4J)
- [ ] Configurar o CORS adequadamente
- [ ] Implementar auditoria de operações
- [ ] Adicionar logs estruturados (JSON)
- [ ] Configurar SSL/TLS no PostgreSQL
- [ ] Implementar backup automático do banco de dados
- [ ] Adicionar testes de carga (JMeter, Gatling)

---

## 🛤️ Roadmap

- [x] Autenticação JWT
- [x] CRUD de Usuários
- [x] CRUD de Pessoas
- [x] Vínculo de Responsáveis
- [ ] Gestão de Vacinas (calendário vacinal)
- [ ] Registro de Doses Aplicadas
- [ ] Histórico de alergias
- [ ] Armazenamento de foto da carteira de vacinação física
- [ ] Alertas e notificações de doses pendentes (e-mail)
- [ ] Geração de certificados em PDF
- [ ] API de consulta pública (carteira digital)
- [ ] Integração com sistemas de saúde (HL7/FHIR)

---

## 📚 Documentação Adicional

- **Instruções de Camadas:** `.github/instructions/`
- **Comandos Úteis Docker:** `docker/COMANDOS-UTEIS.md`
- **Diagrama EER:** `EER Diagram/README.md`
- **Exemplos de Requisições:** `Client REST testes/`

---

## 🧑‍💻 Sobre o Desenvolvedor

Este projeto foi desenvolvido como parte do meu portfólio de desenvolvedor back-end júnior. Com ele, busco demonstrar minha capacidade de construir APIs RESTful bem estruturadas, seguras e documentadas, aplicando boas práticas do mercado desde o início do desenvolvimento.

**Competências demonstradas neste projeto:**

- ✅ Desenvolvimento de APIs REST com Java e Spring Boot
- ✅ Arquitetura em camadas e princípios SOLID
- ✅ Segurança com JWT e Spring Security
- ✅ Persistência de dados com JPA/Hibernate
- ✅ Containerização com Docker
- ✅ Código limpo e documentação técnica

**Autor:** Moisés Vila Nova de Oliveira  
**GitHub:** [MoisesVNdev](https://github.com/MoisesVNdev)  
**LinkedIn:** [linkedin.com/in/moisesvndev](https://www.linkedin.com/in/moisesvnoliveira/)  
**E-mail:** [moisesvn.dev@gmail.com](mailto:moisesvn.dev@gmail.com)

> "Este software é uma ferramenta de apoio. Sempre mantenha sua carteira de vacinação física em local seguro."
---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 🤝 Contribuições

Contribuições são muito bem-vindas! Sinta-se à vontade para abrir issues, sugerir melhorias ou enviar pull requests seguindo o fluxo abaixo:

1. Faça um fork do projeto
2. Crie uma branch para a sua feature (`git checkout -b feature/MinhaFeature`)
3. Faça o commit das suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Envie para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## ⭐ Agradecimentos

- Comunidade Spring Boot
- Documentação oficial do Spring Framework
- Tutoriais e cursos que serviram de inspiração para este projeto

---

**⚡ Desenvolvido com Java, Spring Boot e ❤️**