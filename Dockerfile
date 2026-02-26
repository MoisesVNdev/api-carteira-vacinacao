# ============================================================================
# DOCKERFILE MULTI-STAGE - SPRING BOOT APPLICATION
# ============================================================================
# Este Dockerfile utiliza multi-stage build para criar uma imagem otimizada
# e segura da aplicação Spring Boot.
#
# Benefícios:
# - Imagem final menor (sem ferramentas de build)
# - Build em camadas (aproveitamento de cache)
# - Execução como usuário não-root
# - Separação de dependências e código
# ============================================================================

# ============================================================================
# ESTÁGIO 1: BUILD
# ============================================================================
# Usa Amazon Corretto (distribuição OpenJDK mantida pela AWS)
# Geralmente possui menos vulnerabilidades que outras distribuições
FROM amazoncorretto:21-alpine AS build

# -----------------------------------------------------------------------
# SEGURANÇA: Instala Maven e atualiza pacotes do sistema
# -----------------------------------------------------------------------
RUN apk update && apk upgrade --no-cache && \
    apk add --no-cache maven

# Define o diretório de trabalho
WORKDIR /app

# -----------------------------------------------------------------------
# OTIMIZAÇÃO: Copia apenas pom.xml primeiro
# -----------------------------------------------------------------------
# Isso permite que o Docker cache as dependências separadamente do código
# As dependências só serão baixadas novamente se o pom.xml mudar
COPY pom.xml .

# Baixa as dependências (esta camada será cacheada)
RUN mvn dependency:go-offline -B

# -----------------------------------------------------------------------
# Copia o código-fonte
# -----------------------------------------------------------------------
COPY src ./src

# -----------------------------------------------------------------------
# Compila a aplicação
# -----------------------------------------------------------------------
# -DskipTests: Pula os testes (execute em seu CI/CD)
# -B: Modo batch (menos verbose)
# clean: Limpa builds anteriores
# package: Cria o JAR
RUN mvn clean package -DskipTests -B

# ============================================================================
# ESTÁGIO 2: RUNTIME
# ============================================================================
# Usa Amazon Corretto Alpine (distribuição OpenJDK mantida pela AWS)
# Imagem otimizada e com melhor suporte de segurança
FROM amazoncorretto:21-alpine

# -----------------------------------------------------------------------
# METADADOS
# -----------------------------------------------------------------------
LABEL maintainer="seu-email@exemplo.com"
LABEL description="Spring Boot Application"
LABEL version="1.0.0"

# -----------------------------------------------------------------------
# SEGURANÇA: Atualiza pacotes do sistema para corrigir vulnerabilidades
# -----------------------------------------------------------------------
RUN apk update && apk upgrade --no-cache

# -----------------------------------------------------------------------
# INSTALAÇÃO DE FERRAMENTAS ESSENCIAIS
# -----------------------------------------------------------------------
# wget: Para health checks
# tzdata: Para configuração de timezone
# dumb-init: Para gerenciamento correto de processos
RUN apk add --no-cache \
    wget \
    tzdata \
    dumb-init

# -----------------------------------------------------------------------
# CRIAÇÃO DE USUÁRIO NÃO-ROOT
# -----------------------------------------------------------------------
# SEGURANÇA: Nunca execute aplicações como root
# Cria um grupo e usuário específico para a aplicação
RUN addgroup -g 1000 spring && \
    adduser -D -u 1000 -G spring spring

# Define o diretório de trabalho
WORKDIR /app

# -----------------------------------------------------------------------
# COPIA O JAR DO ESTÁGIO DE BUILD
# -----------------------------------------------------------------------
# Copia o JAR compilado do estágio anterior
COPY --from=build --chown=spring:spring /app/target/*.jar app.jar

# -----------------------------------------------------------------------
# CONFIGURAÇÕES DE SEGURANÇA
# -----------------------------------------------------------------------
# Muda propriedade do diretório para o usuário não-root
RUN chown -R spring:spring /app

# Troca para o usuário não-root
USER spring:spring

# -----------------------------------------------------------------------
# EXPÕE A PORTA DA APLICAÇÃO
# -----------------------------------------------------------------------
# Porta padrão do Spring Boot
EXPOSE 8080

# -----------------------------------------------------------------------
# HEALTH CHECK
# -----------------------------------------------------------------------
# Verifica se a aplicação está respondendo
# Ajuste o endpoint conforme necessário
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# -----------------------------------------------------------------------
# COMANDO DE EXECUÇÃO
# -----------------------------------------------------------------------
# dumb-init: Gerencia processos corretamente (sinais, zombies, etc)
# -Djava.security.egd: Melhora performance de geração de números aleatórios
# -XX:+UseContainerSupport: Detecta limites de memória do container
ENTRYPOINT ["dumb-init", "--"]
CMD ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -XX:+UseContainerSupport -jar app.jar"]

# ============================================================================
# INSTRUÇÕES DE USO
# ============================================================================
# 
# BUILD:
# docker build -t minha-api:latest .
#
# RUN (standalone):
# docker run -p 8080:8080 \
#   -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/db \
#   -e SPRING_DATASOURCE_USERNAME=usuario \
#   -e SPRING_DATASOURCE_PASSWORD=senha \
#   minha-api:latest
#
# BUILD COM COMPOSE:
# docker-compose build
#
# RUN COM COMPOSE:
# docker-compose up -d
# ============================================================================
