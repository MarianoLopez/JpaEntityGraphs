create sequence hibernate_sequence start with 1 increment by 1;
create table product (id integer not null, created_date timestamp not null, last_modified_date timestamp not null, name varchar(255) not null, price double not null check (price>=0), stock integer not null check (stock>=0), primary key (id));
create table ticket (id integer not null, created_date timestamp not null, last_modified_date timestamp not null, total double not null check (total>=0), primary key (id));
create table ticket_detail (product_id integer not null, ticket_id integer not null, quantity integer not null check (quantity>=1), primary key (product_id, ticket_id));
alter table product add constraint UK_jmivyxk9rmgysrmsqw15lqr5b unique (name);
alter table ticket_detail add constraint FKoq8l34egnnenu37sgfnbwb1r4 foreign key (product_id) references product;
alter table ticket_detail add constraint FKgi0g5nsmkdaoyc25unogm3rry foreign key (ticket_id) references ticket;