--liquibase formatted sql
--changeset marcosav:create-customers-table

CREATE TABLE IF NOT EXISTS customers
(
    id                UUID PRIMARY KEY,
    name              VARCHAR(50)              NOT NULL,
    surname           VARCHAR(50)              NOT NULL,
    profile_image_url VARCHAR(255),
    active            BOOLEAN                  NOT NULL,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by        VARCHAR(100)             NOT NULL,
    updated_at        TIMESTAMP WITH TIME ZONE,
    updated_by        VARCHAR(100)
);

--rollback DROP TABLE IF EXISTS customers;