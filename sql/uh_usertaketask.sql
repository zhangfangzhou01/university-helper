create table uh_usertaketask
(
    id     bigint auto_increment
        primary key,
    userId bigint default 0 not null,
    taskId bigint default 0 not null
)
    comment '任务与其接受者的表';

INSERT INTO universityhelper.uh_usertaketask (id, userId, taskId) VALUES (8, 11, 17);
INSERT INTO universityhelper.uh_usertaketask (id, userId, taskId) VALUES (9, 11, 19);
INSERT INTO universityhelper.uh_usertaketask (id, userId, taskId) VALUES (10, 10, 18);
