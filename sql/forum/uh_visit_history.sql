create table uh_visit_history
(
    id        bigint                                  not null
        primary key,
    userId    bigint    default 0                     not null,
    postId    bigint    default 0                     not null,
    visitTime timestamp default '1970-01-02 00:00:00' not null,
    version   int       default 1                     not null
)
    comment '用户访问post的历史记录' charset = utf8;

