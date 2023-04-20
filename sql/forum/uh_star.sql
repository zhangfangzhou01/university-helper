create table uh_star
(
    starId bigint           not null
        primary key,
    userId bigint default 0 not null,
    postId bigint default 0 not null
) engine = InnoDB
  default charset = utf8mb4;

