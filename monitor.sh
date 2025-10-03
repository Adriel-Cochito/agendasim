#!/bin/bash

# ============================================================================
# Script de Monitoramento da Aplicação
# ============================================================================

# Cores
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Carregar variáveis do .env
if [ -f .env ]; then
    source .env
fi

API_PORT=${API_PORT:-8080}

echo -e "${BLUE}=== Status do AgendaSim ===${NC}"
echo "Data: $(date)"
echo

# Status dos containers
echo -e "${YELLOW}📦 Status dos Containers:${NC}"
docker-compose ps
echo

# Health check da API
echo -e "${YELLOW}🏥 Health Check da API:${NC}"
if curl -s -f http://localhost:$API_PORT/api/actuator/health > /dev/null; then
    echo -e "${GREEN}✅ API está saudável${NC}"
    
    # Mostrar informações detalhadas
    echo -e "${BLUE}Detalhes:${NC}"
    curl -s http://localhost:$API_PORT/api/actuator/health | jq '.' 2>/dev/null || curl -s http://localhost:$API_PORT/api/actuator/health
else
    echo -e "${RED}❌ API não está respondendo${NC}"
fi
echo

# Status do banco de dados
echo -e "${YELLOW}🗄️  Status do PostgreSQL:${NC}"
if docker exec agendasim-postgres pg_isready -U ${POSTGRES_USER:-postgres} > /dev/null 2>&1; then
    echo -e "${GREEN}✅ PostgreSQL está funcionando${NC}"
else
    echo -e "${RED}❌ PostgreSQL não está respondendo${NC}"
fi
echo

# Uso de recursos
echo -e "${YELLOW}📊 Uso de Recursos:${NC}"
docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.MemPerc}}"
echo

# Logs recentes
echo -e "${YELLOW}📋 Últimos logs da aplicação:${NC}"
docker-compose logs --tail=10 agendasim-api
echo

# Espaço em disco
echo -e "${YELLOW}💾 Espaço em Disco:${NC}"
df -h | grep -E '(Filesystem|/dev/)'
echo

# Volume do PostgreSQL
echo -e "${YELLOW}🗂️  Volume do PostgreSQL:${NC}"
docker volume ls | grep agendasim
echo

echo -e "${BLUE}=== Fim do Status ===${NC}"
