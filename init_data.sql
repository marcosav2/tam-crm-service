INSERT INTO users
(id,
 username,
 password,
 name,
 surname,
 active,
 role)
VALUES ('00000000-0000-0000-0000-000000000000',
        'admin',
        '$2y$10$q9fOMGufM34Aqq3rwgbiNe8nxlBtct6zeXp/ORWeBfelttLH96RNm',
        'Admin',
        'istrator',
        true,
        'ADMIN')
ON CONFLICT DO NOTHING;