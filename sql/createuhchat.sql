create table universityhelper.uh_chat
(
    chatId       int auto_increment
        primary key,
    fromUsername varchar(255) default '' not null,
    toUsername   varchar(255) default '' not null,
    content      varchar(255) default '' not null
);


