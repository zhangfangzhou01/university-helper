create table persistent_logins
(
    username  varchar(64) not null,
    series    varchar(64) not null
        primary key,
    token     varchar(64) not null,
    last_used timestamp   not null
);

INSERT INTO universityhelper.persistent_logins (username, series, token, last_used)
VALUES ('2027405037', 'j35OLYvCPEkc/Xj55a2BDg==', 'WZGlgwdxYr94ZQCnDRP5Ag==', '2023-04-01 15:48:16');
INSERT INTO universityhelper.persistent_logins (username, series, token, last_used)
VALUES ('2027405036', 'pFBNnnFwuK4ewq1so1paqw==', 'AWysXI9IwbNcBd37DvDCcg==', '2023-04-01 01:30:55');