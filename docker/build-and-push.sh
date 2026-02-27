#!/usr/bin/env bash
# ============================================================================
# SCRIPT DE BUILD E PUSH PROFISSIONAL - DOCKERHUB
# ============================================================================
# Este script automatiza o processo de build e push de imagens Docker
# seguindo as melhores práticas de versionamento semântico e multi-tag.
#
# Uso:
#   ./build-and-push.sh <versão>
#
# Exemplos:
#   ./build-and-push.sh 1.0.0
#   ./build-and-push.sh 1.2.3
#   ./build-and-push.sh 2.0.0-beta.1
# ============================================================================

set -euo pipefail  # Exit on error, undefined vars, pipe failures

# ----------------------------------------------------------------------------
# CONFIGURAÇÕES
# ----------------------------------------------------------------------------
DOCKER_USERNAME="${DOCKER_USERNAME:-moisesvn}"
IMAGE_NAME="${IMAGE_NAME:-carteira-vacinacao-api}"
FULL_IMAGE="${DOCKER_USERNAME}/${IMAGE_NAME}"
PLATFORMS="linux/amd64,linux/arm64"

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# ----------------------------------------------------------------------------
# FUNÇÕES AUXILIARES
# ----------------------------------------------------------------------------
log_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

log_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

log_error() {
    echo -e "${RED}❌ $1${NC}"
}

show_usage() {
    cat << EOF
Uso: $0 <versão>

Exemplos:
  $0 1.0.0           # Build e push versão 1.0.0
  $0 1.2.3           # Build e push versão 1.2.3
  $0 2.0.0-alpha.1   # Build e push versão 2.0.0-alpha.1

Versão semântica: MAJOR.MINOR.PATCH[-PRERELEASE]
EOF
}

validate_semver() {
    local version=$1
    if [[ ! $version =~ ^[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9.]+)?$ ]]; then
        log_error "Versão inválida: $version"
        log_error "Use formato semântico: MAJOR.MINOR.PATCH (ex: 1.0.0)"
        return 1
    fi
    return 0
}

check_docker_login() {
    if ! docker info &> /dev/null; then
        log_error "Docker não está rodando!"
        exit 1
    fi
    
    log_info "Verificando login no DockerHub..."
    if ! docker info | grep -q "Username: ${DOCKER_USERNAME}"; then
        log_warning "Não está logado no DockerHub"
        log_info "Fazendo login..."
        docker login
    else
        log_success "Já está logado como ${DOCKER_USERNAME}"
    fi
}

check_git_status() {
    if [[ -n $(git status --porcelain) ]]; then
        log_warning "Existem mudanças não commitadas"
        read -p "Continuar mesmo assim? (s/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Ss]$ ]]; then
            log_info "Build cancelado"
            exit 0
        fi
    fi
}

