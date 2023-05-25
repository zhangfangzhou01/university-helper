create table uh_post
(
    postId         bigint auto_increment
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

INSERT INTO universityhelper.uh_post (postId, userId, title, tags, releaseTime, lastModifyTime, content, likeNum, starNum, commentNum, viewNum, version) VALUES (1, 11, '1', '[]', '1970-01-02 00:00:00', '1970-01-02 00:00:00', '12', 0, 0, 0, 0, 1);
