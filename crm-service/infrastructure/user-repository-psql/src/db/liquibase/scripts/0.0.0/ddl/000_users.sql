--liquibase formatted sql
--changeset marcosav:create-users-table

CREATE TABLE IF NOT EXISTS users
(
    id       UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    name     VARCHAR(50)  NOT NULL,
    surname  VARCHAR(50)  NOT NULL,
    active   BOOLEAN      NOT NULL
);

--rollback DROP TABLE IF EXISTS users;