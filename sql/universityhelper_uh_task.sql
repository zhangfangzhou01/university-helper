create table uh_task
(
    taskId             bigint auto_increment,
    userId             bigint                  not null,
    expectedPeriod     int          default 0  not null,
    transactionAmount  int          default 0  not null,
    phoneNumForNow     int          default 0  not null,
    distance           int          default 0  not null,
    targetLocation     varchar(255) default '' not null,
    arrivalLocation    varchar(255) default '' not null,
    arrivalTime        timestamp               not null,
    orderTime          timestamp               not null,
    takeoutId          int          default 0  not null,
    completeFlag       tinyint(1)   default 0  null,
    maxNumOfPeopleTake int          default 1  not null,
    requireDescription varchar(255) default '' not null,
    title              varchar(255) default '' not null,
    releaseTime        timestamp               not null,
    tags               varchar(255) default '' not null,
    priority           int          default 0  not null,
    score              int          default 0  not null,
    taskState          int          default 0  not null,
    isHunter           int          default 0  not null,
    constraint uh_task_pk
        unique (taskId)
);

INSERT INTO universityhelper.uh_task (taskId, userId, expectedPeriod, transactionAmount, phoneNumForNow, distance,
                                      targetLocation, arrivalLocation, arrivalTime, orderTime, takeoutId, completeFlag,
                                      maxNumOfPeopleTake, requireDescription, title, releaseTime, tags, priority, score,
                                      taskState, isHunter)
VALUES (3, 11, 1, 100, 0, 0, '东八426', '东校区西门', '2020-12-12 12:12:12', '2020-12-12 12:12:12', 0, 0, 1, '帮我拿外卖',
        '这个是外卖任务', '2020-12-12 12:12:12', '["外卖", "红色"]', 0, 0, 0, 0);
INSERT INTO universityhelper.uh_task (taskId, userId, expectedPeriod, transactionAmount, phoneNumForNow, distance,
                                      targetLocation, arrivalLocation, arrivalTime, orderTime, takeoutId, completeFlag,
                                      maxNumOfPeopleTake, requireDescription, title, releaseTime, tags, priority, score,
                                      taskState, isHunter)
VALUES (4, 11, 1, 100, 0, 0, '东八426', '东校区西门', '2020-12-12 12:12:12', '2020-12-12 12:12:12', 0, 0, 1, '帮我拿外卖',
        '这个是外卖任务', '2020-12-12 12:12:12', '["外卖", "红色"]', 0, 0, 0, 0);
INSERT INTO universityhelper.uh_task (taskId, userId, expectedPeriod, transactionAmount, phoneNumForNow, distance,
                                      targetLocation, arrivalLocation, arrivalTime, orderTime, takeoutId, completeFlag,
                                      maxNumOfPeopleTake, requireDescription, title, releaseTime, tags, priority, score,
                                      taskState, isHunter)
VALUES (5, 11, 1, 100, 0, 0, '东八426', '东校区西门', '2020-12-12 12:12:12', '2020-12-12 12:12:12', 0, 0, 1, '帮我拿外卖',
        '这个是外卖任务', '2020-12-12 12:12:12', '["外卖", "红色"]', 0, 0, 0, 0);
INSERT INTO universityhelper.uh_task (taskId, userId, expectedPeriod, transactionAmount, phoneNumForNow, distance,
                                      targetLocation, arrivalLocation, arrivalTime, orderTime, takeoutId, completeFlag,
                                      maxNumOfPeopleTake, requireDescription, title, releaseTime, tags, priority, score,
                                      taskState, isHunter)
VALUES (6, 11, 1, 100, 0, 0, '东八426', '东校区西门', '2020-12-12 12:12:12', '2020-12-12 12:12:12', 0, 0, 1, '帮我拿外卖',
        '这个是外卖任务', '2020-12-12 12:12:12', '["外卖", "红色"]', 0, 0, 0, 0);
