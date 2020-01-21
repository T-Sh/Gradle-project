create extension pgcrypto;

alter extension pgcrypto set schema public;
	
	create table public.users (
id  bigserial not null, 
active boolean, 
coins int8, 
email varchar(255), 
nickname varchar(255), 
password varchar(255), 
username varchar(255), 
primary key (id));

insert into users(id, active, coins, email, nickname, password, username)
values (1, true, 0, null, 'admin', public.crypt(text('admin'), public.gen_salt('bf', 8)), 'admin');

create table user_role (
id int8 not null, 
roles varchar(255));

alter table if exists user_role 
add constraint FKm01t79r1jd43urtdfwr98po4q foreign key (id) references public.users;

insert into user_role(id, roles)
values (1, 'USER'), (1, 'ADMIN');

create table public.cost (
id  bigserial not null, 
cost int2, 
name varchar(255), 
primary key (id));

insert into cost
values (1, 10, 'Регистрация');

insert into cost
values (2, 3, 'Комментарий');

insert into cost
values (3, 10, 'Создать цель');

insert into cost
values (4, 30, 'Выполнить цель');

insert into cost
values (5, -25, 'Удалить цель');

insert into cost
values (6, 5, 'Передвинуть дату выполнения назад');

insert into cost
values (7, -5, 'Передвинуть дату выполнения вперед');

insert into cost
values (8, 1, 'Штраф за задержку');

insert into cost
values (9, 0, 'Оценка пользователя');