# ----------------------------------------------------------------------------
# VALIDAÇÕES INICIAIS
# ----------------------------------------------------------------------------
if [[ $# -eq 0 ]]; then
    log_error "Versão não especificada!"
    echo
    show_usage
    exit 1
fi

VERSION=$1
if ! validate_semver "$VERSION"; then
    exit 1
fi

# Extrair componentes da versão
MAJOR=$(echo "$VERSION" | cut -d. -f1)
MINOR=$(echo "$VERSION" | cut -d. -f2)
PATCH=$(echo "$VERSION" | cut -d. -f3 | cut -d- -f1)

# ----------------------------------------------------------------------------
# BANNER
# ----------------------------------------------------------------------------
cat << "EOF"
╔═══════════════════════════════════════════════════════════════╗
║                                                               ║
║     🐳 Docker Build & Push - Carteira de Vacinação API       ║
║                                                               ║
╚═══════════════════════════════════════════════════════════════╝
EOF

echo
log_info "Imagem: ${FULL_IMAGE}"
log_info "Versão: ${VERSION}"
log_info "Plataformas: ${PLATFORMS}"
echo

# ----------------------------------------------------------------------------
# PRÉ-VERIFICAÇÕES
# ----------------------------------------------------------------------------
log_info "🔍 Executando pré-verificações..."

check_docker_login
check_git_status

# Verificar se a tag já existe no Git
if git rev-parse "v${VERSION}" >/dev/null 2>&1; then
    log_warning "Tag v${VERSION} já existe no Git"
    read -p "Deseja sobrescrever? (s/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Ss]$ ]]; then
        git tag -d "v${VERSION}"
        git push origin ":refs/tags/v${VERSION}" 2>/dev/null || true
    fi
fi

# Verificar se a imagem já existe no DockerHub (não é bloqueante)
if docker manifest inspect "${FULL_IMAGE}:${VERSION}" &> /dev/null; then
    log_warning "Versão ${VERSION} já existe no DockerHub"
    read -p "Continuar e sobrescrever? (s/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Ss]$ ]]; then
        log_info "Build cancelado"
        exit 0
    fi
fi

echo
log_success "Pré-verificações concluídas!"
echo

# ----------------------------------------------------------------------------
# BUILD DA IMAGEM
# ----------------------------------------------------------------------------
log_info "🔨 Iniciando build da imagem..."
echo

# Criar builder se não existir
if ! docker buildx inspect multiplatform-builder &> /dev/null; then
    log_info "Criando builder multiplatform..."
    docker buildx create --name multiplatform-builder --use
    docker buildx inspect --bootstrap
fi

# Tags que serão aplicadas
TAGS=(
    "${FULL_IMAGE}:${VERSION}"           # Ex: 1.2.3
    "${FULL_IMAGE}:${MAJOR}.${MINOR}"    # Ex: 1.2
    "${FULL_IMAGE}:${MAJOR}"             # Ex: 1
    "${FULL_IMAGE}:latest"               # latest
)

# Construir argumentos de tag
TAG_ARGS=""
for tag in "${TAGS[@]}"; do
    TAG_ARGS+="--tag $tag "
done

# Build e push
log_info "Tags que serão aplicadas:"
for tag in "${TAGS[@]}"; do
    echo "  • $tag"
done
echo

docker buildx build \
    --platform "${PLATFORMS}" \
    ${TAG_ARGS} \
    --push \
    --progress=plain \
    --build-arg JAVA_VERSION=21 \
    --label "org.opencontainers.image.version=${VERSION}" \
    --label "org.opencontainers.image.created=$(date -u +'%Y-%m-%dT%H:%M:%SZ')" \
    --label "org.opencontainers.image.revision=$(git rev-parse HEAD)" \
    .

echo
log_success "Build e push concluídos com sucesso!"
echo

# ----------------------------------------------------------------------------
# CRIAR TAG NO GIT
# ----------------------------------------------------------------------------
log_info "🏷️  Criando tag no Git..."

git tag -a "v${VERSION}" -m "Release version ${VERSION}"
git push origin "v${VERSION}"

log_success "Tag v${VERSION} criada e enviada ao GitHub"
echo

# ----------------------------------------------------------------------------
# VERIFICAR IMAGEM
# ----------------------------------------------------------------------------
log_info "🔍 Verificando imagem publicada..."

for tag in "${TAGS[@]}"; do
    if docker manifest inspect "$tag" &> /dev/null; then
        log_success "$tag - OK"
    else
        log_error "$tag - FALHOU"
    fi
done

echo

# ----------------------------------------------------------------------------
# ESCANEAR VULNERABILIDADES (Opcional, requer Trivy)
# ----------------------------------------------------------------------------
if command -v trivy &> /dev/null; then
    log_info "🔒 Escaneando vulnerabilidades com Trivy..."
    trivy image --severity HIGH,CRITICAL "${FULL_IMAGE}:${VERSION}" || true
    echo
else
    log_warning "Trivy não encontrado. Pule o scan de vulnerabilidades."
    log_info "Instale com: brew install trivy (macOS) ou apt install trivy (Ubuntu)"
    echo
fi

# ----------------------------------------------------------------------------
# RESUMO FINAL
# ----------------------------------------------------------------------------
cat << EOF
╔═══════════════════════════════════════════════════════════════╗
║                    ✅ BUILD CONCLUÍDO!                        ║
╚═══════════════════════════════════════════════════════════════╝

🐳 Imagem publicada: ${FULL_IMAGE}

📦 Tags disponíveis:
$(for tag in "${TAGS[@]}"; do echo "   • $tag"; done)

🔗 Links úteis:
   • DockerHub: https://hub.docker.com/r/${DOCKER_USERNAME}/${IMAGE_NAME}
   • GitHub: https://github.com/${DOCKER_USERNAME}/api-carteira-vacinacao

📋 Testar a imagem:
   docker pull ${FULL_IMAGE}:${VERSION}
   docker run -p 8080:8080 ${FULL_IMAGE}:${VERSION}

🚀 Próximos passos:
   1. Testar a imagem em um ambiente de staging
   2. Atualizar documentação com a nova versão
   3. Criar release notes no GitHub
   4. Notificar usuários sobre a atualização

EOF

log_success "Processo completo! 🎉"
