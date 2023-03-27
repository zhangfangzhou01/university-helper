create table uh_chat
(
    chatId       bigint auto_increment
        primary key,
    fromUsername varchar(255) default '' not null,
    toUsername   varchar(255) default '' not null,
    content      varchar(255) default '' not null,
    time         timestamp               not null
);

INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (1, '2027405037', 'all', 'Type here...', '2023-03-23 22:57:48');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (2, '2027405037', 'all', 'Type here...', '2023-03-23 22:59:41');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (3, '2027405037', 'all', '', '2023-03-23 22:59:51');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (4, '2027405037', 'all', 'Type here...', '2023-03-23 23:03:08');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (5, '2027405037', 'all', 'Type here...', '2023-03-23 23:03:32');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (6, '2027405037', '2027405037', 'ghfg', '2023-03-23 23:04:25');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (7, '2027405037', '2027405036', '你是谁', '2023-03-23 23:04:50');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (8, '2027405036', '2027405037', '我是2027405036', '2023-03-23 23:07:01');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (9, '2027405037', '2027405036', '我是2027405037', '2023-03-23 23:07:04');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (10, '2027405037', 'all', 'Type here...', '2023-03-23 23:22:44');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (11, '2027405037', 'all', 'Type here...', '2023-03-23 23:28:19');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (12, '2027405037', 'all', 'Type here...', '2023-03-23 23:32:33');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (13, '2027405037', 'all', '', '2023-03-23 23:32:34');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (14, '2027405037', 'all', '', '2023-03-23 23:32:35');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (15, '2027405037', 'all', 'Type here...', '2023-03-23 23:39:05');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (16, '2027405037', 'all', '', '2023-03-23 23:39:13');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (17, '2027405037', 'all', 'Type here...', '2023-03-23 23:40:18');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (18, '2027405037', 'all', '', '2023-03-23 23:40:26');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (19, '2027405037', 'all', 'Type here...', '2023-03-24 08:07:25');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (20, '2027405037', 'all', 'Type here...', '2023-03-24 08:07:28');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (21, '2027405037', 'all', '', '2023-03-24 08:07:28');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (22, '2027405037', 'all', '', '2023-03-24 08:07:31');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (23, '2027405037', 'all', '', '2023-03-24 08:07:56');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (24, '2027405036', '2027405037', '你好呀，我是2027405036', '2023-03-24 11:40:16');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (25, '2027405037', '2027405036', '你也好呀，我是2027405037', '2023-03-24 11:40:21');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (26, '2027405036', '2027405037', '你好', '2023-03-24 17:05:26');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (27, '2027405036', '2027405037', '你好', '2023-03-24 17:05:54');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (28, '2027405037', '2027405036', '你好', '2023-03-24 17:06:05');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (29, '2027405037', '2027405036', 'Type here...', '2023-03-24 19:00:28');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (30, '2027405036', '2027405037', 'Type here...', '2023-03-24 19:00:36');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (31, '2027405036', '2027405037', 'Type here...', '2023-03-24 19:34:39');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (32, '2027405037', 'all', '', '2023-03-25 00:18:57');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (33, '2027405037', 'all', '', '2023-03-25 00:18:58');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (34, '2027405037', '2027405036', 'Type here...', '2023-03-26 02:05:43');