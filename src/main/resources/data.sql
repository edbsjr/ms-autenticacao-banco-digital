-- Adiciona um usuário com o perfil de CLIENTE
INSERT INTO users (
    login, password_hash,
    role, status,
    created_at, last_access_date)
VALUES (
    'cliente01', '$2a$10$B0E0hXpA1.mHn.nFpGq1kO/t3mN1.s3jR4D5d.tBvC4q5p6t7u8v9w',
    'CLIENTE', 'ATIVO',
    NOW(), NOW());

-- Adiciona um usuário com o perfil de GERENTE
INSERT INTO users (
    login, password_hash,
    role, status,
    created_at, last_access_date)
VALUES (
    'gerente01', '$2a$10$J3vL1kH8l.xM2.oD5fG2kL/t5gN7f.s1jR2d.eC3c4d5e6f7g8h9',
    'GERENTE', 'ATIVO',
    NOW(), NOW());

-- Adiciona um usuário com o perfil de ADMIN
INSERT INTO users (
    login, password_hash,
    role, status,
    created_at, last_access_date)
VALUES (
    'admin01', '$2a$10$O6pK2mS4r.xY5.pG8lT2fV/z7hQ9r.t3jN4a.bC5c6d7e8f9g0h1',
    'ADMIN', 'ATIVO',
    NOW(), NOW());

