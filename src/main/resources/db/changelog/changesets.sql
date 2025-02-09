--liquibase formatted sql

--changeset Alex:create.product.orders.2
drop table if exists product;
drop table if exists orders;
drop table if exists order_item;

create table product
(
    id INTEGER PRIMARY KEY,
    name        varchar(255)   not null,
    ingridients text           not null default '',
    recipe      text           not null default '',
    price       decimal(10, 2) not null
);


create table orders
(
    id INTEGER PRIMARY KEY,
    order_type varchar(50) unique not null check (order_type in ('DISCIPLE', 'DELIVERY', 'CHIEF')),
    status     varchar(50)        not null check (status in ('PENDING', 'PROCESSING', 'COMPLETED', 'CANCELED')),
    building   int,
    room_no    int,
    created_at timestamp default current_timestamp
);

create table order_item
(
    id INTEGER PRIMARY KEY,
    product_id bigint           not null references product (id) on delete cascade,
    order_id   bigint           not null references orders (id) on delete cascade,
    quantity   int              not null check (quantity > 0),
    price      double precision not null
);

--changeset Alex:product.data.2
delete from product;
INSERT INTO product (name, ingridients, recipe, price)
VALUES ('Dark Chocolate Pancakes',
        'Flour, Cocoa Powder, Eggs, Milk, Sugar, Baking Powder, Butter, Dark Chocolate',
        'Prepare the batter with melted dark chocolate, cook until fluffy.',
        7.99),

       ('Dark Chocolate Whipped Cream Hazelnuts Pancake',
        'Flour, Cocoa Powder, Eggs, Milk, Sugar, Baking Powder, Butter, Dark Chocolate, Whipped Cream, Hazelnuts',
        'Make batter with dark chocolate, top with whipped cream and crushed hazelnuts.',
        8.99),

       ('Dark Chocolate Whipped Cream Pancake',
        'Flour, Cocoa Powder, Eggs, Milk, Sugar, Baking Powder, Butter, Dark Chocolate, Whipped Cream',
        'Mix batter with melted dark chocolate, serve with whipped cream topping.',
        8.49),

       ('Milk Chocolate Hazelnuts Pancake',
        'Flour, Cocoa Powder, Eggs, Milk, Sugar, Baking Powder, Butter, Milk Chocolate, Hazelnuts',
        'Prepare batter, fold in milk chocolate and crushed hazelnuts, cook on a skillet.',
        8.79);

