create table uh_user_role
(
    userId  bigint default 0 not null,
    roleId  bigint default 0 not null,
    id      bigint auto_increment
        primary key,
    version int    default 1 not null,
    constraint roleid
        unique (roleId, userId)
);

create index uh_user_role_uh_user_userid_fk
    on uh_user_role (userId);

INSERT INTO universityhelper.uh_user_role (userId, roleId, id, version) VALUES (11, 2, 1, 1);
INSERT INTO universityhelper.uh_user_role (userId, roleId, id, version) VALUES (10, 1, 3, 1);
INSERT INTO universityhelper.uh_user_role (userId, roleId, id, version) VALUES (18, 2, 11, 1);
