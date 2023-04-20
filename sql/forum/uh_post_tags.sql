create table uh_post_tags
(
    id  bigint                  not null
        primary key,
    tag varchar(255) default '' not null
) engine = InnoDB
  default charset = utf8mb4;

