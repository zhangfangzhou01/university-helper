create table uh_user
(
    userId             bigint auto_increment
        primary key,
    sex                char collate utf8mb4_general_ci default '男'                                 not null,
    username           varchar(255)                    default ''                                   not null,
    createTime         timestamp                       default '1970-01-02 00:00:00'                not null,
    password           varchar(255)                    default ''                                   not null,
    nickname           varchar(255)                    default ''                                   not null,
    school             varchar(255)                    default ''                                   not null,
    phone              varchar(255)                    default ''                                   not null,
    location           varchar(255)                    default ''                                   not null,
    description        varchar(255) charset utf8       default ''                                   not null,
    email              varchar(255)                    default (concat(
            convert(replace(uuid(), _utf8mb3'-', _utf8mb4'') using utf8mb4), _utf8mb4'@mysql.com')) not null,
    banned             tinyint(1)                      default 0                                    not null,
    unlockTime         timestamp                       default '1970-01-02 00:00:00'                not null,
    passwordErrorCount int                             default 0                                    not null,
    score              int                             default 0                                    not null,
    region             varchar(30)                     default ''                                   not null,
    avatar             varchar(255)                    default ''                                   not null,
    version            int                             default 1                                    not null,
    constraint uh_user_email_index
        unique (email),
    constraint uh_user_username_uindex
        unique (username)
);

INSERT INTO universityhelper.uh_user (userId, sex, username, createTime, password, nickname, school, phone, location, description, email, banned, unlockTime, passwordErrorCount, score, region, avatar, version) VALUES (10, '男', '2027405036', '2023-03-23 20:43:26', '$2a$10$z1SCx.jZhUjMhXHG5ONbOuqM7jdJie8le7Lcy8VabHT17WETLT8cS', 'nickname', '学校', '电话', '地址', '个人简介', '邮箱', 0, '1970-01-02 00:00:00', 0, 1, '内网IP', '', 11);
INSERT INTO universityhelper.uh_user (userId, sex, username, createTime, password, nickname, school, phone, location, description, email, banned, unlockTime, passwordErrorCount, score, region, avatar, version) VALUES (11, '男', '2027405037', '2023-03-24 11:53:38', '$2a$10$zq/gnkQNSFVQjq6ZGf5qPujqtD7gZJZMHCg4ArHvtnCDFbij7p0ae', 'nickname', '学校', '18806163370', '地址', '我是xyk', '2633915958@qq.com', 0, '2023-04-05 20:31:16', 0, 0, '内网IP', '', 30);
INSERT INTO universityhelper.uh_user (userId, sex, username, createTime, password, nickname, school, phone, location, description, email, banned, unlockTime, passwordErrorCount, score, region, avatar, version) VALUES (18, '男', '2027405035', '2023-04-18 10:26:35', '$2a$10$Ty5yQquVD/.6zSofsNhdW.LsVQPJRsVGCO7NRYtLr7hhGX6U7hb.q', '', '', '', '', '', '', 0, '1970-01-02 00:00:00', 0, 0, '', '', 1);
INSERT INTO universityhelper.uh_user (userId, sex, username, createTime, password, nickname, school, phone, location, description, email, banned, unlockTime, passwordErrorCount, score, region, avatar, version) VALUES (20, '男', '2027405023', '2023-05-19 00:11:06', '$2a$10$Ij9YqS1M7rQcdA/8QapWnuVlP0NqEvY3jvZuirChhywnwXP9j.r7i', '', '', '', '', '', '980990c3f59611edbf0500163e097b2b@mysql.com', 0, '1970-01-02 00:00:00', 0, 0, '内网IP', '', 1);
