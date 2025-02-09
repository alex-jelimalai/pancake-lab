--liquibase formatted sql

--changeset Alex:create.product.orders.1
drop table if exists product;
drop table if exists orders;
drop table if exists order_item;

create table product
(
    id          serial primary key,
    name        varchar(255)   not null,
    ingridients text           not null default '',
    recipe      text           not null default '',
    price       decimal(10, 2) not null
);


create table orders
(
    id         serial primary key,
    order_type varchar(50) unique not null check (order_type in ('DISCIPLE', 'DELIVERY', 'CHIEF')),
    status     varchar(50)        not null check (status in ('PENDING', 'PROCESSING', 'COMPLETED', 'CANCELED')),
    building   int,
    room_no    int,
    created_at timestamp default current_timestamp
);

create table order_item
(
    id         serial primary key,
    product_id bigint             not null references product (id) on delete cascade,
    order_id bigint               not null references orders (id) on delete cascade,
    quantity   int                not null check (quantity > 0),
    price      double precision   not null
);
