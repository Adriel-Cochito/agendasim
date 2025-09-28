-- Migração para criação da tabela de aceites de documentos estáticos
-- Versão: 2.0
-- Data: 2024-09-28

CREATE TABLE aceites_estaticos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo_documento VARCHAR(50) NOT NULL, -- Ex: TERMO, POLITICA
    versao VARCHAR(20) NOT NULL,
    aceito BOOLEAN NOT NULL,
    data_aceite TIMESTAMP NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(1000),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_aceites_estaticos_usuario (usuario_id),
    INDEX idx_aceites_estaticos_tipo_versao (tipo_documento, versao),
    UNIQUE (usuario_id, tipo_documento, versao) -- Garante um único aceite por usuário para uma versão de documento
);