create type role as enum (
    'admin',
    'guest'
    );

create table "user"
(
    id                serial primary key,
    first_name        varchar not null,
    last_name         varchar not null,
    email             varchar not null
        constraint user_uk02
            unique,
    password          varchar not null,
    role              role      default 'guest',
    active            boolean   default true,
    create_date       timestamp default timezone('utc'::text, now()),
    modification_date timestamp
);

insert into "user" (first_name, last_name, email, password, role, active)
values ('first name 1', 'last name 1', 'firstname1@gmail.com', 'sd', 'guest', true),
       ('first name 2', 'last name 2', 'firstname2@gmail.com', 'sd', 'admin', true);