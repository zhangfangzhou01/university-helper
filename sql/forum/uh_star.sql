create table uh_star
(
    starId bigint not null
        primary key,
    userId bigint not null,
    postId bigint not null
);

