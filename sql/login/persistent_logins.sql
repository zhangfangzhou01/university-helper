create table persistent_logins
(
    username  varchar(64) not null,
    series    varchar(64) not null
        primary key,
    token     varchar(64) not null,
    last_used timestamp   not null
);

INSERT INTO universityhelper.persistent_logins (username, series, token, last_used) VALUES ('2027405037', 'QVuxAFrwQy7rwuYuwP4KCQ==', 'zFAkXjWGHh+5gsA0zKVG/Q==', '2023-04-22 12:20:50');
INSERT INTO universityhelper.persistent_logins (username, series, token, last_used) VALUES ('2027405037', 'rtQMhRRMqpt4mTzaquQikw==', 'q7L4xVPZHIbI0G3c0RcDlA==', '2023-04-22 12:22:45');
INSERT INTO universityhelper.persistent_logins (username, series, token, last_used) VALUES ('2027405037', 'sIwEhWFUdGFGW7/XvH2T8Q==', 'qPddAUCKGb+NzzZJwrW+aQ==', '2023-04-22 13:08:18');
