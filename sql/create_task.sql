use universityhelper;

create table uh_task
(
    taskId             int auto_increment,

#   com
    userId             int                     not null,
    priority           int                     not null,
    tags               varchar(255)            not null,
    releaseTime        timestamp               not null,
    title              varchar(255) default '' not null,
    requireDescription varchar(255) default '' not null,
    maxNumOfPeopleTake int          default 1  not null,
    completeFlag       tinyint(1)   default 0  null,
    constraint uh_task_pk
        unique (taskId),

#   for takeout
    takeoutId          int                     not null,
    orderTime          timestamp               not null,
    arrivalTime        timestamp               not null,
    arrivalLocation    varchar(255)            not null,
    targetLocation     varchar(255)            not null,
    # 外卖送到学校的地点 和 外卖接受者在学校的地点 的 距离
    distance           int                     not null,
    # 有后续更改手机号的问题
    phoneNumForNow     int(4)                  not null,

#   for transaction
    transactionAmount  int                     not null,
    expectedPeriod     int                     not null

);

