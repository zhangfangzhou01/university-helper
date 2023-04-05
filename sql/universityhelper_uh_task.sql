create table uh_task
(
    taskId              bigint auto_increment,
    userId              bigint                  not null,
    priority            int          default 0  not null,
    type                varchar(255) default '' not null,
    tags                varchar(255) default '' not null,
    releaseTime         timestamp               not null,
    title               varchar(255) default '' not null,
    requireDescription  varchar(255) default '' not null,
    maxNumOfPeopleTake  int          default 1  not null,
    expectedPeriod      int          default 0  not null,
    taskState           int          default 0  not null,
    score               int          default 0  not null,
    takeoutId           int          default 0  not null,
    orderTime           timestamp               not null,
    arrivalTime         timestamp               not null,
    arrivalLocation     varchar(255) default '' not null,
    targetLocation      varchar(255) default '' not null,
    distance            int          default 0  not null,
    phoneNumForNow      int          default 0  not null,
    transactionAmount   double       default 0  not null,
    isHunter            int          default 0  not null,
    leftNumOfPeopleTake int          default 1  not null,
    constraint uh_task_pk
        unique (taskId)
);

INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (3, 11, 0, '外卖', '["外卖", "红色"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0, '2020-12-12 12:12:12',
        '2020-12-12 12:12:12', '东校区西门', '东八426', 0, 0, 100, 0, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (4, 11, 0, '外卖', '["外卖", "红色"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0, '2020-12-12 12:12:12',
        '2020-12-12 12:12:12', '东校区西门', '东八426', 0, 0, 100, 0, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (5, 11, 0, '外卖', '["外卖", "红色"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0, '2020-12-12 12:12:12',
        '2020-12-12 12:12:12', '东校区西门', '东八426', 0, 0, 100, 0, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (6, 11, 0, '外卖', '["外卖", "红色"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0, '2020-12-12 12:12:12',
        '2020-12-12 12:12:12', '东校区西门', '东八426', 0, 0, 100, 0, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (7, 11, 0, '外卖', '["外卖","红色"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0, '2020-12-12 12:12:12',
        '2020-12-12 12:12:12', '东校区西门', '东八426', 0, 0, 100, 0, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (8, 11, 0, '外卖', '["外卖","红色"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0, '2020-12-12 12:12:12',
        '2020-12-12 12:12:12', '东校区西门', '东八426', 0, 0, 100, 0, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (9, 11, 0, '外卖', '["外卖","红色"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0, '2020-12-12 12:12:12',
        '2020-12-12 12:12:12', '东校区西门', '东八426', 0, 0, 100, 0, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (12, 11, 1, '外卖', '["外卖","市场","Java","你好"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 1, 0, 0,
        '2020-12-12 12:12:12', '2020-12-12 12:12:12', '东校区西门', '东八426', 0, 0, 100, 0, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (13, 11, 0, '外卖', '["外卖","红色"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0, '2020-12-12 12:12:12',
        '2020-12-12 12:12:12', '东校区西门', '东八426', 0, 0, 100, 0, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (14, 11, 0, '外卖', '["外卖","红色"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0, '2020-12-12 12:12:12',
        '2020-12-12 12:12:12', '东校区西门', '东八426', 0, 0, 100, 0, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (15, 11, 0, '外卖', '["外卖","红色"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0, '2020-12-12 12:12:12',
        '2020-12-12 12:12:12', '东校区西门', '东八426', 0, 0, 100, 0, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (16, 11, 0, '外卖', '["外卖","红色"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0, '2020-12-12 12:12:12',
        '2020-12-12 12:12:12', '东校区西门', '东八426', 0, 0, 100, 0, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (17, 11, 0, '外卖', '["外卖","黄色"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0, '2020-12-12 12:12:12',
        '2020-12-12 12:12:12', '东校区西门', '东八426', 0, 0, 100, 0, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (18, 11, 0, '外卖', '["外卖","紫色"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0, '2020-12-12 12:12:12',
        '2020-12-12 12:12:12', '东校区西门', '东八426', 0, 0, 100, 0, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (22, 11, 0, '交易', '["交易","紫色"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0, '2020-12-12 12:12:12',
        '2020-12-12 12:12:12', '东校区西门', '东八426', 0, 0, 100, 1, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (23, 11, 0, '交易', '["交易","紫色"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0, '2020-12-12 12:12:12',
        '2020-12-12 12:12:11', '东校区西门', '东八426', 0, 0, 100, 1, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (24, 11, 0, '交易', '["交易","紫色","a","b","c"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0,
        '2020-12-12 12:12:12', '2020-12-12 12:12:11', '东校区西门', '东八426', 0, 0, 100, 1, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (25, 11, 0, '交易', '["交易","紫色","a","b","c"]', '2020-12-12 12:12:12', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0,
        '2020-12-12 12:12:12', '2020-12-12 12:12:11', '东校区西门', '东八426', 0, 0, 100, 1, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (26, 11, 0, '交易', '["交易","紫色","a","b","c"]', '2020-12-12 12:12:01', '这个是外卖任务', '帮我拿外卖', 1, 1, 0, 0, 0,
        '2020-12-12 12:12:01', '2020-12-12 12:12:11', '东校区西门', '东八426', 0, 0, 100, 1, 1);
INSERT INTO universityhelper.uh_task (taskId, userId, priority, type, tags, releaseTime, title, requireDescription,
                                      maxNumOfPeopleTake, expectedPeriod, taskState, score, takeoutId, orderTime,
                                      arrivalTime, arrivalLocation, targetLocation, distance, phoneNumForNow,
                                      transactionAmount, isHunter, leftNumOfPeopleTake)
VALUES (27, 11, 0, '交易', '["交易","紫色","a","b","c"]', '2020-12-12 12:12:12', '期货交易，现金交易，以物易物，货到付款',
        '交易，又称贸易、交换、互市，是买卖双方对有价物品及服务进行互通有无的行为。[1][2]可以是以货币为交易媒介的过程，也可以是以物易物，例如一只黄牛交换三只猪。', 1, 1, 0, 0, 0,
        '2020-12-12 12:12:12', '2020-12-12 12:12:11', '东校区西门', '东八426', 0, 0, 100, 1, 1);