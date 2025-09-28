-- Migração para criação das tabelas LGPD
-- Versão: 1.0
-- Data: 2024-01-01

-- Tabela para termos de uso com versionamento
CREATE TABLE termos_aceite (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    versao VARCHAR(20) NOT NULL UNIQUE,
    titulo VARCHAR(200) NOT NULL,
    conteudo TEXT NOT NULL,
    resumo_alteracoes VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'RASCUNHO',
    ativo BOOLEAN NOT NULL DEFAULT FALSE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_publicacao TIMESTAMP NULL,
    data_inativacao TIMESTAMP NULL,
    criado_por VARCHAR(100),
    aprovado_por VARCHAR(100),
    
    CONSTRAINT chk_status_termos CHECK (status IN ('RASCUNHO', 'PENDENTE_APROVACAO', 'ATIVO', 'INATIVO', 'ARQUIVADO')),
    CONSTRAINT chk_versao_termos CHECK (versao REGEXP '^[0-9]+\\.[0-9]+(\\.[0-9]+)?$')
);

-- Tabela para políticas de privacidade com versionamento
CREATE TABLE politicas_privacidade (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    versao VARCHAR(20) NOT NULL UNIQUE,
    titulo VARCHAR(200) NOT NULL,
    conteudo TEXT NOT NULL,
    resumo_alteracoes VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'RASCUNHO',
    ativo BOOLEAN NOT NULL DEFAULT FALSE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_publicacao TIMESTAMP NULL,
    data_inativacao TIMESTAMP NULL,
    criado_por VARCHAR(100),
    aprovado_por VARCHAR(100),
    
    CONSTRAINT chk_status_politicas CHECK (status IN ('RASCUNHO', 'PENDENTE_APROVACAO', 'ATIVO', 'INATIVO', 'ARQUIVADO')),
    CONSTRAINT chk_versao_politicas CHECK (versao REGEXP '^[0-9]+\\.[0-9]+(\\.[0-9]+)?$')
);

-- Tabela para aceites de termos de uso
CREATE TABLE aceites_usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    termo_id BIGINT NOT NULL,
    aceito BOOLEAN NOT NULL,
    data_aceite TIMESTAMP NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(1000),
    versao_aceita VARCHAR(20),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (termo_id) REFERENCES termos_aceite(id) ON DELETE CASCADE,
    INDEX idx_aceites_usuario (usuario_id),
    INDEX idx_aceites_termo (termo_id),
    INDEX idx_aceites_data (data_aceite)
);

-- Tabela para aceites de políticas de privacidade
CREATE TABLE aceites_politicas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    politica_id BIGINT NOT NULL,
    aceito BOOLEAN NOT NULL,
    data_aceite TIMESTAMP NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(1000),
    versao_aceita VARCHAR(20),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (politica_id) REFERENCES politicas_privacidade(id) ON DELETE CASCADE,
    INDEX idx_aceites_politica_usuario (usuario_id),
    INDEX idx_aceites_politica_politica (politica_id),
    INDEX idx_aceites_politica_data (data_aceite)
);

-- Tabela para consentimentos granulares
CREATE TABLE consentimentos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo_consentimento VARCHAR(50) NOT NULL,
    finalidade VARCHAR(100) NOT NULL,
    consentido BOOLEAN NOT NULL,
    data_consentimento TIMESTAMP NULL,
    data_revogacao TIMESTAMP NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(1000),
    versao_politica VARCHAR(20),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NULL,
    
    INDEX idx_consentimentos_usuario (usuario_id),
    INDEX idx_consentimentos_tipo (tipo_consentimento),
    INDEX idx_consentimentos_finalidade (finalidade),
    INDEX idx_consentimentos_data (data_consentimento),
    INDEX idx_consentimentos_ativo (usuario_id, tipo_consentimento, consentido, data_revogacao)
);

-- Tabela para logs de auditoria
CREATE TABLE log_auditoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NULL,
    acao VARCHAR(100) NOT NULL,
    tabela_afetada VARCHAR(50),
    registro_id BIGINT,
    dados_anteriores TEXT,
    dados_novos TEXT,
    ip_address VARCHAR(45),
    user_agent VARCHAR(1000),
    sessao_id VARCHAR(100),
    trace_id VARCHAR(50),
    nivel_risco VARCHAR(20) NOT NULL DEFAULT 'BAIXO',
    data_operacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_auditoria_usuario (usuario_id),
    INDEX idx_auditoria_acao (acao),
    INDEX idx_auditoria_tabela (tabela_afetada),
    INDEX idx_auditoria_data (data_operacao),
    INDEX idx_auditoria_risco (nivel_risco),
    INDEX idx_auditoria_trace (trace_id),
    
    CONSTRAINT chk_nivel_risco CHECK (nivel_risco IN ('BAIXO', 'MEDIO', 'ALTO', 'CRITICO'))
);

