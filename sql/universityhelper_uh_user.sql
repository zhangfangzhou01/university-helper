create table uh_user
(
    userId      bigint auto_increment
        primary key,
    sex         char(2)                   default '' not null,
    createTime  timestamp                            not null,
    username    varchar(255)              default '' not null,
    password    varchar(255)              default '' not null,
    nickname    varchar(255)              default '' not null,
    school      varchar(255)              default '' not null,
    phone       varchar(255)              default '' not null,
    location    varchar(255)              default '' not null,
    description varchar(255) charset utf8 default '' not null,
    email       varchar(255)              default '' not null,
    studentId   int                       default 0  not null,
    score       int                       default 0  not null,
    avatar      varchar(255)              default '' not null,
    banned      tinyint(1)                default 0  not null,
    constraint uh_user_username_uindex
        unique (username)
);

create index uh_user_uh_login_picture_pictureid_fk
    on uh_user (avatar);

INSERT INTO universityhelper.uh_user (userId, sex, createTime, username, password, nickname, school, phone, location,
                                      description, email, studentId, score, avatar, banned)
VALUES (10, '男', '2023-03-23 20:43:26', '2027405036', '$2a$10$QlL1zEC7.vFZ0RyI7LjEGOPT6Tc6jO09Qou/T8/sEa2wm35.Sktru',
        'nickname', '学校', '电话', '地址', '个人简介', '邮箱', 2, 0, '', 0);
INSERT INTO universityhelper.uh_user (userId, sex, createTime, username, password, nickname, school, phone, location,
                                      description, email, studentId, score, avatar, banned)
VALUES (11, '男', '2023-03-24 11:53:38', '2027405037', '$2a$10$teJcu8AOmHb7u4w0UFAwOe3PIyK2YrtzfbH0hwqp6kC/LUp0.zTGi',
        'nickname', '学校', '电话', '地址', '我是zfz', '邮箱', 2, 0, '', 0);