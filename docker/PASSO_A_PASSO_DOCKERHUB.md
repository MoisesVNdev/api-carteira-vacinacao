# 🐳 Guia Completo: Publicar API no DockerHub (Nível Sênior)

Este guia mostra como publicar sua API no DockerHub seguindo as melhores práticas de um desenvolvedor sênior: versionamento semântico, CI/CD automatizado, segurança e documentação.

---

## 📋 Índice

1. [Preparação Inicial](#1-preparação-inicial)
2. [Primeira Publicação Manual](#2-primeira-publicação-manual)
3. [Configurar CI/CD Automatizado](#3-configurar-cicd-automatizado)
4. [Releases e Versionamento](#4-releases-e-versionamento)
5. [Monitoramento e Segurança](#5-monitoramento-e-segurança)
6. [Manutenção e Updates](#6-manutenção-e-updates)

---

## 1. Preparação Inicial

### 1.1 Criar Conta no DockerHub

1. Acesse: https://hub.docker.com/
2. Clique em **Sign Up**
3. Escolha um username (ex: `moisesvndev`)
4. Verifique seu email

### 1.2 Criar Access Token (Mais Seguro que Senha)

⚠️ **NUNCA use sua senha diretamente em scripts ou CI/CD!**

1. Acesse: https://hub.docker.com/settings/security
2. Clique em **New Access Token**
3. Nome: `github-actions-api-vacinas`
4. Permissões: **Read & Write**
5. Clique em **Generate**
6. ⚠️ **COPIE O TOKEN AGORA** - ele não será exibido novamente!
7. Salve em local seguro (ex: gerenciador de senhas)

### 1.3 Verificar Pré-requisitos Locais

```bash
# 1. Docker instalado e rodando
docker --version
# Saída esperada: Docker version 24.x.x ou superior

# 2. Docker Buildx (para multi-arquitetura)
docker buildx version
# Saída esperada: github.com/docker/buildx vx.x.x

# 3. Git configurado
git --version
git config user.name
git config user.email

# 4. (Opcional) Trivy para scan de vulnerabilidades
trivy --version
# Se não tiver: brew install trivy (macOS) ou apt install trivy (Ubuntu)
```

---

## 2. Primeira Publicação Manual

### 2.1 Login no DockerHub

```bash
# Login usando o token (recomendado)
docker login -u moisesvndev

# Cole o token quando solicitado
# NÃO digite a senha da conta!
```

Ou use variável de ambiente:

```bash
echo $DOCKERHUB_TOKEN | docker login -u moisesvndev --password-stdin
```

Verificar login:
```bash
docker info | grep Username
# Saída: Username: moisesvndev
```

### 2.2 Testar Build Local

```bash
# Build de teste (sem push)
docker build -t carteira-vacinacao-api:test .

# Verificar tamanho da imagem
docker images | grep carteira-vacinacao-api
# Objetivo: < 500MB (build multi-stage otimizado)
```

**Opção A: Testar com docker-compose (Mais Fácil!)**

```bash
# Usar o docker-compose.yml do projeto
docker-compose up -d

# Aguardar inicialização
sleep 15

# Testar health check
curl http://localhost:8080/actuator/health

# Testar registro de usuário
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Test User",
    "email": "test@test.com",
    "senha": "Test@123456"
  }'

# Parar
docker-compose down
```

**Opção B: Testar apenas a imagem com banco separado**

```bash
# 1. Subir PostgreSQL temporário
docker run -d \
  --name postgres-test \
  -e POSTGRES_DB=test_db \
  -e POSTGRES_USER=test \
  -e POSTGRES_PASSWORD=test \
  -p 5432:5432 \
  postgres:16-alpine

# 2. Aguardar PostgreSQL iniciar
sleep 10

# 3. Testar a imagem
docker run --rm -p 8080:8080 \
  --link postgres-test:postgres \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/test_db \
  -e SPRING_DATASOURCE_USERNAME=test \
  -e SPRING_DATASOURCE_PASSWORD=test \
  -e JWT_SECRET=test_secret_key_minimum_32_characters_long \
  -e SPRING_PROFILES_ACTIVE=dev \
  carteira-vacinacao-api:test

# 4. Em outro terminal, testar
curl http://localhost:8080/actuator/health

# 5. Limpar
docker stop postgres-test && docker rm postgres-test
```

**Opção C: Apenas verificar a imagem (sem executar)**

⚠️ **Nota:** A aplicação vai falhar sem banco, mas confirma que o build funcionou.

```bash
# Ver informações da imagem
docker inspect carteira-vacinacao-api:test

# Confirmar usuário não-root (segurança)
docker run --rm carteira-vacinacao-api:test whoami
# Saída esperada: spring
```

### 2.3 Build e Push com Script Profissional

Tornando o script executável:

```bash
chmod +x docker/build-and-push.sh
```

Primeira publicação (versão 1.0.0):

```bash
./docker/build-and-push.sh 1.0.0
```

O script vai:
- ✅ Validar versionamento semântico
- ✅ Verificar login no DockerHub
- ✅ Verificar Git status
- ✅ Build multi-arquitetura (amd64 + arm64)
- ✅ Aplicar múltiplas tags (1.0.0, 1.0, 1, latest)
- ✅ Push para DockerHub
- ✅ Criar tag no Git (v1.0.0)
- ✅ Escanear vulnerabilidades (se Trivy instalado)

### 2.4 Verificar no DockerHub

1. Acesse: https://hub.docker.com/r/moisesvndev/carteira-vacinacao-api
2. Verifique:
   - ✅ Tags criadas (latest, 1.0.0, 1.0, 1)
   - ✅ Tamanho da imagem
   - ✅ Data de publicação
   - ✅ Arquiteturas suportadas (amd64, arm64)

---

## 3. Configurar CI/CD Automatizado

### 3.1 Adicionar Secrets no GitHub

1. Acesse seu repositório: https://github.com/MoisesVNdev/api-carteira-vacinacao
2. Vá para: **Settings** → **Secrets and variables** → **Actions**
3. Clique em **New repository secret**

Adicione 2 secrets:

**Secret 1:**
- Name: `DOCKERHUB_USERNAME`
- Value: `moisesvndev`

**Secret 2:**
- Name: `DOCKERHUB_TOKEN`
- Value: `cole_o_token_aqui_que_voce_criou_no_passo_1.2`

### 3.2 Workflow já está pronto!

O arquivo `.github/workflows/docker-publish.yml` já foi criado e contém:

- ✅ Testes automatizados
- ✅ Build multi-arquitetura
- ✅ Versionamento semântico
- ✅ Scan de vulnerabilidades
- ✅ Atualização automática do README do DockerHub

### 3.3 Testar o Workflow

**Opção A: Push para main (trigger automático)**

```bash
git add .
git commit -m "chore: configurar CI/CD para DockerHub"
git push origin main
```

**Opção B: Criar release tag**

```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

**Opção C: Executar manualmente**

1. Vá para: https://github.com/MoisesVNdev/api-carteira-vacinacao/actions
2. Clique em **Docker Build and Publish**
3. Clique em **Run workflow**
4. Escolha a branch e execute

### 3.4 Acompanhar Execução

1. Acesse: https://github.com/MoisesVNdev/api-carteira-vacinacao/actions
2. Clique no workflow em execução
3. Acompanhe os logs em tempo real
4. Verifique o resumo ao final

---

## 4. Releases e Versionamento

### 4.1 Versionamento Semântico (SemVer)

Formato: `MAJOR.MINOR.PATCH`

- **MAJOR** (1.x.x): Mudanças incompatíveis (breaking changes)
- **MINOR** (x.1.x): Novas funcionalidades (compatíveis)
- **PATCH** (x.x.1): Bug fixes e melhorias

Exemplos:
- `1.0.0` - Primeira versão estável
- `1.1.0` - Adicionou endpoint de relatórios
- `1.1.1` - Corrigiu bug no cálculo de datas
- `2.0.0` - Mudou estrutura de DTOs (breaking)

### 4.2 Criar Nova Versão

```bash
# 1. Fazer mudanças no código
git add .
git commit -m "feat: adicionar endpoint de relatórios"

# 2. Criar tag de versão
git tag -a v1.1.0 -m "Release version 1.1.0 - Relatórios"

# 3. Push do código + tag
git push origin main
git push origin v1.1.0

# 4. CI/CD vai buildar e publicar automaticamente!
```

### 4.3 Criar Release no GitHub

1. Acesse: https://github.com/MoisesVNdev/api-carteira-vacinacao/releases
2. Clique em **Draft a new release**
3. Escolha a tag: `v1.1.0`
4. Release title: `v1.1.0 - Relatórios e Melhorias`
5. Descrição:

```markdown
## 🚀 O que há de novo

### ✨ Novas Funcionalidades
- Endpoint de relatórios vacinais dos pacientes
- Exportação de dados em PDF

### 🐛 Correções
- Corrigido cálculo de idade em anos bissextos
- Ajustado formato de data no registro de vacinas

### 📦 Docker
\`\`\`bash
docker pull moisesvndev/carteira-vacinacao-api:1.1.0
\`\`\`

### 📋 Breaking Changes
Nenhuma mudança incompatível.

---
**Full Changelog**: https://github.com/MoisesVNdev/api-carteira-vacinacao/compare/v1.0.0...v1.1.0
```

6. Clique em **Publish release**

---

## 5. Monitoramento e Segurança

### 5.1 Scan de Vulnerabilidades Automático

O workflow já escaneia com Trivy. Para ver resultados:

1. Acesse: https://github.com/MoisesVNdev/api-carteira-vacinacao/security
2. Clique em **Code scanning**
3. Veja vulnerabilidades encontradas

### 5.2 DockerHub Automated Security Scans

1. Acesse: https://hub.docker.com/r/moisesvndev/carteira-vacinacao-api/general
2. Ative **Advanced Image Analysis**
3. Veja relatório de vulnerabilidades

### 5.3 Badges no README

Adicione badges para mostrar qualidade:

```markdown
![Docker Image Size](https://img.shields.io/docker/image-size/moisesvndev/carteira-vacinacao-api/latest)
![Docker Pulls](https://img.shields.io/docker/pulls/moisesvndev/carteira-vacinacao-api)
![Docker Build](https://github.com/MoisesVNdev/api-carteira-vacinacao/workflows/Docker%20Build%20and%20Publish/badge.svg)
```

### 5.4 Monitorar Downloads

```bash
# Ver estatísticas localmente
curl -s https://hub.docker.com/v2/repositories/moisesvndev/carteira-vacinacao-api/ | jq '.pull_count'
```

Ou acesse: https://hub.docker.com/r/moisesvndev/carteira-vacinacao-api/tags

---

## 6. Manutenção e Updates

### 6.1 Atualizar Imagem Existente

```bash
# Correção de bug (patch)
./docker/build-and-push.sh 1.1.1

# Nova feature (minor)
./docker/build-and-push.sh 1.2.0

# Breaking change (major)
./docker/build-and-push.sh 2.0.0
```

### 6.2 Remover Tag Antiga

```bash
# CUIDADO: Isso é irreversível!
docker rmi moisesvndev/carteira-vacinacao-api:old-tag

# No DockerHub (via web):
# 1. Acesse https://hub.docker.com/r/moisesvndev/carteira-vacinacao-api/tags
# 2. Clique no ícone de lixeira na tag desejada
```

### 6.3 Atualizar README do DockerHub

Edite o arquivo `docker/DOCKERHUB_README.md` e faça push. O CI/CD atualiza automaticamente!

### 6.4 Rollback em Caso de Problema

```bash
# Usuários podem voltar para versão anterior:
docker pull moisesvndev/carteira-vacinacao-api:1.0.0

# Ou criar nova tag apontando para versão antiga:
docker pull moisesvndev/carteira-vacinacao-api:1.0.0
docker tag moisesvndev/carteira-vacinacao-api:1.0.0 moisesvndev/carteira-vacinacao-api:latest
docker push moisesvndev/carteira-vacinacao-api:latest
```

---

## ✅ Checklist de Boas Práticas (Nível Sênior)

### Antes de Publicar
- [ ] Código testado localmente
- [ ] Build da imagem sem erros
- [ ] Imagem testada em container
- [ ] README atualizado com a versão
- [ ] CHANGELOG.md atualizado
- [ ] Git commit com mensagem semântica (feat:, fix:, chore:)

### Segurança
- [ ] Secrets configurados no GitHub (nunca no código!)
- [ ] Access token usado (não senha)
- [ ] Imagem escaneada para vulnerabilidades
- [ ] Usuário não-root no container (já configurado)
- [ ] Sem dados sensíveis na imagem

### Documentação
- [ ] README do DockerHub completo
- [ ] Variáveis de ambiente documentadas
- [ ] Quick start funcional
- [ ] Exemplos de uso testados
- [ ] Release notes detalhadas

### CI/CD
- [ ] Workflow configurado e testado
- [ ] Testes automatizados passando
- [ ] Multi-arquitetura habilitada (amd64, arm64)
- [ ] Versionamento semântico seguido
- [ ] Tags múltiplas aplicadas (latest, major, minor, patch)

### Manutenção
- [ ] Monitoramento de vulnerabilidades ativo
- [ ] Estatísticas de uso acompanhadas
- [ ] Suporte a issues no GitHub
- [ ] Versionamento claro e comunicado

---

## 🎯 Resultado Final

Seguindo este guia, você terá:

✅ **Imagem publicada no DockerHub** com múltiplas tags
✅ **CI/CD automatizado** via GitHub Actions
✅ **Versionamento semântico** profissional
✅ **Multi-arquitetura** (amd64 + arm64)
✅ **Segurança** com scan de vulnerabilidades
✅ **Documentação** completa e profissional
✅ **Badges** mostrando qualidade do projeto

---

## 📚 Referências

- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Semantic Versioning](https://semver.org/)
- [GitHub Actions for Docker](https://docs.github.com/en/actions/publishing-packages/publishing-docker-images)
- [Trivy Security Scanner](https://trivy.dev/)
- [Keep a Changelog](https://keepachangelog.com/)

---

## 🆘 Troubleshooting

### Erro: "denied: requested access to the resource is denied"

**Solução:**
```bash
docker logout
docker login -u moisesvndev
# Cole o access token
```

### Erro: "Connection to host.docker.internal:5432 refused"

**Causa:** Não há PostgreSQL rodando ou não está acessível do container.

**Soluções:**
```bash
# Solução 1: Usar docker-compose completo (RECOMENDADO)
docker-compose up -d

# Solução 2: Subir PostgreSQL separado
docker run -d --name postgres \
  -e POSTGRES_DB=carteira_vacinacao_db \
  -e POSTGRES_USER=usuario \
  -e POSTGRES_PASSWORD=senha \
  -p 5432:5432 \
  postgres:16-alpine

# Solução 3: Usar --link ao rodar a API
docker run --link postgres:postgres \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/... \
  moisesvndev/carteira-vacinacao-api:latest
```

### Erro: "no space left on device"

**Solução:**
```bash
docker system prune -a --volumes
```

### Workflow falha: "secrets.DOCKERHUB_TOKEN not found"

**Solução:**
1. Verifique se adicionou os secrets no GitHub (passo 3.1)
2. Nome do secret deve ser exatamente `DOCKERHUB_TOKEN`

### Imagem muito grande (> 1GB)

**Solução:**
- Dockerfile já usa multi-stage build
- Verifique se não está copiando arquivos desnecessários
- Use `.dockerignore` adequado

---

**🎉 Parabéns! Você agora publica imagens Docker como um desenvolvedor sênior!**