-- Tabela para solicitações LGPD
CREATE TABLE solicitacoes_lgpd (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo_solicitacao VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    descricao TEXT,
    dados_solicitados TEXT,
    resposta TEXT,
    data_solicitacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_resposta TIMESTAMP NULL,
    responsavel_resposta VARCHAR(100),
    prazo_resposta TIMESTAMP NULL,
    
    INDEX idx_solicitacoes_usuario (usuario_id),
    INDEX idx_solicitacoes_tipo (tipo_solicitacao),
    INDEX idx_solicitacoes_status (status),
    INDEX idx_solicitacoes_data (data_solicitacao),
    
    CONSTRAINT chk_tipo_solicitacao CHECK (tipo_solicitacao IN ('ACESSO', 'CORRECAO', 'PORTABILIDADE', 'ANONIMIZACAO', 'EXCLUSAO')),
    CONSTRAINT chk_status_solicitacao CHECK (status IN ('PENDENTE', 'EM_ANALISE', 'APROVADA', 'REJEITADA', 'CONCLUIDA'))
);

-- Tabela para violações de privacidade
CREATE TABLE violacoes_privacidade (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NULL,
    tipo_violacao VARCHAR(50) NOT NULL,
    descricao TEXT NOT NULL,
    dados_afetados TEXT,
    nivel_gravidade VARCHAR(20) NOT NULL DEFAULT 'BAIXO',
    status VARCHAR(20) NOT NULL DEFAULT 'DETECTADA',
    data_deteccao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_resolucao TIMESTAMP NULL,
    acoes_tomadas TEXT,
    responsavel_resolucao VARCHAR(100),
    notificacao_autoridade BOOLEAN DEFAULT FALSE,
    data_notificacao TIMESTAMP NULL,
    
    INDEX idx_violacoes_usuario (usuario_id),
    INDEX idx_violacoes_tipo (tipo_violacao),
    INDEX idx_violacoes_gravidade (nivel_gravidade),
    INDEX idx_violacoes_status (status),
    INDEX idx_violacoes_data (data_deteccao),
    
    CONSTRAINT chk_tipo_violacao CHECK (tipo_violacao IN ('ACESSO_NAO_AUTORIZADO', 'VAZAMENTO_DADOS', 'USO_INDEVIDO', 'RETENCAO_EXCESSIVA', 'OUTROS')),
    CONSTRAINT chk_gravidade_violacao CHECK (nivel_gravidade IN ('BAIXO', 'MEDIO', 'ALTO', 'CRITICO')),
    CONSTRAINT chk_status_violacao CHECK (status IN ('DETECTADA', 'EM_INVESTIGACAO', 'RESOLVIDA', 'ARQUIVADA'))
);

-- Inserir dados iniciais

-- Termo de uso inicial
INSERT INTO termos_aceite (versao, titulo, conteudo, resumo_alteracoes, status, ativo, criado_por) VALUES 
('1.0', 'Termos de Uso - AgendaSim', 
'<h1>Termos de Uso - AgendaSim</h1>
<p>Bem-vindo ao AgendaSim! Estes termos de uso regem o uso da nossa plataforma de agendamento.</p>
<h2>1. Aceitação dos Termos</h2>
<p>Ao utilizar nossa plataforma, você concorda com estes termos de uso.</p>
<h2>2. Uso da Plataforma</h2>
<p>Você pode usar nossa plataforma para agendar serviços e gerenciar sua agenda.</p>
<h2>3. Responsabilidades do Usuário</h2>
<p>Você é responsável por manter a confidencialidade de sua conta.</p>
<h2>4. Limitação de Responsabilidade</h2>
<p>Não nos responsabilizamos por danos indiretos decorrentes do uso da plataforma.</p>
<h2>5. Modificações</h2>
<p>Reservamo-nos o direito de modificar estes termos a qualquer momento.</p>',
'Versão inicial dos termos de uso',
'ATIVO', TRUE, 'SISTEMA');

-- Política de privacidade inicial
INSERT INTO politicas_privacidade (versao, titulo, conteudo, resumo_alteracoes, status, ativo, criado_por) VALUES 
('1.0', 'Política de Privacidade - AgendaSim',
'<h1>Política de Privacidade - AgendaSim</h1>
<p>Esta política descreve como coletamos, usamos e protegemos suas informações pessoais.</p>
<h2>1. Informações que Coletamos</h2>
<p>Coletamos informações como nome, email, telefone e dados de agendamento.</p>
<h2>2. Como Usamos suas Informações</h2>
<p>Utilizamos suas informações para fornecer nossos serviços e melhorar sua experiência.</p>
<h2>3. Compartilhamento de Informações</h2>
<p>Não compartilhamos suas informações pessoais com terceiros sem seu consentimento.</p>
<h2>4. Segurança dos Dados</h2>
<p>Implementamos medidas de segurança para proteger suas informações.</p>
<h2>5. Seus Direitos</h2>
<p>Você tem o direito de acessar, corrigir e excluir suas informações pessoais.</p>
<h2>6. Contato</h2>
<p>Para dúvidas sobre esta política, entre em contato conosco.</p>',
'Versão inicial da política de privacidade',
'ATIVO', TRUE, 'SISTEMA');

-- Criar índices adicionais para performance
CREATE INDEX idx_termos_ativo ON termos_aceite(ativo, status);
CREATE INDEX idx_politicas_ativa ON politicas_privacidade(ativo, status);
CREATE INDEX idx_consentimentos_ativo_consentido ON consentimentos(usuario_id, tipo_consentimento, consentido, data_revogacao);
CREATE INDEX idx_auditoria_usuario_data ON log_auditoria(usuario_id, data_operacao);
CREATE INDEX idx_auditoria_acao_data ON log_auditoria(acao, data_operacao);
