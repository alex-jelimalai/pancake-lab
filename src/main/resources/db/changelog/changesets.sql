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
    product_id bigint           not null references product (id) on delete cascade,
    order_id   bigint           not null references orders (id) on delete cascade,
    quantity   int              not null check (quantity > 0),
    price      double precision not null
);

--changeset Alex:product.data
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
        8.79),

       ('Milk Chocolate Pancake',
        'Flour, Cocoa Powder, Eggs, Milk, Sugar, Baking Powder, Butter, Milk Chocolate',
        'Mix batter with melted milk chocolate, cook on a non-stick skillet.',
        7.99),

       ('Classic Buttermilk Pancakes',
        'Flour, Buttermilk, Eggs, Baking Powder, Sugar, Salt, Butter',
        'Mix dry ingredients, add wet ingredients, cook on a skillet until golden brown.',
        5.99),

       ('Chocolate Chip Pancakes',
        'Flour, Buttermilk, Eggs, Baking Powder, Sugar, Salt, Butter, Chocolate Chips',
        'Prepare the batter, fold in chocolate chips, cook until golden brown.',
        6.49),

       ('Blueberry Pancakes',
        'Flour, Buttermilk, Eggs, Baking Powder, Sugar, Salt, Butter, Blueberries',
        'Mix batter, gently fold in blueberries, cook on a skillet.',
        6.99),

       ('Banana Pancakes',
        'Flour, Bananas, Eggs, Baking Powder, Milk, Sugar, Butter, Cinnamon',
        'Mash bananas, mix with other ingredients, cook until fluffy.',
        6.79),

       ('Strawberry Pancakes',
        'Flour, Eggs, Milk, Sugar, Baking Powder, Salt, Butter, Fresh Strawberries',
        'Mix batter, add chopped strawberries, cook on a skillet.',
        7.29),

       ('Oatmeal Pancakes',
        'Oats, Flour, Eggs, Milk, Baking Powder, Cinnamon, Sugar, Butter',
        'Blend oats into flour, mix with other ingredients, cook on a skillet.',
        7.49),

       ('Vegan Pancakes',
        'Flour, Almond Milk, Baking Powder, Maple Syrup, Coconut Oil, Vanilla Extract',
        'Mix ingredients, let rest for 5 minutes, cook until golden brown.',
        7.99),

       ('Protein Pancakes',
        'Oats, Protein Powder, Egg Whites, Milk, Baking Powder, Honey',
        'Blend ingredients, cook on a non-stick skillet.',
        8.49),

       ('Pumpkin Spice Pancakes',
        'Flour, Pumpkin Puree, Eggs, Milk, Baking Powder, Cinnamon, Nutmeg, Sugar',
        'Mix ingredients, cook on medium heat until golden.',
        7.99),

       ('Matcha Green Tea Pancakes',
        'Flour, Eggs, Milk, Sugar, Baking Powder, Matcha Powder, Vanilla Extract',
        'Mix ingredients, cook on a skillet, serve with honey.',
        8.99);

