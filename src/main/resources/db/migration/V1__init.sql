create table users(
id serial,
name varchar(50) not null,
password varchar,
email varchar(100) not null unique,
role varchar(50) not null,
primary key (id));