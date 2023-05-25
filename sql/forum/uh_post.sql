create table uh_post
(
    postId         bigint auto_increment
        primary key,
    userId         bigint       default 0                     not null,
    username       varchar(255) default ''                    not null,
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

INSERT INTO universityhelper.uh_post (postId, userId, username, title, tags, releaseTime, lastModifyTime, content, likeNum, starNum, commentNum, viewNum, version) VALUES (1, 11, '', '1', '[]', '1970-01-02 00:00:00', '1970-01-02 00:00:00', '12', 0, 0, 0, 0, 1);
INSERT INTO universityhelper.uh_post (postId, userId, username, title, tags, releaseTime, lastModifyTime, content, likeNum, starNum, commentNum, viewNum, version) VALUES (2, 11, '', 'title', '["这是第一条帖子", "hello world"]', '2023-05-25 23:00:11', '2023-05-25 23:00:11', 'content', 0, 0, 0, 0, 1);
INSERT INTO universityhelper.uh_post (postId, userId, username, title, tags, releaseTime, lastModifyTime, content, likeNum, starNum, commentNum, viewNum, version) VALUES (3, 11, '', 'title', '["这是第一条帖子", "hello world"]', '2023-05-25 23:02:57', '2023-05-25 23:02:57', 'content', 0, 0, 0, 0, 1);
INSERT INTO universityhelper.uh_post (postId, userId, username, title, tags, releaseTime, lastModifyTime, content, likeNum, starNum, commentNum, viewNum, version) VALUES (4, 11, '2027405037', 'title', '["这是第一条帖子", "hello world"]', '2023-05-26 00:06:00', '2023-05-26 00:06:00', 'content', 0, 0, 0, 0, 1);
INSERT INTO universityhelper.uh_post (postId, userId, username, title, tags, releaseTime, lastModifyTime, content, likeNum, starNum, commentNum, viewNum, version) VALUES (5, 11, '2027405037', 'title', '["这是第一条帖子", "hello world"]', '2023-05-26 00:06:27', '2023-05-26 00:06:27', 'content', 0, 0, 0, 0, 1);
