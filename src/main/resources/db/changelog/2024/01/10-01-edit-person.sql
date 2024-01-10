-- liquibase formatted sql

-- changeset Shulika619:1704888551809-1
ALTER TABLE person
    ADD role VARCHAR(10);
ALTER TABLE person
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;
ALTER TABLE person
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;