INSERT INTO universityhelper.uh_task (taskId, userId, expectedPeriod, transactionAmount, phoneNumForNow, distance,
                                      targetLocation, arrivalLocation, arrivalTime, orderTime, takeoutId, completeFlag,
                                      maxNumOfPeopleTake, requireDescription, title, releaseTime, tags, priority, score,
                                      taskState, isHunter)
VALUES (7, 11, 1, 100, 0, 0, '东八426', '东校区西门', '2020-12-12 12:12:12', '2020-12-12 12:12:12', 0, 0, 1, '帮我拿外卖',
        '这个是外卖任务', '2020-12-12 12:12:12', '["外卖","红色"]', 0, 0, 0, 0);
INSERT INTO universityhelper.uh_task (taskId, userId, expectedPeriod, transactionAmount, phoneNumForNow, distance,
                                      targetLocation, arrivalLocation, arrivalTime, orderTime, takeoutId, completeFlag,
                                      maxNumOfPeopleTake, requireDescription, title, releaseTime, tags, priority, score,
                                      taskState, isHunter)
VALUES (8, 11, 1, 100, 0, 0, '东八426', '东校区西门', '2020-12-12 12:12:12', '2020-12-12 12:12:12', 0, 0, 1, '帮我拿外卖',
        '这个是外卖任务', '2020-12-12 12:12:12', '["外卖","红色"]', 0, 0, 0, 0);
INSERT INTO universityhelper.uh_task (taskId, userId, expectedPeriod, transactionAmount, phoneNumForNow, distance,
                                      targetLocation, arrivalLocation, arrivalTime, orderTime, takeoutId, completeFlag,
                                      maxNumOfPeopleTake, requireDescription, title, releaseTime, tags, priority, score,
                                      taskState, isHunter)
VALUES (9, 11, 1, 100, 0, 0, '东八426', '东校区西门', '2020-12-12 12:12:12', '2020-12-12 12:12:12', 0, 0, 1, '帮我拿外卖',
        '这个是外卖任务', '2020-12-12 12:12:12', '["外卖","红色"]', 0, 0, 0, 0);
INSERT INTO universityhelper.uh_task (taskId, userId, expectedPeriod, transactionAmount, phoneNumForNow, distance,
                                      targetLocation, arrivalLocation, arrivalTime, orderTime, takeoutId, completeFlag,
                                      maxNumOfPeopleTake, requireDescription, title, releaseTime, tags, priority, score,
                                      taskState, isHunter)
VALUES (12, 11, 1, 100, 0, 0, '东八426', '东校区西门', '2020-12-12 12:12:12', '2020-12-12 12:12:12', 0, 0, 1, '帮我拿外卖',
        '这个是外卖任务', '2020-12-12 12:12:12', '["交易","市场","Java","你好"]', 0, 0, 1, 0);
INSERT INTO universityhelper.uh_task (taskId, userId, expectedPeriod, transactionAmount, phoneNumForNow, distance,
                                      targetLocation, arrivalLocation, arrivalTime, orderTime, takeoutId, completeFlag,
                                      maxNumOfPeopleTake, requireDescription, title, releaseTime, tags, priority, score,
                                      taskState, isHunter)
VALUES (13, 11, 1, 100, 0, 0, '东八426', '东校区西门', '2020-12-12 12:12:12', '2020-12-12 12:12:12', 0, 0, 1, '帮我拿外卖',
        '这个是外卖任务', '2020-12-12 12:12:12', '["外卖","红色"]', 0, 0, 0, 0);
INSERT INTO universityhelper.uh_task (taskId, userId, expectedPeriod, transactionAmount, phoneNumForNow, distance,
                                      targetLocation, arrivalLocation, arrivalTime, orderTime, takeoutId, completeFlag,
                                      maxNumOfPeopleTake, requireDescription, title, releaseTime, tags, priority, score,
                                      taskState, isHunter)
