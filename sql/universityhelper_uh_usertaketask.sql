drop table if exists universityhelper.uh_usertaketask;
create table uh_usertaketask
(
    id     bigint auto_increment
        primary key,
    userId bigint default 0 not null,
    taskId bigint default 0 not null
)
    comment '任务与其接受者的表';

