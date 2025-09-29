-- Criar tabela de chamados de suporte
CREATE TABLE chamados_suporte (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    descricao VARCHAR(2000) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    subcategoria VARCHAR(50) NOT NULL,
    prioridade VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ABERTO',
    email_usuario VARCHAR(100) NOT NULL,
    nome_usuario VARCHAR(100) NOT NULL,
    pagina_erro VARCHAR(100),
    resposta_suporte VARCHAR(2000),
    usuario_suporte VARCHAR(100),
    data_resposta TIMESTAMP,
    avaliacao_nota INT,
    avaliacao_comentario VARCHAR(500),
    empresa_id BIGINT,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE SET NULL,
    
    INDEX idx_email_usuario (email_usuario),
    INDEX idx_empresa_id (empresa_id),
    INDEX idx_status (status),
    INDEX idx_prioridade (prioridade),
    INDEX idx_categoria (categoria),
    INDEX idx_data_criacao (data_criacao)
);

-- Criar tabela de anexos dos chamados
CREATE TABLE chamado_anexos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    chamado_id BIGINT NOT NULL,
    url_anexo VARCHAR(500) NOT NULL,
    
    FOREIGN KEY (chamado_id) REFERENCES chamados_suporte(id) ON DELETE CASCADE,
    
    INDEX idx_chamado_id (chamado_id)
);

-- Inserir dados iniciais para categorias de suporte (opcional)
-- Estes dados podem ser mantidos no código Java ou inseridos aqui
INSERT INTO chamados_suporte (titulo, descricao, categoria, subcategoria, prioridade, email_usuario, nome_usuario, status) 
VALUES 
('Sistema de Suporte Inicializado', 'Este é um chamado de exemplo para demonstrar o funcionamento do sistema de suporte.', 'outros', 'outros_geral', 'BAIXA', 'sistema@agendasim.com.br', 'Sistema', 'FECHADO');
