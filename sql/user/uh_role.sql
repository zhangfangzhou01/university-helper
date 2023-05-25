create table uh_role
(
    roleId   bigint auto_increment
        primary key,
    rolename varchar(255) default 'ROLE_USER' not null,
    version  int          default 1           not null
);

INSERT INTO universityhelper.uh_role (roleId, rolename, version) VALUES (1, 'ROLE_ADMIN', 1);
INSERT INTO universityhelper.uh_role (roleId, rolename, version) VALUES (2, 'ROLE_USER', 1);
INSERT INTO universityhelper.uh_role (roleId, rolename, version) VALUES (3, 'ROLE_ANONYMOUS', 1);
