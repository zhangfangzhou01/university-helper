create table universityhelper.uh_task
(
    taskId             int auto_increment,
    userId             int          default 0  not null,
    tags               varchar(255)            not null,
    priority           int          default 1  not null,
    releaseTime        timestamp               not null,
    title              varchar(255) default '' not null,
    requireDescription varchar(255) default '' not null,
    maxNumOfPeopleTake int          default 1  not null,
    expectedPeriod     int          default 1  not null,
    score              int          default 0  not null,
    taskState          int          default 0  not null,
    takeoutId          int          default 0  not null,
    orderTime          timestamp               not null,
    arrivalTime        timestamp               not null,
    arrivalLocation    varchar(255)            not null,
    targetLocation     varchar(255)            not null,
    distance           int          default 0  not null,
    phoneNumForNow     int          default 0  not null,
    transactionAmount  double       default 0  not null,
    isHunter           int          default 0  not null,
    constraint uh_task_pk
        unique (taskId)
);

