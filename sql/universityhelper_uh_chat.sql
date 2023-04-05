create table uh_chat
(
    chatId       bigint auto_increment
        primary key,
    fromUsername varchar(255) default ''                    not null,
    toUsername   varchar(255) default ''                    not null,
    content      varchar(255) default ''                    not null,
    time         timestamp    default '1970-01-02 00:00:00' not null
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
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (35, '2027405037', 'all', 'Type here...', '2023-03-26 13:56:28');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (36, '2027405037', '2027405036', 'Type here...', '2023-03-27 14:34:20');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (37, '2027405036', 'all', 'Type here...', '2023-03-27 14:34:29');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (38, '2027405037', '2027405036', 'Type here...', '2023-03-29 09:42:11');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (39, '2027405037', '2027405036', 'Type here...', '2023-03-29 09:43:40');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (40, '2027405037', '2027405036', '你好', '2023-03-29 09:47:46');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (41, '2027405037', '2027405036', '', '2023-03-29 09:49:35');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (42, '2027405037', '2027405036', 'asdasdasd', '2023-03-29 09:49:50');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (43, '2027405037', '2027405036', 'Type here...', '2023-03-29 09:50:21');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (44, '2027405037', '2027405036', 'Type here...', '2023-03-29 09:51:48');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (45, '2027405037', '2027405036', 'Type here...', '2023-03-29 09:52:06');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (46, '2027405037', '2027405036', 'Type here...', '2023-03-29 10:03:04');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (47, '2027405037', '2027405036', '你好', '2023-03-29 10:03:16');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (48, '2027405037', '2027405037', '你好', '2023-03-29 10:03:16');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (49, '2027405037', '2027405036', '', '2023-03-29 10:04:40');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (50, '2027405036', '2027405037', 'Type here...', '2023-03-29 10:06:10');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (51, '2027405037', 'all', 'Type here...', '2023-03-29 10:10:29');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (52, '2027405037', '2027405036', '', '2023-03-29 10:10:47');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (53, '2027405037', '2027405037', '', '2023-03-29 10:10:47');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (54, '2027405037', 'all', 'Type here...', '2023-03-29 10:19:20');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (55, '2027405037', '2027405037', 'Type here...', '2023-03-29 10:19:25');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (56, '2027405037', '2027405036', 'Type here...', '2023-03-29 10:19:36');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (57, '2027405037', '2027405036', 'Type here...', '2023-03-29 10:20:53');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (58, '2027405036', '2027405037', 'Type here...', '2023-03-29 10:21:37');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (59, '2027405037', '2027405036', 'Type here...', '2023-03-29 10:26:08');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (60, '2027405037', '2027405037', 'Type here...', '2023-03-29 10:26:08');
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time)
VALUES (61, '2027405037', 'all', 'Type here...', '2023-03-29 10:26:16');