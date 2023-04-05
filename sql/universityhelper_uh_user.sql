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
    avatar             varchar(255)              default ''                    not null,
    score              int                       default 0                     not null,
    passwordErrorCount int                       default 0                     not null,
    unlockTime         timestamp                 default '1970-01-02 00:00:00' not null,
    banned             tinyint(1)                default 0                     not null,
    constraint uh_user_username_uindex
        unique (username)
);

create index uh_user_uh_login_picture_pictureid_fk
    on uh_user (avatar);

INSERT INTO universityhelper.uh_user (userId, sex, createTime, username, password, nickname, school, phone, location,
                                      description, email, banned, unlockTime, passwordErrorCount, score, avatar)
VALUES (10, '男', '2023-03-23 20:43:26', '2027405036', '$2a$10$z1SCx.jZhUjMhXHG5ONbOuqM7jdJie8le7Lcy8VabHT17WETLT8cS',
        'nickname', '学校', '电话', '地址', '个人简介', '邮箱', 0, '1970-01-02 00:00:00', 1, 1, '');
INSERT INTO universityhelper.uh_user (userId, sex, createTime, username, password, nickname, school, phone, location,
                                      description, email, banned, unlockTime, passwordErrorCount, score, avatar)
VALUES (11, '男', '2023-03-24 11:53:38', '2027405037', '$2a$10$zq/gnkQNSFVQjq6ZGf5qPujqtD7gZJZMHCg4ArHvtnCDFbij7p0ae',
        'nickname', '学校', '18806163370', '地址', '我是zfz', '2633915958@qq.com', 0, '2023-04-01 16:28:43', 0, 0,
        'http://localhost:8080/doc.html#/default/用户管理/updateUsingPOST_1');