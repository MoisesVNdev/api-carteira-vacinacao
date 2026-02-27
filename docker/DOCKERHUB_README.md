# 📋 API Carteira de Vacinação Digital

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.3-brightgreen?logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![License](https://img.shields.io/badge/License-MIT-yellow)

API REST para gerenciamento de carteira de vacinação digital, desenvolvida com **Spring Boot 4.0.3** e **Java 21**. Fornece autenticação segura via JWT, gestão completa de usuários, pessoas, vacinas, esquemas vacinais, registros e alergias.

---

## 🚀 Quick Start

### Usando Docker Compose

```bash
# 1. Criar arquivo .env
cat > .env << EOF
POSTGRES_DB=carteira_vacinacao_db
POSTGRES_USER=usuario
POSTGRES_PASSWORD=$(openssl rand -base64 32)
JWT_SECRET=$(openssl rand -base64 64)
JWT_EXPIRATION=86400000
EOF

# 2. Criar docker-compose.yml
curl -O https://raw.githubusercontent.com/MoisesVNdev/api-carteira-vacinacao/main/docker-compose.dockerhub.yml

# 3. Executar
docker-compose -f docker-compose.dockerhub.yml up -d
```

### Usando Docker Run

```bash
# 1. PostgreSQL
docker run -d \
  --name postgres \
  -e POSTGRES_DB=carteira_vacinacao_db \
  -e POSTGRES_USER=usuario \
  -e POSTGRES_PASSWORD=senha_forte \
  -p 5432:5432 \
  postgres:16-alpine

# 2. API
docker run -d \
  --name carteira-vacinacao-api \
  --link postgres:postgres \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/carteira_vacinacao_db \
  -e SPRING_DATASOURCE_USERNAME=usuario \
  -e SPRING_DATASOURCE_PASSWORD=senha_forte \
  -e JWT_SECRET=sua_chave_secreta_jwt_minimo_32_caracteres \
  -e SPRING_PROFILES_ACTIVE=prod \
  -p 8080:8080 \
  moisevndev/carteira-vacinacao-api:latest
```

---

## 📋 Variáveis de Ambiente Obrigatórias

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `SPRING_DATASOURCE_URL` | URL JDBC do PostgreSQL | `jdbc:postgresql://postgres:5432/carteira_vacinacao_db` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `admin_vacinas` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | `senha_forte_aqui` |
| `JWT_SECRET` | Chave secreta JWT (min 32 chars) | `sua_chave_secreta...` |

### Variáveis Opcionais

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `SPRING_PROFILES_ACTIVE` | Perfil Spring | `prod` |
| `JWT_EXPIRATION` | Expiração do token (ms) | `86400000` (24h) |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Modo DDL | `none` |
| `SPRING_JPA_SHOW_SQL` | Exibir SQL no log | `false` |
| `JAVA_OPTS` | Opções da JVM | `-Xms256m -Xmx512m` |

---

## 🌐 Endpoints Principais

Após iniciar, acesse:

- **API Base:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Health Check:** http://localhost:8080/actuator/health

### Autenticação

```bash
# Registrar novo usuário
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "senha": "SenhaForte@123"
  }'

# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "senha": "SenhaForte@123"
  }'
```

---

## 🏗️ Arquitetura

- **Backend:** Java 21 + Spring Boot 4.0.3
- **Segurança:** Spring Security + JWT (JJWT 0.11.5)
- **ORM:** Spring Data JPA + Hibernate
- **Migrations:** Flyway
- **Banco de Dados:** PostgreSQL 16
- **Containerização:** Docker Multi-stage Build
- **Usuário não-root:** Executado como `spring:spring` (UID 1000)

---

## 📊 Recursos

### Funcionalidades Principais

- ✅ Autenticação JWT com refresh token
- ✅ CRUD completo de usuários
- ✅ Gestão de pessoas (crianças, adolescentes, adultos)
- ✅ Vínculo responsável-pessoa
- ✅ Cadastro de vacinas (base PNI pré-carregada)
- ✅ Esquemas vacinais por faixa etária
- ✅ Registro de vacinas aplicadas
- ✅ Gestão de alergias
- ✅ Validação de dados com Bean Validation
- ✅ Tratamento de exceções padronizado
- ✅ Documentação interativa (Swagger)

### Segurança

- 🔒 Senhas com hash BCrypt
- 🔒 Tokens JWT assinados
- 🔒 Validação de entrada em todos os endpoints
- 🔒 Headers de segurança (CORS, CSP)
- 🔒 Usuário não-root no container
- 🔒 Sem capacidades privilegiadas

---

## 🔧 Health Checks

A aplicação expõe health checks via Actuator:

```bash
curl http://localhost:8080/actuator/health
```

Resposta esperada:
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
    }
  }
}
```

---

## 🏷️ Tags Disponíveis

- `latest` - Versão mais recente (branch main)
- `develop` - Versão de desenvolvimento
- `1.x.x` - Versões específicas (semantic versioning)
- `sha-xxxxxxx` - Builds por commit SHA

### Exemplos

```bash
# Última versão estável
docker pull moisevndev/carteira-vacinacao-api:latest

# Versão específica
docker pull moisevndev/carteira-vacinacao-api:1.0.0

# Versão de desenvolvimento
docker pull moisevndev/carteira-vacinacao-api:develop
```

---

## 📚 Documentação Completa

- **GitHub:** [MoisesVNdev/api-carteira-vacinacao](https://github.com/MoisesVNdev/api-carteira-vacinacao)
- **README:** Documentação completa com exemplos
- **Swagger UI:** Disponível em `/swagger-ui.html` após iniciar
- **Testes REST:** Collection Postman disponível no repositório

---

## 🐛 Issues e Suporte

Encontrou um bug ou tem uma sugestão?

- **Issues:** [GitHub Issues](https://github.com/MoisesVNdev/api-carteira-vacinacao/issues)
- **Discussions:** [GitHub Discussions](https://github.com/MoisesVNdev/api-carteira-vacinacao/discussions)

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o [LICENSE](https://github.com/MoisesVNdev/api-carteira-vacinacao/blob/main/LICENSE) para mais detalhes.

---

## 👨‍💻 Autor

**Moisés Vila Nova de Oliveira**

- GitHub: [@MoisesVNdev](https://github.com/MoisesVNdev)
- LinkedIn: [moisesvnoliveira](https://www.linkedin.com/in/moisesvnoliveira/)
- Email: moisesvn.dev@gmail.com

---

## ⚠️ Aviso Legal

Esta aplicação é uma **ferramenta de apoio** para controle vacinal familiar. Não substitui a carteira de vacinação física oficial. Sempre mantenha sua carteira física em local seguro.

---

**🌟 Se este projeto foi útil, deixe uma estrela no [GitHub](https://github.com/MoisesVNdev/api-carteira-vacinacao)!**
