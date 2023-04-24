create table uh_post_tags
(
    id      bigint                  not null
        primary key,
    tag     varchar(255) default '' not null,
    version int          default 1  not null
);

