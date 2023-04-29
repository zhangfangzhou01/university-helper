create table uh_chat
(
    chatId       bigint auto_increment
        primary key,
    fromUsername varchar(255) default ''                    not null,
    toUsername   varchar(255) default ''                    not null,
    content      text                                       not null,
    time         timestamp    default '1970-01-02 00:00:00' not null,
    `read`       tinyint(1)   default 0                     not null,
    version      int          default 1                     not null
);

INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (1, '2027405037', 'all', 'Type here...', '2023-03-23 22:57:48', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (2, '2027405037', 'all', 'Type here...', '2023-03-23 22:59:41', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (3, '2027405037', 'all', '', '2023-03-23 22:59:51', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (4, '2027405037', 'all', 'Type here...', '2023-03-23 23:03:08', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (5, '2027405037', 'all', 'Type here...', '2023-03-23 23:03:32', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (6, '2027405037', '2027405037', 'ghfg', '2023-03-23 23:04:25', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (7, '2027405037', '2027405036', '你是谁', '2023-03-23 23:04:50', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (8, '2027405036', '2027405037', '我是2027405036', '2023-03-23 23:07:01', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (9, '2027405037', '2027405036', '我是2027405037', '2023-03-23 23:07:04', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (10, '2027405037', 'all', 'Type here...', '2023-03-23 23:22:44', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (11, '2027405037', 'all', 'Type here...', '2023-03-23 23:28:19', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (12, '2027405037', 'all', 'Type here...', '2023-03-23 23:32:33', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (13, '2027405037', 'all', '', '2023-03-23 23:32:34', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (14, '2027405037', 'all', '', '2023-03-23 23:32:35', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (15, '2027405037', 'all', 'Type here...', '2023-03-23 23:39:05', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (16, '2027405037', 'all', '', '2023-03-23 23:39:13', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (17, '2027405037', 'all', 'Type here...', '2023-03-23 23:40:18', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (18, '2027405037', 'all', '', '2023-03-23 23:40:26', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (19, '2027405037', 'all', 'Type here...', '2023-03-24 08:07:25', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (20, '2027405037', 'all', 'Type here...', '2023-03-24 08:07:28', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (21, '2027405037', 'all', '', '2023-03-24 08:07:28', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (22, '2027405037', 'all', '', '2023-03-24 08:07:31', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (23, '2027405037', 'all', '', '2023-03-24 08:07:56', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (24, '2027405036', '2027405037', '你好呀，我是2027405036', '2023-03-24 11:40:16', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (25, '2027405037', '2027405036', '你也好呀，我是2027405037', '2023-03-24 11:40:21', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (26, '2027405036', '2027405037', '你好', '2023-03-24 17:05:26', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (27, '2027405036', '2027405037', '你好', '2023-03-24 17:05:54', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (28, '2027405037', '2027405036', '你好', '2023-03-24 17:06:05', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (29, '2027405037', '2027405036', 'Type here...', '2023-03-24 19:00:28', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (30, '2027405036', '2027405037', 'Type here...', '2023-03-24 19:00:36', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (31, '2027405036', '2027405037', 'Type here...', '2023-03-24 19:34:39', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (32, '2027405037', 'all', '', '2023-03-25 00:18:57', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (33, '2027405037', 'all', '', '2023-03-25 00:18:58', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (34, '2027405037', '2027405036', 'Type here...', '2023-03-26 02:05:43', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (35, '2027405037', 'all', 'Type here...', '2023-03-26 13:56:28', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (36, '2027405037', '2027405036', 'Type here...', '2023-03-27 14:34:20', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (37, '2027405036', 'all', 'Type here...', '2023-03-27 14:34:29', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (38, '2027405037', '2027405036', 'Type here...', '2023-03-29 09:42:11', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (39, '2027405037', '2027405036', 'Type here...', '2023-03-29 09:43:40', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (40, '2027405037', '2027405036', '你好', '2023-03-29 09:47:46', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (41, '2027405037', '2027405036', '', '2023-03-29 09:49:35', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (42, '2027405037', '2027405036', 'asdasdasd', '2023-03-29 09:49:50', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (43, '2027405037', '2027405036', 'Type here...', '2023-03-29 09:50:21', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (44, '2027405037', '2027405036', 'Type here...', '2023-03-29 09:51:48', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (45, '2027405037', '2027405036', 'Type here...', '2023-03-29 09:52:06', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (46, '2027405037', '2027405036', 'Type here...', '2023-03-29 10:03:04', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (47, '2027405037', '2027405036', '你好', '2023-03-29 10:03:16', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (48, '2027405037', '2027405037', '你好', '2023-03-29 10:03:16', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (49, '2027405037', '2027405036', '', '2023-03-29 10:04:40', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (50, '2027405036', '2027405037', 'Type here...', '2023-03-29 10:06:10', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (51, '2027405037', 'all', 'Type here...', '2023-03-29 10:10:29', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (52, '2027405037', '2027405036', '', '2023-03-29 10:10:47', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (53, '2027405037', '2027405037', '', '2023-03-29 10:10:47', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (54, '2027405037', 'all', 'Type here...', '2023-03-29 10:19:20', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (55, '2027405037', '2027405037', 'Type here...', '2023-03-29 10:19:25', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (56, '2027405037', '2027405036', 'Type here...', '2023-03-29 10:19:36', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (57, '2027405037', '2027405036', 'Type here...', '2023-03-29 10:20:53', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (58, '2027405036', '2027405037', 'Type here...', '2023-03-29 10:21:37', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (59, '2027405037', '2027405036', 'Type here...', '2023-03-29 10:26:08', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (60, '2027405037', '2027405037', 'Type here...', '2023-03-29 10:26:08', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (61, '2027405037', 'all', 'Type here...', '2023-03-29 10:26:16', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (62, '2027405037', 'all', 'Type here...', '2023-04-18 15:56:38', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (63, '2027405037', 'all', 'Type here...', '2023-04-18 16:10:45', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (64, '2027405037', 'all', 'Type here...', '2023-04-18 16:10:48', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (65, '2027405037', 'all', 'Type here...', '2023-04-18 16:10:56', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (66, '2027405037', 'all', 'Type here...', '2023-04-18 16:10:57', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (67, '2027405037', 'all', 'Type here...', '2023-04-18 16:10:57', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (68, '2027405037', 'all', 'Type here...', '2023-04-18 16:11:00', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (69, '2027405037', 'all', 'Type here...', '2023-04-18 16:13:42', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (70, '2027405037', 'all', 'Type here...', '2023-04-18 16:14:06', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (71, '2027405037', 'all', 'Type here...', '2023-04-18 19:00:59', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (72, '2027405037', 'all', 'Type here...', '2023-04-18 19:00:59', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (73, '2027405037', 'all', 'Type here...', '2023-04-18 19:01:00', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (74, '2027405037', 'all', 'Type here...', '2023-04-18 19:02:09', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (75, '2027405037', 'all', 'Type here...', '2023-04-18 19:21:23', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (76, '2027405037', 'all', 'Type here...', '2023-04-18 19:21:23', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (77, '2027405037', 'all', 'Type here...', '2023-04-18 19:21:23', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (78, '2027405037', 'all', 'Type here...', '2023-04-18 19:21:24', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (79, '2027405037', 'all', 'Type here...', '2023-04-18 19:21:24', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (80, '2027405037', 'all', 'Type here...', '2023-04-18 19:21:28', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (81, '2027405037', 'all', 'Type here...', '2023-04-18 19:21:28', 0, 1);
INSERT INTO universityhelper.uh_chat (chatId, fromUsername, toUsername, content, time, isRead, version) VALUES (82, '2027405037', 'all', 'Type here...', '2023-04-18 19:21:28', 0, 1);
