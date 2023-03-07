use universityhelper;

create table uh_usertaketask
(
    taskId int not null,
    userId int not null
)
    comment '任务与其接受者的表';

