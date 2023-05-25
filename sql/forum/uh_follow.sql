create table uh_follow
(
    id         bigint           not null
        primary key,
    followerId bigint default 0 not null comment '关注者 Id(粉丝)',
    followedId bigint default 0 not null comment '被关注者',
    version    int    default 1 not null,
    constraint uh_follow_pk
        unique (followerId),
    constraint uh_follow_pk2
        unique (followedId)
)
    comment 'follower 关注 followed';

