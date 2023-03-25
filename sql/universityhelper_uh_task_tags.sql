drop table if exists universityhelper.uh_task_tags;
create table uh_task_tags
(
    id  bigint auto_increment
        primary key,
    tag varchar(255) default '' not null
);

INSERT INTO universityhelper.uh_task_tags (id, tag) VALUES (1, '外卖');
INSERT INTO universityhelper.uh_task_tags (id, tag) VALUES (2, '红色');
INSERT INTO universityhelper.uh_task_tags (id, tag) VALUES (4, '黄色');
INSERT INTO universityhelper.uh_task_tags (id, tag) VALUES (5, '紫色');