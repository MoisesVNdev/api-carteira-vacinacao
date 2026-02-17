# 🛠️ COMANDOS ÚTEIS - DOCKER E DOCKER COMPOSE

## 📋 Índice Rápido

1. [Comandos Básicos](#comandos-básicos)
2. [Gerenciamento de Containers](#gerenciamento-de-containers)
3. [Logs e Debug](#logs-e-debug)
4. [Volumes e Dados](#volumes-e-dados)
5. [Redes](#redes)
6. [Performance e Limpeza](#performance-e-limpeza)
7. [Troubleshooting](#troubleshooting)

---

## 🚀 Comandos Básicos

### Iniciar Aplicação

```bash
# Iniciar todos os serviços (modo detached/background)
docker compose up -d

# Iniciar e ver logs em tempo real
docker compose up

# Iniciar apenas um serviço específico
docker compose up -d postgres
docker compose up -d app

# Rebuild e iniciar (após mudanças no Dockerfile ou código)
docker compose up -d --build

# Rebuild sem cache
docker compose build --no-cache
docker compose up -d
```

### Parar Aplicação

```bash
# Parar todos os serviços (mantém volumes)
docker compose down

# Parar e REMOVER volumes (CUIDADO: apaga dados!)
docker compose down -v

# Parar, remover volumes, redes e imagens órfãs
docker compose down -v --rmi all --remove-orphans

# Apenas pausar (não remove containers)
docker compose pause

# Despausar
docker compose unpause
```

### Status dos Serviços

```bash
# Ver status de todos os containers
docker compose ps

# Status detalhado
docker compose ps -a

# Ver apenas containers rodando
docker ps

# Ver todos os containers (incluindo parados)
docker ps -a
```

---

## 🔧 Gerenciamento de Containers

### Restart

```bash
# Reiniciar todos os serviços
docker compose restart

# Reiniciar apenas um serviço
docker compose restart app
docker compose restart postgres

# Reiniciar com timeout customizado (segundos)
docker compose restart -t 30 app
```

### Executar Comandos

```bash
# Entrar no shell do container da aplicação
docker compose exec app sh

# Entrar no shell do PostgreSQL
docker compose exec postgres sh

# Executar comando sem entrar no shell
docker compose exec app ls -la
docker compose exec postgres psql -U meu_usuario -d meu_banco

# Executar como usuário específico
docker compose exec -u root app sh
```

### Escalar Serviços

```bash
# Criar múltiplas instâncias da aplicação
docker compose up -d --scale app=3

# Verificar instâncias
docker compose ps app
```

---

## 📊 Logs e Debug

### Ver Logs

```bash
# Logs de todos os serviços
docker compose logs

# Logs em tempo real (follow)
docker compose logs -f

# Logs de um serviço específico
docker compose logs app
docker compose logs postgres

# Logs com timestamps
docker compose logs -t app

# Últimas N linhas de log
docker compose logs --tail=100 app

# Logs desde uma data específica
docker compose logs --since 2024-02-16 app

# Logs até uma data específica
docker compose logs --until 2024-02-16T10:00:00 app

# Exportar logs para arquivo
docker compose logs > logs-completos.txt
docker compose logs app > logs-app.txt
```

### Inspecionar Containers

```bash
# Informações detalhadas do container
docker inspect minha-api-spring

# Ver apenas o IP do container
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' minha-api-spring

# Ver variáveis de ambiente
docker inspect -f '{{.Config.Env}}' minha-api-spring

# Ver mounts/volumes
docker inspect -f '{{.Mounts}}' minha-api-spring
```

### Monitoramento em Tempo Real

```bash
# Ver uso de recursos (CPU, Memória, Rede)
docker stats

# Monitorar apenas um container
docker stats minha-api-spring

# Ver processos rodando no container
docker compose top
docker compose top app

# Ver eventos do Docker
docker events
docker events --filter container=minha-api-spring
```

---

## 💾 Volumes e Dados

### Listar Volumes

```bash
# Listar todos os volumes
docker volume ls

# Listar volumes do projeto
docker volume ls | grep minha-api

# Ver detalhes de um volume
docker volume inspect minha-api_postgres_data
```

### Gerenciar Volumes

```bash
# Criar volume manualmente
docker volume create meu-volume-custom

# Remover volume específico
docker volume rm minha-api_postgres_data

# Remover volumes não utilizados
docker volume prune

# Remover volumes não utilizados (sem confirmação)
docker volume prune -f
```

### Backup e Restore

```bash
# BACKUP do PostgreSQL
docker compose exec postgres pg_dump -U meu_usuario meu_banco > backup_$(date +%Y%m%d_%H%M%S).sql

# BACKUP com compressão
docker compose exec postgres pg_dump -U meu_usuario meu_banco | gzip > backup_$(date +%Y%m%d_%H%M%S).sql.gz

# RESTORE do backup
docker compose exec -T postgres psql -U meu_usuario meu_banco < backup_20240216_100000.sql

# RESTORE de backup comprimido
gunzip -c backup_20240216_100000.sql.gz | docker compose exec -T postgres psql -U meu_usuario meu_banco

# BACKUP do volume inteiro
docker run --rm \
  -v minha-api_postgres_data:/data \
  -v $(pwd):/backup \
  alpine tar czf /backup/postgres_volume_backup.tar.gz /data

# RESTORE do volume
docker run --rm \
  -v minha-api_postgres_data:/data \
  -v $(pwd):/backup \
  alpine tar xzf /backup/postgres_volume_backup.tar.gz -C /
```

---

## 🌐 Redes

### Listar Redes

```bash
# Listar todas as redes
docker network ls

# Ver detalhes de uma rede
docker network inspect minha-api_network
```

### Testar Conectividade

```bash
# Testar conexão do app para o postgres
docker compose exec app ping postgres

# Testar conexão HTTP
docker compose exec app wget --spider http://localhost:8080/actuator/health

# Verificar conectividade do banco
docker compose exec app nc -zv postgres 5432

# Ver portas expostas
docker compose port app 8080
docker compose port postgres 5432
```

---

## 🧹 Performance e Limpeza

### Limpeza Básica

```bash
# Remover containers parados
docker container prune

# Remover imagens não utilizadas
docker image prune

# Remover volumes não utilizados
docker volume prune

# Remover redes não utilizadas
docker network prune

# LIMPEZA COMPLETA (cuidado!)
docker system prune -a --volumes
```

### Informações de Uso

```bash
# Ver espaço em disco usado pelo Docker
docker system df

# Ver detalhado
docker system df -v

# Ver tamanho das imagens
docker images --format "{{.Repository}}:{{.Tag}}\t{{.Size}}"

# Ver tamanho dos volumes
docker volume ls -q | xargs docker volume inspect --format '{{ .Name }}: {{ .Mountpoint }}' | xargs -I {} sh -c 'echo -n "{}"; du -sh $(echo {} | cut -d: -f2) 2>/dev/null | cut -f1'
```

### Otimização

```bash
# Atualizar imagens base
docker compose pull

# Rebuild com cache otimizado
docker compose build --pull

# Limpar cache de build
docker builder prune

# Limpar tudo mantendo volumes
docker system prune -a --filter "label!=keep"
```

---

## 🔍 Troubleshooting

### Problemas com Containers

```bash
# Container não inicia - ver logs
docker compose logs app

# Container crashando - ver últimos logs
docker compose logs --tail=50 app

# Ver erros de build
docker compose build app 2>&1 | grep -i error

# Forçar recreação do container
docker compose up -d --force-recreate app

# Remover container específico e recriar
docker compose rm -f app
docker compose up -d app
```

### Problemas de Conexão

```bash
# Verificar se o PostgreSQL está aceitando conexões
docker compose exec postgres pg_isready -U meu_usuario

# Testar conexão da aplicação ao banco
docker compose exec app nc -zv postgres 5432

# Ver configurações de rede
docker network inspect minha-api_network

# Verificar DNS interno
docker compose exec app nslookup postgres
docker compose exec app ping -c 3 postgres
```

### Problemas de Permissão

```bash
# Ver usuário rodando no container
docker compose exec app whoami

# Ver permissões de arquivos
docker compose exec app ls -la /app

# Ajustar permissões (se necessário)
docker compose exec -u root app chown -R spring:spring /app

# Entrar como root para debug
docker compose exec -u root app sh
```

### Reset Completo

```bash
# 1. Parar tudo
docker compose down -v

# 2. Remover volumes
docker volume rm minha-api_postgres_data
docker volume rm minha-api_maven_cache

# 3. Remover imagens
docker rmi $(docker images -q 'minha-api*')

# 4. Limpar sistema
docker system prune -a -f

# 5. Rebuild do zero
docker compose build --no-cache
docker compose up -d
```

---

## 🐘 Comandos Específicos do PostgreSQL

### Acesso ao Banco

```bash
# Entrar no psql
docker compose exec postgres psql -U meu_usuario -d meu_banco

# Executar query diretamente
docker compose exec postgres psql -U meu_usuario -d meu_banco -c "SELECT version();"

# Listar bancos de dados
docker compose exec postgres psql -U meu_usuario -c "\l"

# Listar tabelas
docker compose exec postgres psql -U meu_usuario -d meu_banco -c "\dt"

# Descrever estrutura de uma tabela
docker compose exec postgres psql -U meu_usuario -d meu_banco -c "\d nome_tabela"
```

### Manutenção do Banco

```bash
# Verificar tamanho do banco
docker compose exec postgres psql -U meu_usuario -d meu_banco -c "SELECT pg_size_pretty(pg_database_size('meu_banco'));"

# Ver conexões ativas
docker compose exec postgres psql -U meu_usuario -d meu_banco -c "SELECT * FROM pg_stat_activity;"

# Matar conexões ativas (cuidado!)
docker compose exec postgres psql -U meu_usuario -d meu_banco -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'meu_banco' AND pid <> pg_backend_pid();"

# Vacuum do banco
docker compose exec postgres psql -U meu_usuario -d meu_banco -c "VACUUM ANALYZE;"

# Reindex
docker compose exec postgres psql -U meu_usuario -d meu_banco -c "REINDEX DATABASE meu_banco;"
```

---

## ☕ Comandos Específicos da Aplicação Java

### Maven

```bash
# Executar testes
docker compose exec app mvn test

# Compilar sem testes
docker compose exec app mvn clean package -DskipTests

# Verificar dependências desatualizadas
docker compose exec app mvn versions:display-dependency-updates

# Ver árvore de dependências
docker compose exec app mvn dependency:tree
```

### Debug da JVM

```bash
# Ver informações da JVM
docker compose exec app java -version

# Ver propriedades do sistema
docker compose exec app java -XshowSettings:properties -version

# Thread dump
docker compose exec app jstack 1

# Heap dump (cuidado com o tamanho!)
docker compose exec app jmap -dump:format=b,file=/tmp/heap.hprof 1
```

---

## 📦 Aliases Úteis

Adicione ao seu `~/.bashrc` ou `~/.zshrc`:

```bash
# Aliases Docker Compose
alias dc='docker compose'
alias dcu='docker compose up -d'
alias dcd='docker compose down'
alias dcl='docker compose logs -f'
alias dcp='docker compose ps'
alias dcr='docker compose restart'
alias dcb='docker compose up -d --build'

# Aliases Docker
alias dps='docker ps'
alias dpsa='docker ps -a'
alias di='docker images'
alias dv='docker volume ls'
alias dn='docker network ls'
alias dclean='docker system prune -a -f'

# Funções úteis
dexec() { docker compose exec "$1" sh; }
dlogs() { docker compose logs -f "$1"; }
```

Uso:
```bash
dcu        # docker compose up -d
dcl app    # docker compose logs -f app
dexec app  # docker compose exec app sh
```

---

## 🆘 Comandos de Emergência

```bash
# Parar TUDO no Docker
docker stop $(docker ps -aq)

# Remover TUDO (containers, volumes, redes, imagens)
docker stop $(docker ps -aq)
docker rm $(docker ps -aq)
docker rmi $(docker images -q)
docker volume rm $(docker volume ls -q)
docker network rm $(docker network ls -q)

# Reiniciar serviço Docker (Linux)
sudo systemctl restart docker

# Verificar logs do daemon Docker
sudo journalctl -u docker

# Ver processos Docker no host
ps aux | grep docker
```

---

## 📚 Recursos Adicionais

- [Docker Compose CLI Reference](https://docs.docker.com/compose/reference/)
- [Docker CLI Reference](https://docs.docker.com/engine/reference/commandline/cli/)
- [PostgreSQL CLI Reference](https://www.postgresql.org/docs/current/app-psql.html)

---

**💡 Dica:** Use `docker compose --help` ou `docker --help` para ver todos os comandos disponíveis.
