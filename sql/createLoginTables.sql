create table if not exists universityhelper.uh_login_picture
(
    pictureId      int auto_increment
        primary key,
    picture        blob         not null,
    userid         int          not null,
    isHeadPortrait tinyint(1)   not null,
    createTime     timestamp    not null,
    description    varchar(255) not null
);

create index uh_login_picture_uh_user_userid_fk
    on universityhelper.uh_login_picture (userid);

create table if not exists universityhelper.uh_role
(
    roleId   int auto_increment
        primary key,
    rolename varchar(255) not null
);

create table if not exists universityhelper.uh_user
(
    userId      int auto_increment
        primary key,
    studentId   int                       not null,
    pictureId   int                       not null,
    description varchar(255) charset utf8 not null,
    location    varchar(255)              not null,
    phone       varchar(255)              not null,
    email       varchar(255)              not null,
    sex         char(2)                   not null,
    password    varchar(255)              not null,
    nickname    varchar(255)              not null,
    username    varchar(255)              not null,
    school      varchar(255)              not null
);

create index uh_user_uh_login_picture_pictureid_fk
    on universityhelper.uh_user (avatar);

create table if not exists universityhelper.uh_user_role
(
    userId int not null,
    roleId int not null,
    id     int auto_increment
        primary key,
    constraint roleid
        unique (roleId, userId)
);

create index uh_user_role_uh_user_userid_fk
    on universityhelper.uh_user_role (userId);


