-- Adicionar coluna nome_unico na tabela empresas
ALTER TABLE empresas ADD COLUMN nome_unico VARCHAR(50) NOT NULL DEFAULT '';

-- Criar índice único para nome_unico
CREATE UNIQUE INDEX idx_empresas_nome_unico ON empresas(nome_unico);

-- Comentário explicativo
COMMENT ON COLUMN empresas.nome_unico IS 'Nome único da empresa usado para links de agendamento público';