VALUES (14, 11, 1, 100, 0, 0, '东八426', '东校区西门', '2020-12-12 12:12:12', '2020-12-12 12:12:12', 0, 0, 1, '帮我拿外卖',
        '这个是外卖任务', '2020-12-12 12:12:12', '["外卖","红色"]', 0, 0, 0, 0);
INSERT INTO universityhelper.uh_task (taskId, userId, expectedPeriod, transactionAmount, phoneNumForNow, distance,
                                      targetLocation, arrivalLocation, arrivalTime, orderTime, takeoutId, completeFlag,
                                      maxNumOfPeopleTake, requireDescription, title, releaseTime, tags, priority, score,
                                      taskState, isHunter)
VALUES (15, 11, 1, 100, 0, 0, '东八426', '东校区西门', '2020-12-12 12:12:12', '2020-12-12 12:12:12', 0, 0, 1, '帮我拿外卖',
        '这个是外卖任务', '2020-12-12 12:12:12', '["外卖","红色"]', 0, 0, 0, 0);
INSERT INTO universityhelper.uh_task (taskId, userId, expectedPeriod, transactionAmount, phoneNumForNow, distance,
                                      targetLocation, arrivalLocation, arrivalTime, orderTime, takeoutId, completeFlag,
                                      maxNumOfPeopleTake, requireDescription, title, releaseTime, tags, priority, score,
                                      taskState, isHunter)
VALUES (16, 11, 1, 100, 0, 0, '东八426', '东校区西门', '2020-12-12 12:12:12', '2020-12-12 12:12:12', 0, 0, 1, '帮我拿外卖',
        '这个是外卖任务', '2020-12-12 12:12:12', '["外卖","红色"]', 0, 0, 0, 0);
INSERT INTO universityhelper.uh_task (taskId, userId, expectedPeriod, transactionAmount, phoneNumForNow, distance,
                                      targetLocation, arrivalLocation, arrivalTime, orderTime, takeoutId, completeFlag,
                                      maxNumOfPeopleTake, requireDescription, title, releaseTime, tags, priority, score,
                                      taskState, isHunter)
VALUES (17, 11, 1, 100, 0, 0, '东八426', '东校区西门', '2020-12-12 12:12:12', '2020-12-12 12:12:12', 0, 0, 1, '帮我拿外卖',
        '这个是外卖任务', '2020-12-12 12:12:12', '["外卖","黄色"]', 0, 0, 0, 0);
INSERT INTO universityhelper.uh_task (taskId, userId, expectedPeriod, transactionAmount, phoneNumForNow, distance,
                                      targetLocation, arrivalLocation, arrivalTime, orderTime, takeoutId, completeFlag,
                                      maxNumOfPeopleTake, requireDescription, title, releaseTime, tags, priority, score,
                                      taskState, isHunter)
VALUES (18, 11, 1, 100, 0, 0, '东八426', '东校区西门', '2020-12-12 12:12:12', '2020-12-12 12:12:12', 0, 0, 1, '帮我拿外卖',
        '这个是外卖任务', '2020-12-12 12:12:12', '["外卖","紫色"]', 0, 0, 0, 0);
INSERT INTO universityhelper.uh_task (taskId, userId, expectedPeriod, transactionAmount, phoneNumForNow, distance,
                                      targetLocation, arrivalLocation, arrivalTime, orderTime, takeoutId, completeFlag,
                                      maxNumOfPeopleTake, requireDescription, title, releaseTime, tags, priority, score,
                                      taskState, isHunter)
VALUES (19, 11, 1, 100, 0, 0, '东八426', '东校区西门', '2020-12-12 12:12:12', '2020-12-12 12:12:12', 0, 0, 1, '帮我拿外卖',
        '这个是外卖任务', '2020-12-12 12:12:12', '["交易","紫色"]', 0, 0, 0, 0);