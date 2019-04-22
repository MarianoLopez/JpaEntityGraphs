create table address (provider_id integer not null, city varchar(255) not null, street varchar(255) not null,created_date timestamp not null, last_modified_date timestamp not null, primary key (provider_id));
create table provider (id integer IDENTITY, name varchar(255), primary key (id));
alter table product add COLUMN provider_id integer;
alter table address add constraint FK_address_provider foreign key (provider_id) references provider;
alter table product add constraint FK_product_provider foreign key (provider_id) references provider;
