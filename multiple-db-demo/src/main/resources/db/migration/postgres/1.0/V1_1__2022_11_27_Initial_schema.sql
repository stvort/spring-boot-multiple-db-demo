-- date: 2022-11-27
-- author: stvort

create table notes (
    id bigserial,
    user_id bigint not null,
    title varchar(255),
    text varchar(2000),
    primary key (id)
);