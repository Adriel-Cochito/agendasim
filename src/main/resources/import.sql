-- EMPRESA
INSERT INTO empresas (id, nome, email, telefone, cnpj, ativo, created_at) VALUES (1, 'Empresa Exemplo', 'empresa@exemplo.com', '+55 31 99999-8888', '12.345.678/0001-99', true, NOW());

-- PROFISSIONAL
INSERT INTO profissionais (id, nome, email, google_access_token, google_refresh_token, empresa_id, ativo, created_at, updated_at) VALUES (1, 'João Silva', 'joao@empresa.com', null, null, 1, true, NOW(), NOW());

-- SERVIÇO
INSERT INTO servicos (id, titulo, descricao, preco, duracao, empresa_id, ativo, created_at, updated_at) VALUES (1, 'Corte de Cabelo', 'Corte profissional com máquina e tesoura', 50.00, 30, 1, true, NOW(), NOW());

-- AGENDA
INSERT INTO agendas (id, nome_cliente, telefone_cliente, empresa_id, profissional_id, servico_id, data_hora, status, created_at, updated_at) VALUES (1, 'Cliente Teste', '+55 31 98888-7777', 1, 1, 1, '2025-06-21 10:00:00', 'AGENDADO', NOW(), NOW());

-- DISPONIBILIDADE - GRADE
INSERT INTO disponibilidades (id, tipo, hora_inicio, hora_fim, profissional_id, empresa_id, observacao, created_at, updated_at) VALUES (1, 'GRADE', '09:00:00', '18:00:00', 1, 1, 'Disponível durante a semana', NOW(), NOW());

-- GRADE - diasSemana (segunda a sexta: 1-5)
INSERT INTO disponibilidade_dias_semana (disponibilidade_id, dia_semana) VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5);

-- DISPONIBILIDADE - BLOQUEIO
INSERT INTO disponibilidades (id, tipo, data_hora_inicio, data_hora_fim, profissional_id, empresa_id, observacao, created_at, updated_at) VALUES (2, 'BLOQUEIO', '2025-06-22 12:00:00', '2025-06-22 14:00:00', 1, 1, 'Intervalo para almoço', NOW(), NOW());

-- DISPONIBILIDADE - LIBERADO
INSERT INTO disponibilidades (id, tipo, data_hora_inicio, data_hora_fim, profissional_id, empresa_id, observacao, created_at, updated_at) VALUES (3, 'LIBERADO', '2025-06-23 08:00:00', '2025-06-23 10:00:00', 1, 1, 'Horário extra liberado', NOW(), NOW());
