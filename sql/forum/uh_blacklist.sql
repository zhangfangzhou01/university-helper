create table uh_blacklist
(
    id        bigint auto_increment
        primary key,
    blockerId bigint default 0 not null comment '拉黑别人的人的userId',
    blockedId bigint default 0 not null comment '被拉黑的人的userId',
    version   int    default 1 not null
);

