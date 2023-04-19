create table uh_post
(
    postId         bigint       not null
        primary key,
    userId         bigint       not null,
    title          varchar(255) not null,
    tags           varchar(255) not null,
    releaseTime    timestamp    not null comment '帖子发布时间',
    lastModifyTime timestamp    not null,
    content        text         not null,
    likeNum        bigint       not null,
    dislikeNum     bigint       not null,
    starNum        bigint       not null,
    commentNum     bigint       not null
) comment '论坛表，存储论坛信息';

