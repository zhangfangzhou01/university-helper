create table uh_task_tags
(
    id      bigint auto_increment
        primary key,
    tag     varchar(255) default '' not null,
    version int          default 1  not null,
    constraint tag
        unique (tag),
    constraint uh_task_tags_tag_uindex
        unique (tag)
);

INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (1, '外卖', 1);
INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (2, '红色', 1);
INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (4, '黄色', 1);
INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (5, '紫色', 1);
INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (6, '交易', 1);
INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (7, '市场', 1);
INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (8, 'Java', 1);
INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (9, '你好', 1);
INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (10, 'a', 1);
INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (11, 'b', 1);
INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (12, 'c', 1);
INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (13, '原神', 1);
INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (14, '开放世界', 1);
INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (15, 'helloworld', 1);
INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (19, '哈哈a', 1);
INSERT INTO universityhelper.uh_task_tags (id, tag, version) VALUES (26, '辣椒', 1);
