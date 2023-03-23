use universityhelper;

create table uh_task
(
    taskId             int auto_increment,

#   com
    userId             int                     not null,
#   因为是select的时候排序，所以priority字段的数据胡存储没什么用，但是其po有用
    priority           int                     not null,
#     核心tag, 外卖， 交易，
#  tags:  " ["外卖", 辣"] " 之类的
    tags               varchar(255)            not null,
    releaseTime        timestamp               not null,
    title              varchar(255) default '' not null,
    requireDescription varchar(255) default '' not null,
    maxNumOfPeopleTake int          default 1  not null,
#     任务预计悬挂时间，
    expectedPeriod     timestamp,
#     发布未领取，发布已领取，发布已完成三种状态
    taskState          int          default 0  not null,
#   评分
    score              int          default 0  not null,
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
    phoneNumForNow     varchar(20)             not null,

#   for transaction
    transactionAmount  int                     not null,

#   for taskHunter
    isHunter           int          default 0  not null

);

