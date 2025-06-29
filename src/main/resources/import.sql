-- EMPRESA
INSERT INTO empresas (nome, email, telefone, cnpj, ativo, created_at) VALUES ('Empresa Exemplo', 'empresa@exemplo.com', '+55 31 99999-8888', '12.345.678/0001-99', true, NOW());

-- PROFISSIONAL
INSERT INTO profissionais (nome, email, senha, perfil, google_access_token, google_refresh_token, empresa_id, ativo, created_at) VALUES ('João Silva', 'joao@empresa.com', '$2a$10$eJlE.0VdkiUH8AgN/8PqsOb8tNsYHIVVGPpxFOZAciGvAX5IgIBoq', 'OWNER', null, null, 1, true, NOW());
INSERT INTO profissionais (nome, email, senha, perfil, google_access_token, google_refresh_token, empresa_id, ativo, created_at) VALUES ('Vitor', 'vitor@empresa.com', '$2a$10$eJlE.0VdkiUH8AgN/8PqsOb8tNsYHIVVGPpxFOZAciGvAX5IgIBoq', 'ADMIN', null, null, 1, true, NOW());
INSERT INTO profissionais (nome, email, senha, perfil, google_access_token, google_refresh_token, empresa_id, ativo, created_at) VALUES ('Juca', 'juca@empresa.com', '$2a$10$eJlE.0VdkiUH8AgN/8PqsOb8tNsYHIVVGPpxFOZAciGvAX5IgIBoq', 'USER', null, null, 1, true, NOW());

-- SERVIÇO
INSERT INTO servicos (titulo, descricao, preco, duracao, empresa_id, ativo, created_at) VALUES ('Corte de Cabelo', 'Corte profissional com máquina e tesoura', 50.00, 30, 1, true, NOW());

-- Associa Servico com Profissional
INSERT INTO servico_profissional (servico_id, profissional_id) VALUES (1, 1);
INSERT INTO servico_profissional (servico_id, profissional_id) VALUES (1, 2);
INSERT INTO servico_profissional (servico_id, profissional_id) VALUES (1, 3);


-- AGENDA
INSERT INTO agendas (nome_cliente, telefone_cliente, empresa_id, profissional_id, servico_id, data_hora, status, created_at) VALUES ('Cliente Teste', '+55 31 98888-7777', 1, 1, 1, '2025-06-21 10:00:00', 'AGENDADO', NOW());
INSERT INTO agendas (nome_cliente, telefone_cliente, empresa_id, profissional_id, servico_id, data_hora, status, created_at) VALUES ('Cliente Teste', '+55 31 98888-7777', 1, 1, 1, '2025-06-23 09:00:00', 'AGENDADO', NOW());


-- DISPONIBILIDADE - GRADE
INSERT INTO disponibilidades (tipo, hora_inicio, hora_fim, profissional_id, empresa_id, observacao, created_at) VALUES ('GRADE', '09:00:00', '18:00:00', 1, 1, 'Disponível durante a semana', NOW());

-- GRADE - diasSemana (segunda a sexta: 1-5)
INSERT INTO disponibilidade_dias_semana (disponibilidade_id, dia_semana) VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5);

-- DISPONIBILIDADE - BLOQUEIO
INSERT INTO disponibilidades (tipo, data_hora_inicio, data_hora_fim, profissional_id, empresa_id, observacao, created_at) VALUES ('BLOQUEIO', '2025-06-22 12:00:00', '2025-06-22 14:00:00', 1, 1, 'Intervalo para almoço', NOW());

-- DISPONIBILIDADE - LIBERADO
INSERT INTO disponibilidades (tipo, data_hora_inicio, data_hora_fim, profissional_id, empresa_id, observacao, created_at) VALUES ('LIBERADO', '2025-06-23 08:00:00', '2025-06-23 10:00:00', 1, 1, 'Horário extra liberado', NOW());
