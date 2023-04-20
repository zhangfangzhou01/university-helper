create table uh_comment
(
    commentId       bigint                                     not null
        primary key,
    userId          bigint       default 0                     not null,
    postId          bigint       default 0                     not null,
    releaseTime     timestamp    default '1970-01-02 00:00:00' not null,
    content         varchar(510) default ''                    not null,
    likeNum         bigint       default 0                     not null,
    replayCommentId bigint       default 0                     not null
) engine = InnoDB
  default charset = utf8;

