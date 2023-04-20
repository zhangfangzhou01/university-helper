create table uh_follow
(
    id       bigint           not null
        primary key,
    follower bigint default 0 not null comment '关注者 Id(粉丝)',
    followed bigint default 0 not null comment '被关注者',
    constraint uh_follow_pk
        unique (follower),
    constraint uh_follow_pk2
        unique (followed)
)
    comment 'follower 关注 followed'
    engine = InnoDB
    default charset = utf8;

