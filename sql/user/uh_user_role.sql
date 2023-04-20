create table uh_user_role
(
    userId bigint default 0 not null,
    roleId bigint default 0 not null,
    id     bigint auto_increment
        primary key,
    constraint roleid
        unique (roleId, userId)
) engine = InnoDB
  default charset = utf8mb4;

create index uh_user_role_uh_user_userid_fk
    on uh_user_role (userId);

INSERT INTO universityhelper.uh_user_role (userId, roleId, id)
VALUES (10, 1, 3);
INSERT INTO universityhelper.uh_user_role (userId, roleId, id)
VALUES (11, 2, 1);
INSERT INTO universityhelper.uh_user_role (userId, roleId, id)
VALUES (18, 2, 11);
