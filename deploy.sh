#!/bin/bash

# ============================================================================
# Script de Deploy para VPS Hostinger
# ============================================================================

set -e  # Parar em caso de erro

echo "🚀 Iniciando deploy do AgendaSim..."

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para log colorido
log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

warn() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] WARNING: $1${NC}"
}

error() {
    echo -e "${RED}[$(date +'%Y-%m-%d %H:%M:%S')] ERROR: $1${NC}"
    exit 1
}

# Verificar se o arquivo .env existe
if [ ! -f .env ]; then
    error "Arquivo .env não encontrado! Copie o env.example para .env e configure as variáveis."
fi

log "Carregando variáveis de ambiente..."
source .env

# Verificar variáveis obrigatórias
if [ -z "$POSTGRES_PASSWORD" ] || [ -z "$JWT_SECRET" ]; then
    error "Variáveis POSTGRES_PASSWORD e JWT_SECRET são obrigatórias no arquivo .env"
fi

# Criar diretórios necessários se não existirem
log "Criando diretórios necessários..."
mkdir -p logs
mkdir -p backups

# Parar containers existentes (se houver)
log "Parando containers existentes..."
docker-compose down --remove-orphans || true

# Limpar imagens antigas (opcional - descomente se quiser)
# log "Removendo imagens antigas..."
# docker system prune -f

# Build e start dos containers
log "Construindo e iniciando containers..."
docker-compose up --build -d

# Aguardar containers ficarem saudáveis
log "Aguardando containers ficarem saudáveis..."
timeout=300  # 5 minutos
elapsed=0
interval=10

while [ $elapsed -lt $timeout ]; do
    if docker-compose ps | grep -q "healthy"; then
        log "✅ Containers estão saudáveis!"
        break
    fi
    
    warn "Aguardando containers ficarem saudáveis... (${elapsed}s/${timeout}s)"
    sleep $interval
    elapsed=$((elapsed + interval))
done

if [ $elapsed -ge $timeout ]; then
    error "Timeout: Containers não ficaram saudáveis em ${timeout}s"
fi

# Verificar logs
log "Últimos logs da aplicação:"
docker-compose logs --tail=20 agendasim-api

# Testar endpoint de health
log "Testando endpoint de saúde..."
sleep 10  # Aguardar um pouco mais

if curl -f http://localhost:${API_PORT:-8080}/api/actuator/health > /dev/null 2>&1; then
    log "✅ API está respondendo corretamente!"
else
    warn "⚠️  API pode não estar respondendo ainda. Verifique os logs."
fi

# Mostrar status final
log "Status dos containers:"
docker-compose ps

log "🎉 Deploy concluído!"
log "📊 Acesse o health check em: http://localhost:${API_PORT:-8080}/api/actuator/health"
log "📋 Para ver logs: docker-compose logs -f agendasim-api"
log "🛑 Para parar: docker-compose down"
