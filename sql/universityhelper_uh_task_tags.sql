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
INSERT INTO universityhelper.uh_task_tags (id, tag) VALUES (6, '交易');
INSERT INTO universityhelper.uh_task_tags (id, tag) VALUES (7, '市场');
INSERT INTO universityhelper.uh_task_tags (id, tag) VALUES (8, 'Java');
INSERT INTO universityhelper.uh_task_tags (id, tag) VALUES (9, '你好');
INSERT INTO universityhelper.uh_task_tags (id, tag) VALUES (10, 'a');
INSERT INTO universityhelper.uh_task_tags (id, tag) VALUES (11, 'b');
INSERT INTO universityhelper.uh_task_tags (id, tag) VALUES (12, 'c');