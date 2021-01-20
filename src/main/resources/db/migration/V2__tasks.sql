create table tasks(
id serial,
titulo varchar(20) not null,
descricao varchar(50) not null,
status varchar(50) not null,
priority varchar(50) not null,
deadline date,
users integer,
primary key (id),
foreign key(users) references users(id));
