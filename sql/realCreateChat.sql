-- auto-generated definition
create table uh_chat
(
    time         timestamp               not null,
    chatId       int auto_increment
        primary key,
    fromUsername varchar(255) default '' not null,
    toUsername   varchar(255) default '' not null,
    content      varchar(255) default '' not null
);

