create table uh_comment
(
    commentId       bigint       not null
        primary key,
    userId          bigint       not null,
    postId          bigint       not null,
    releaseTime     bigint       not null,
    content         varchar(510) not null,
    likeNum         bigint       not null,
    dislikeNum      bigint       not null,
    replayCommentId bigint       not null
);

