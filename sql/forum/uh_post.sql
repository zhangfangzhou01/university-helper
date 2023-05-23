create table uh_post
(
    postId         bigint                                     not null
        primary key,
    userId         bigint       default 0                     not null,
    title          varchar(255) default ''                    not null,
    tags           json         default (json_array())        not null,
    releaseTime    timestamp    default '1970-01-02 00:00:00' not null comment '帖子发布时间',
    lastModifyTime timestamp    default '1970-01-02 00:00:00' not null,
    content        text                                       not null,
    likeNum        bigint       default 0                     not null,
    starNum        bigint       default 0                     not null,
    commentNum     bigint       default 0                     not null,
    viewNum        bigint       default 0                     not null,
    version        int          default 1                     not null
)
    comment '论坛表，存储论坛信息' row_format = DYNAMIC;

