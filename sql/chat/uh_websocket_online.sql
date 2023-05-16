create table uh_websocket_online
(
    id         bigint auto_increment
        primary key,
    username   varchar(255) default ''                    not null,
    isOnline   tinyint(1)   default 0                     not null,
    onlineTime timestamp    default '1970-01-02 00:00:00' not null,
    constraint uh_websocket_online_username_uindex
        unique (username),
    constraint username
        unique (username)
);

INSERT INTO universityhelper.uh_websocket_online (id, username, isOnline, onlineTime) VALUES (1, '2027405037', 1, '2023-05-05 11:57:56');
