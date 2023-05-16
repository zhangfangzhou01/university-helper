create table uh_usertaketask
(
    id      bigint auto_increment
        primary key,
    userId  bigint default 0 not null,
    taskId  bigint default 0 not null,
    version int    default 1 not null
)
    comment '任务与其接受者的表';

INSERT INTO universityhelper.uh_usertaketask (id, userId, taskId, version) VALUES (8, 11, 17, 1);
INSERT INTO universityhelper.uh_usertaketask (id, userId, taskId, version) VALUES (9, 11, 19, 1);
INSERT INTO universityhelper.uh_usertaketask (id, userId, taskId, version) VALUES (10, 10, 18, 1);
INSERT INTO universityhelper.uh_usertaketask (id, userId, taskId, version) VALUES (11, 11, 77, 1);
INSERT INTO universityhelper.uh_usertaketask (id, userId, taskId, version) VALUES (12, 11, 81, 1);
