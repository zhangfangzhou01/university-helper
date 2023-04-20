create table uh_role
(
    roleId   bigint auto_increment
        primary key,
    rolename varchar(255) default 'ROLE_USER' not null
) engine = InnoDB
  default charset = utf8mb4;

INSERT INTO universityhelper.uh_role (roleId, rolename)
VALUES (1, 'ROLE_ADMIN');
INSERT INTO universityhelper.uh_role (roleId, rolename)
VALUES (2, 'ROLE_USER');
