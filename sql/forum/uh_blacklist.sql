create table uh_blacklist
(
    id      bigint not null
        primary key,
    blocker bigint not null comment '拉黑别人的人的userId',
    bolcked bigint not null comment '被拉黑的人的userId'
);

