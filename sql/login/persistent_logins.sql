create table persistent_logins
(
    username  varchar(64) not null,
    series    varchar(64) not null
        primary key,
    token     varchar(64) not null,
    last_used timestamp   not null
) engine = InnoDB
  default charset = utf8mb4;

INSERT INTO universityhelper.persistent_logins (username, series, token, last_used)
VALUES ('2027405037', 'a6VmZNoT1f828+qzGZST/g==', 'rxLM9HkhLHKTuuocKCOqRw==', '2023-04-18 11:57:59');
INSERT INTO universityhelper.persistent_logins (username, series, token, last_used)
VALUES ('2027405037', 'C3H9eTc5H3hFVxJjwWnVsQ==', 'Q7TFRG1PL8OK7kl8HN3s5g==', '2023-04-19 13:34:13');
INSERT INTO universityhelper.persistent_logins (username, series, token, last_used)
VALUES ('2027405036', 'c4IiNY/h5zvzCtlXF1kFDQ==', 'c0NtMS70BBooirvmO/OSCQ==', '2023-04-18 12:00:53');
INSERT INTO universityhelper.persistent_logins (username, series, token, last_used)
VALUES ('2027405036', 'Efk8NBIXWMWMD+SgWPeiNw==', '277OOitKFVqirTp1VAV/wg==', '2023-04-18 11:58:02');
INSERT INTO universityhelper.persistent_logins (username, series, token, last_used)
VALUES ('2027405036', 'pFBNnnFwuK4ewq1so1paqw==', 'AWysXI9IwbNcBd37DvDCcg==', '2023-04-01 01:30:55');
INSERT INTO universityhelper.persistent_logins (username, series, token, last_used)
VALUES ('2027405035', 'R0xKixH8Oi0+x+Miwjkfbw==', 'IL6TQGvkvhtsy0d5Ol59Qg==', '2023-04-18 02:26:45');
INSERT INTO universityhelper.persistent_logins (username, series, token, last_used)
VALUES ('2027405036', 'RNzgUlMOifi8V2/gZQNewQ==', '8MywgE9iEbRypBW1oHR/Ew==', '2023-04-18 11:57:30');
INSERT INTO universityhelper.persistent_logins (username, series, token, last_used)
VALUES ('2027405037', 'sQBMG3o7R1MuB8UrhPTB1w==', '4E5+bOiYPDAL9OlxME/ViA==', '2023-04-19 03:09:56');
INSERT INTO universityhelper.persistent_logins (username, series, token, last_used)
VALUES ('2027405037', 'v4Y5QRbcX6ycHk0NadkB3g==', 'Y05/mpzskkmkAW1QSIDjtg==', '2023-04-19 12:56:25');
