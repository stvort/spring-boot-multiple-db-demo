-- date: 2022-11-27
-- author: stvort

create table users (
    id bigint not null auto_increment,
    name varchar(255),
    login varchar(50),
    primary key (id)
);