#!/bin/bash

# ============================================================================
# Script de Backup do Banco de Dados PostgreSQL
# ============================================================================

set -e

# Configurações
BACKUP_DIR="./backups"
DATE=$(date +%Y%m%d_%H%M%S)
CONTAINER_NAME="agendasim-postgres"

# Carregar variáveis do .env
if [ -f .env ]; then
    source .env
fi

DB_NAME=${POSTGRES_DB:-agendasim}
DB_USER=${POSTGRES_USER:-postgres}

# Criar diretório de backup se não existir
mkdir -p $BACKUP_DIR

echo "🗄️  Iniciando backup do banco de dados..."
echo "Database: $DB_NAME"
echo "Data: $(date)"

# Fazer backup
BACKUP_FILE="$BACKUP_DIR/agendasim_backup_$DATE.sql"

docker exec $CONTAINER_NAME pg_dump -U $DB_USER -d $DB_NAME > $BACKUP_FILE

if [ $? -eq 0 ]; then
    echo "✅ Backup criado com sucesso: $BACKUP_FILE"
    
    # Comprimir o backup
    gzip $BACKUP_FILE
    echo "📦 Backup comprimido: $BACKUP_FILE.gz"
    
    # Remover backups antigos (manter apenas os últimos 7 dias)
    find $BACKUP_DIR -name "agendasim_backup_*.sql.gz" -mtime +7 -delete
    echo "🧹 Backups antigos removidos (>7 dias)"
    
    # Mostrar tamanho do backup
    BACKUP_SIZE=$(du -h "$BACKUP_FILE.gz" | cut -f1)
    echo "📊 Tamanho do backup: $BACKUP_SIZE"
    
else
    echo "❌ Erro ao criar backup!"
    exit 1
fi
