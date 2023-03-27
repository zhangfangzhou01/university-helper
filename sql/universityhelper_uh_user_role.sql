create table uh_user_role
(
    userId bigint default 0 not null,
    roleId bigint default 0 not null,
    id     bigint auto_increment
        primary key,
    constraint roleid
        unique (roleId, userId)
);

create index uh_user_role_uh_user_userid_fk
    on uh_user_role (userId);

INSERT INTO universityhelper.uh_user_role (userId, roleId, id) VALUES (10, 1, 3);
INSERT INTO universityhelper.uh_user_role (userId, roleId, id) VALUES (11, 1, 1);