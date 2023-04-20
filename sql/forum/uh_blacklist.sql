create table uh_blacklist
(
    id      bigint           not null
        primary key,
    blocker bigint default 0 not null comment '拉黑别人的人的userId',
    blocked bigint default 0 not null comment '被拉黑的人的userId'
) engine = InnoDB
  default charset = utf8mb4;

