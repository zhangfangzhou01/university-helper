create table uh_user
(
    userId             bigint auto_increment
        primary key,
    sex                char                      default ''                    not null,
    createTime         timestamp                 default '1970-01-02 00:00:00' not null,
    username           varchar(255)              default ''                    not null,
    password           varchar(255)              default ''                    not null,
    nickname           varchar(255)              default ''                    not null,
    school             varchar(255)              default ''                    not null,
    phone              varchar(255)              default ''                    not null,
    location           varchar(255)              default ''                    not null,
    description        varchar(255) charset utf8 default ''                    not null,
    email              varchar(255)              default ''                    not null,
    banned             tinyint(1)                default 0                     not null,
    unlockTime         timestamp                 default '1970-01-02 00:00:00' not null,
    passwordErrorCount int                       default 0                     not null,
    score              int                       default 0                     not null,
    avatar             varchar(255)              default ''                    not null,
    region             varchar(50)               default ''                    null,
    constraint uh_user_username_uindex
        unique (username)
) engine = InnoDB
  default charset = utf8mb4;

create index uh_user_uh_login_picture_pictureid_fk
    on uh_user (avatar);

INSERT INTO universityhelper.uh_user (userId, sex, createTime, username, password, nickname, school, phone, location,
                                      description, email, banned, unlockTime, passwordErrorCount, score, avatar, region)
VALUES (10, '男', '2023-03-23 20:43:26', '2027405036', '$2a$10$z1SCx.jZhUjMhXHG5ONbOuqM7jdJie8le7Lcy8VabHT17WETLT8cS',
        'nickname', '学校', '电话', '地址', '个人简介', '邮箱', 0, '1970-01-02 00:00:00', 0, 1, '', '内网IP');
INSERT INTO universityhelper.uh_user (userId, sex, createTime, username, password, nickname, school, phone, location,
                                      description, email, banned, unlockTime, passwordErrorCount, score, avatar, region)
VALUES (11, '男', '2023-03-24 11:53:38', '2027405037', '$2a$10$zq/gnkQNSFVQjq6ZGf5qPujqtD7gZJZMHCg4ArHvtnCDFbij7p0ae',
        'nickname', '学校', '18806163370', '地址', '我是xyk', '28@qq.com', 0, '2023-04-05 20:31:16', 0, 0,
        'http://localhost:8080/doc.html#/default/用户管理/updateUsingPOST_1', '内网IP');
INSERT INTO universityhelper.uh_user (userId, sex, createTime, username, password, nickname, school, phone, location,
                                      description, email, banned, unlockTime, passwordErrorCount, score, avatar, region)
VALUES (18, '', '2023-04-18 10:26:35', '2027405035', '$2a$10$Ty5yQquVD/.6zSofsNhdW.LsVQPJRsVGCO7NRYtLr7hhGX6U7hb.q', '',
        '', '', '', '', '', 0, '1970-01-02 00:00:00', 0, 0, '', '内网IP');
