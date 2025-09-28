-- Migração para criação da tabela de aceites estáticos
-- Versão: 2.0
-- Data: 2024-09-28

-- Tabela para aceites de documentos estáticos (termos e políticas)
CREATE TABLE aceites_estaticos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo_documento VARCHAR(20) NOT NULL,
    versao VARCHAR(20) NOT NULL,
    aceito BOOLEAN NOT NULL,
    data_aceite TIMESTAMP NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(1000),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_aceites_estaticos_usuario (usuario_id),
    INDEX idx_aceites_estaticos_tipo (tipo_documento),
    INDEX idx_aceites_estaticos_versao (versao),
    INDEX idx_aceites_estaticos_data (data_aceite),
    UNIQUE KEY uk_aceites_estaticos_usuario_tipo_versao (usuario_id, tipo_documento, versao),
    
    CONSTRAINT chk_tipo_documento CHECK (tipo_documento IN ('TERMO', 'POLITICA'))
);
