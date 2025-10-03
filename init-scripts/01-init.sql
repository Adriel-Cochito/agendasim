-- ============================================================================
-- Script de inicialização do banco de dados PostgreSQL
-- ============================================================================

-- Criar extensões necessárias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Configurações de timezone
SET timezone = 'America/Sao_Paulo';

-- Configurações de encoding
SET client_encoding = 'UTF8';

-- Log da inicialização
DO $$
BEGIN
    RAISE NOTICE 'Database initialized successfully for AgendaSim at %', now();
END $$;
