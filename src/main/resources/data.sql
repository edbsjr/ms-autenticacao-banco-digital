-- Adiciona um usuário com o perfil de CLIENTE senha pura"senhaCliente123"
INSERT INTO users (
    login, password_hash,
    role, status,
    created_at, last_access_date)
VALUES (
    'cliente01', '$2a$10$t2kXES4xJo6OMSSW9DpO0uUcSgnmI5bR/JaJNcxV42VK4H2T4qzNC',
    'CLIENTE', 'ATIVO',
    NOW(), NOW());

-- Adiciona um usuário com o perfil de GERENTE senha pura "senhaGerente123"
INSERT INTO users (
    login, password_hash,
    role, status,
    created_at, last_access_date)
VALUES (
    'gerente01', '$2a$10$YfCgPDMZquZt.4QYXKrsVOiv5qsKvZyVT1aJK2CzVnMrOJ4LOAvUa',
    'GERENTE', 'ATIVO',
    NOW(), NOW());

-- Adiciona um usuário com o perfil de ADMIN senha pura "senhaAdmin123"
INSERT INTO users (
    login, password_hash,
    role, status,
    created_at, last_access_date)
VALUES (
    'admin01', '$2a$10$ZCNsb.TLH48/9fzlnrr7E.h/LIOVsL8R7gj/Y0wVgQJ5TKSA2pqJW',
    'ADMIN', 'ATIVO',
    NOW(), NOW());

