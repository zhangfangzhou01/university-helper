create table persistent_logins
(
    username  varchar(64) not null,
    series    varchar(64) not null
        primary key,
    token     varchar(64) not null,
    last_used timestamp   not null
);

INSERT INTO universityhelper.persistent_logins (username, series, token, last_used)
VALUES ('2027405037', 'bVX/heR8Go5kCkaZ1GwTlQ==', 'VZF9KvYZ/T8WoKxYsRvJbA==', '2023-03-26 02:38:24');