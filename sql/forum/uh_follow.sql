create table uh_follow
(
    id       bigint not null
        primary key,
    follower bigint not null comment '关注者 Id(粉丝)',
    followed bigint not null comment '被关注者'
) comment 'follower 关注 followed';

