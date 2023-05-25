create table uh_comment
(
    commentId      bigint auto_increment
        primary key,
    userId         bigint       default 0                     not null,
    postId         bigint       default 0                     not null,
    releaseTime    timestamp    default '1970-01-02 00:00:00' not null,
    content        varchar(510) default ''                    not null,
    likeNum        bigint       default 0                     not null,
    replyCommentId bigint       default 0                     not null,
    version        int          default 1                     not null
);

INSERT INTO universityhelper.uh_comment (commentId, userId, postId, releaseTime, content, likeNum, replyCommentId, version) VALUES (1, 11, 1, '1970-01-02 00:00:00', '123', 0, 0, 1);
INSERT INTO universityhelper.uh_comment (commentId, userId, postId, releaseTime, content, likeNum, replyCommentId, version) VALUES (2, 11, 1, '1970-01-02 00:00:00', '1234', 0, 1, 1);
INSERT INTO universityhelper.uh_comment (commentId, userId, postId, releaseTime, content, likeNum, replyCommentId, version) VALUES (3, 11, 1, '1970-01-02 00:00:00', '12345', 0, 1, 1);
