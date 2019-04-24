insert into provider (name, id) values ('Mariano', 1);
insert into address (created_date, last_modified_date, city, street, provider_id)
            values ('2019-04-23T09:34:22.340', '2019-04-23T09:34:22.340', 'Corrientes', '123', 1);
insert into product (created_date, last_modified_date, name, price, provider_id, id)
            values ('2019-04-23T09:34:22.434', '2019-04-23T09:34:22.434', 'Apple', 35.5, 1, 1);
            insert into product (created_date, last_modified_date, name, price, provider_id, id)
            values ('2019-04-23T09:34:22.434', '2019-04-23T09:34:22.434', 'Orange', 40.0, 1, 2);
insert into ticket (created_date, last_modified_date, id)
            values ('2019-04-23T09:34:22.465', '2019-04-23T09:34:22.465', 1);
insert into ticket_detail (id, product_id) values (1, 1);
insert into ticket_detail (id, product_id) values (1, 2);