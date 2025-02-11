--liquibase formatted sql

--changeset Alex:create.product.orders.14
drop table if exists product;
drop table if exists orders;
drop table if exists order_item;

create table product
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        varchar(255)   not null,
    ingridients text           not null default '',
    details     text           not null default '',
    price       decimal(10, 2) not null
);


create table orders
(
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    order_type varchar(50) not null check (order_type in ('DISCIPLE', 'DELIVERY', 'CHIEF')),
    status     varchar(50) not null check (status in ('NEW', 'PROCESSING', 'COMPLETED', 'DELIVERED', 'READY_FOR_PROCESSING','CANCELED')),
    building   int,
    room_no    int
);

create table order_item
(
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    product_id bigint           not null references product (id) on delete cascade,
    order_id   bigint           not null references orders (id) on delete cascade,
    quantity   int              not null check (quantity > 0),
    price      double precision not null
);

--changeset Alex:product.data.14
delete
from product;
INSERT INTO product (name, ingridients, details, price)
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

--changeset Alex:orders.mock.data.14
delete
from order_item;
delete
from orders;

INSERT INTO orders (id, order_type, status, building, room_no)
VALUES (1, 'DISCIPLE', 'NEW', 3, 205),
       (2, 'DELIVERY', 'READY_FOR_PROCESSING', NULL, NULL),
       (3, 'CHIEF', 'COMPLETED', NULL, NULL),
       (4, 'DISCIPLE', 'CANCELED', 2, 101),
       (5, 'DELIVERY', 'NEW', NULL, NULL),
       (6, 'CHIEF', 'READY_FOR_PROCESSING', NULL, NULL),
       (7, 'DISCIPLE', 'COMPLETED', 4, 308),
       (8, 'DELIVERY', 'READY_FOR_PROCESSING', NULL, NULL),
       (9, 'CHIEF', 'PROCESSING', NULL, NULL),
       (10, 'DISCIPLE', 'NEW', 1, 112);

INSERT INTO order_item (id, product_id, order_id, quantity, price)
VALUES (1, 1, 1, 2, 15.98),  -- Order 1 has 2 Dark Chocolate Pancakes
       (2, 3, 1, 1, 8.49),   -- Order 1 has 1 Dark Chocolate Whipped Cream Pancake

       (3, 3, 2, 1, 7.99),   -- Order 2 has 1 Milk Chocolate Pancake
       (4, 2, 2, 1, 8.99),   -- Order 2 has 1 Dark Chocolate Whipped Cream Hazelnuts Pancake

       (5, 4, 3, 2, 12.98),  -- Order 3 has 2 Chocolate Chip Pancakes

       (6, 1, 4, 1, 6.79),   -- Order 4 has 1 Banana Pancake

       (7, 3, 5, 1, 7.29),   -- Order 5 has 1 Strawberry Pancake
       (8, 2, 5, 1, 5.99),   -- Order 5 has 1 Classic Buttermilk Pancake

       (9, 1, 6, 2, 15.98),  -- Order 6 has 2 Pumpkin Spice Pancakes

       (10, 2, 7, 1, 7.99),  -- Order 7 has 1 Vegan Pancake

       (11, 2, 8, 1, 8.99),  -- Order 8 has 1 Matcha Green Tea Pancake
       (12, 1, 8, 1, 8.49),  -- Order 8 has 1 Protein Pancake

       (13, 4, 9, 1, 8.79),  -- Order 9 has 1 Milk Chocolate Hazelnuts Pancake
       (14, 2, 9, 2, 13.98), -- Order 9 has 2 Blueberry Pancakes

       (15, 3, 10, 1, 7.49); -- Order 10 has 1 Oatmeal Pancake

