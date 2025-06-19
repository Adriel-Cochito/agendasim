
INSERT INTO empresas (nome, email, telefone, cnpj) VALUES ('Empresa A', 'contato@empresaA.com', '+55 31 91234-5678', '12.345.678/0001-90');


INSERT INTO profissionais (nome, email, google_access_token, google_refresh_token, empresa_id) VALUES ('João Silva', 'joao@empresaA.com', NULL, NULL, 1);


INSERT INTO servicos (titulo, descricao, preco, duracao, empresa_id) VALUES ('Corte de Cabelo', 'Corte profissional masculino', 50.00, 30, 1);


-- INSERT INTO disponibilidades (tipo, data_hora_inicio, data_hora_fim, profissional_id, empresa_id, observacao) VALUES ('GRADE', '2025-06-18 08:00:00', '2025-06-18 12:00:00', 1, 1, 'Atendimento matutino');

INSERT INTO agendas (nome_cliente, telefone_cliente, empresa_id, profissional_id, servico_id, data_hora, status) VALUES ('Carlos Mendes', '+55 31 91111-2222', 1, 1, 1, '2025-06-18 09:00:00', 'CONFIRMADO');
