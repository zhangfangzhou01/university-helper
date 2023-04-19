create table uh_visit_history
(
    id        bigint    not null
        primary key,
    userId    bigint    not null,
    postId    bigint    not null,
    visitTime timestamp not null
) comment '用户访问post的历史记录';

