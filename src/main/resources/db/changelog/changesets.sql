--liquibase formatted sql

--changeset Alex:create-product
create table product(
    id bigserial,
    name varchar(255) not null,
    ingridients varchar(255) not null,
    recipe varchar(255) not null
)