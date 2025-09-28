-- Adicionar campos LGPD na tabela agendas
ALTER TABLE agendas 
ADD COLUMN email_cliente VARCHAR(255),
ADD COLUMN endereco_cliente VARCHAR(200),
ADD COLUMN cpf_cliente VARCHAR(14),
ADD COLUMN observacoes TEXT,
ADD COLUMN data_agendamento DATE;

-- Criar índices para melhorar performance das consultas LGPD
CREATE INDEX idx_agendas_nome_telefone ON agendas(nome_cliente, telefone_cliente);
CREATE INDEX idx_agendas_data_agendamento ON agendas(data_agendamento);

-- Atualizar data_agendamento baseado na dataHora existente (se necessário)
UPDATE agendas 
SET data_agendamento = CAST(data_hora AS DATE)
WHERE data_agendamento IS NULL;
