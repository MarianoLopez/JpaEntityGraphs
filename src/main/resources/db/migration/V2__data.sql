insert into provider (name, id) values ('Mariano', 1);
insert into address (created_date, last_modified_date, city, street, provider_id)
            values ('2019-04-23T09:34:22.340', '2019-04-23T09:34:22.340', 'Corrientes', '123', 1);
insert into product (created_date, last_modified_date, name, price, stock, provider_id, id)
            values ('2019-04-23T09:34:22.434', '2019-04-23T09:34:22.434', 'Apple', 35.5, 12, 1, 1);
            insert into product (created_date, last_modified_date, name, price, stock, provider_id, id)
            values ('2019-04-23T09:34:22.434', '2019-04-23T09:34:22.434', 'Orange', 40.0, 24, 1, 2);
insert into ticket (created_date, last_modified_date, total, id)
            values ('2019-04-23T09:34:22.465', '2019-04-23T09:34:22.465', 575 ,1);
insert into ticket_detail (ticket_id, product_id, quantity, total) values (1, 1, 5, 175);
update product set stock = 7 where id = 1;
insert into ticket_detail (ticket_id, product_id, quantity, total) values (1, 2, 10, 400);
update product set stock = 30 where id = 2;