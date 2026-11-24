create database EcoDescate;
use EcoDescate;

-- Criação da tabela de Usuários (Comum e Admin)
CREATE TABLE usuarios (
    id int PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    cidade VARCHAR(100),
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Criação da tabela de Equipamentos
CREATE TABLE equipamentos (
    id int PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    icone VARCHAR(255),
    req_limp_dados BOOLEAN DEFAULT FALSE
);

-- Criação da tabela de Checklists (Passos de segurança)
CREATE TABLE checklists (
    id int PRIMARY KEY AUTO_INCREMENT,
    equipamento_id int NOT NULL,
    instrucao TEXT NOT NULL,
    ordem INT NOT NULL,
    CONSTRAINT fk_equipamento_checklist 
        FOREIGN KEY (equipamento_id) 
        REFERENCES equipamentos(id) 
        ON DELETE CASCADE
);

-- Criação da tabela de Pontos de Coleta
CREATE TABLE pontos_coleta (
    id int PRIMARY KEY,
    nome_local VARCHAR(150) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    bairro VARCHAR(100),
    cidade VARCHAR(100) NOT NULL,
    horario_atendimento VARCHAR(150),
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela Associativa N:N (Ecopontos aceitam múltiplos Equipamentos)
CREATE TABLE ponto_coleta_equipamento (
    ponto_coleta_id int NOT NULL,
    equipamento_id int NOT NULL,
    PRIMARY KEY (ponto_coleta_id, equipamento_id),
    CONSTRAINT fk_ponto 
        FOREIGN KEY (ponto_coleta_id) 
        REFERENCES pontos_coleta(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_equipamento 
        FOREIGN KEY (equipamento_id) 
        REFERENCES equipamentos(id) 
        ON DELETE CASCADE
);
select * from checklists;
-- 1. Inserindo tipos de equipamentos eletrônicos
INSERT INTO equipamentos (id, nome, categoria, icone, req_limp_dados) VALUES
(1, 'Smartphone / Celular', 'Telefonia e Portáteis', 'celular.png', true),
(2, 'Notebook / Laptop', 'Informática', 'notebook.png', true),
(3, 'Pilhas e Baterias', 'Energia e Acumuladores', 'bateria.png', false),
(4, 'Monitor / Televisão', 'Áudio e Vídeo', 'monitor.png', false),
(5, 'Cabos, Carregadores e Fones', 'Acessórios e Periféricos', 'cabos.png', false);

-- 2. Inserindo os passos do checklist vinculados a cada equipamento

-- Passos para Smartphone / Celular (equipamento_id = 1)
INSERT INTO checklists (equipamento_id, instrucao, ordem) VALUES
(1, 'Faça backup de fotos, contatos, conversas e arquivos importantes na nuvem ou computador.', 1),
(1, 'Remova o chip da operadora (SIM card) e o cartão de memória (MicroSD).', 2),
(1, 'Desvincule sua conta Google ou Apple ID (remova bloqueios de ativação).', 3),
(1, 'Restaure o aparelho para as configurações originais de fábrica.', 4),
(1, 'Se a bateria estiver inchada, coloque o aparelho em um saco plástico resistente e não tente dobrá-lo.', 5);

-- Passos para Notebook / Laptop (equipamento_id = 2)
INSERT INTO checklists (equipamento_id, instrucao, ordem) VALUES
(2, 'Salve seus documentos pessoais em um HD externo ou armazenamento em nuvem.', 1),
(2, 'Saia de todas as contas logadas nos navegadores e aplicativos.', 2),
(2, 'Formate o disco rígido (HD/SSD) ou execute a restauração completa do sistema operacional.', 3),
(2, 'Guarde a fonte de alimentação (carregador) junto com o notebook para facilitar a triagem.', 4);

-- Passos para Pilhas e Baterias (equipamento_id = 3)
INSERT INTO checklists (equipamento_id, instrucao, ordem) VALUES
(3, 'Isole os polos positivo (+) e negativo (-) com fita isolante ou fita adesiva para evitar curto-circuito.', 1),
(3, 'Armazene as pilhas e baterias em um recipiente plástico seco, longe de objetos metálicos (como chaves ou moedas).', 2),
(3, 'Nunca amasse, perfure, queime ou tente abrir o invólucro da bateria.', 3);

-- Passos para Monitor / Televisão (equipamento_id = 4)
INSERT INTO checklists (equipamento_id, instrucao, ordem) VALUES
(4, 'Desconecte todos os cabos de energia, HDMI e antenas antes de movimentar o aparelho.', 1),
(4, 'Enrole os cabos separados e prenda-os com elástico ou abraçadeira.', 2),
(4, 'Evite apoiar objetos pesados sobre a tela para não quebrar o painel de vidro ou cristal líquido.', 3);

-- Passos para Cabos, Carregadores e Fones (equipamento_id = 5)
INSERT INTO checklists (equipamento_id, instrucao, ordem) VALUES
(5, 'Verifique se não há fios desencapados expostos; se houver, passe fita isolante nas pontas.', 1),
(5, 'Enrole cada cabo individualmente para evitar emaranhados durante o transporte.', 2),
(5, 'Separe carregadores com fonte das extensões ou fones simples, se possível.', 3);