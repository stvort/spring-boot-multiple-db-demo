-- date: 2022-11-27
-- author: stvort


create table users (
    id bigserial,
    name varchar(255),
    login varchar(50),
    primary key (id)
);

create table notes (
    id bigserial,
    user_id bigint not null references users(id),
    title varchar(255),
    text varchar(2000),
    primary key (id)
);