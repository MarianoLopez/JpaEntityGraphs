create table product (id integer IDENTITY, created_date timestamp not null, last_modified_date timestamp not null, name varchar(255) not null, price double not null, primary key (id));
create table ticket (id integer IDENTITY, created_date timestamp not null, last_modified_date timestamp not null, primary key (id));
create table ticket_detail (id integer not null, product_id integer not null);
create sequence hibernate_sequence start with 1 increment by 1;
alter table ticket_detail add constraint FK_ticket_detail_product foreign key (product_id) references product;
alter table ticket_detail add constraint FK_ticket_detail_ticket foreign key (id) references ticket;