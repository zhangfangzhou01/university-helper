package com.yhm.universityhelper.validation;


import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yhm.universityhelper.dao.TaskMapper;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.dao.UserRoleMapper;
import com.yhm.universityhelper.dao.UsertaketaskMapper;
import com.yhm.universityhelper.entity.po.*;
import com.yhm.universityhelper.util.BeanUtils;

import java.util.Optional;

/*insert(用户能设置的):
    tags（必须给）
    title（必须给）
    requireDescription（可以给，也可以不给）
    交易：maxNumOfPeopleTake（交易必须给）
    expectedPeriod（可以给，也可以不给）
    takeoutId（外卖必须给）
    orderTime（外卖必须给）
    arrivalTime（外卖必须给）
    arrivalLocation（外卖必须给）
    targetLocation（外卖必须给）
    transactionAmount（必须给）

insert(用户不能设置的):
    userId（但是必须给）
    taskId（但是必须给）
    isHunter（但是必须给）
    priority（默认0，不用给）
    type（但是必须给）
    releaseTime（系统自动给）
    外卖: maxNumOfPeopleTake（外卖默认1，不用给）
    taskState（默认0，不用给）
    distance（系统算出来的，不用给）
    phoneNumberForNow（根据用户信息自动填充，不用给）
    leftNumOfPeopleTake（系统算出来的，不用给）
    score（插入的时候不能给，只能在任务完成的时候给）

update(用户能改的):
    和insert一样

update(用户不能改的):
    和insert一样*/
public class TaskValidator extends CustomValidator {
    public static void insert(JSONObject task) {
        // 插入时必须给的
        Optional.ofNullable(task.getStr("userId"))
                .map(userId -> Validator.validateNumber(userId, "用户ID不合法"))
                .map(Long::parseLong)
                .map(taskId -> CustomValidator.validateBetween("用户ID", taskId, 1, Long.MAX_VALUE))
                .map(userId -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, userId)))
                .map(exists -> Validator.validateTrue(exists, "用户ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户ID"));

        Optional.ofNullable(task.getStr("isHunter"))
                .map(isHunter -> Validator.validateMatchRegex("[01]", isHunter, "0:雇主，1:猎人"))
                .orElseThrow(() -> new ValidateException("必须提供isHunter"));

        Optional.ofNullable(task.getStr("type"))
                .map(type -> Validator.validateMatchRegex("(交易|外卖)", type, "类型必须是交易或外卖"))
                .orElseThrow(() -> new ValidateException("必须提供type"));

        Optional.ofNullable(task.getStr("tags"))
                .map(tags -> Validator.validateMatchRegex(JSON_ARRAY_REGEX, tags, "tags必须是json数组"))
                .map(tags -> Validator.validateMatchRegex(".{1,255}", tags, "tags长度必须在1-255之间"))
                .orElseThrow(() -> new ValidateException("必须提供tags"));

        Optional.ofNullable(task.getStr("title"))
                .map(title -> Validator.validateMatchRegex(".{1,255}", title, "标题长度必须在1-255之间"))
                .orElseThrow(() -> new ValidateException("必须提供title"));

        Optional.ofNullable(task.getStr("transactionAmount"))
                .map(transactionAmount -> Validator.validateNumber(transactionAmount, "交易金额不合法"))
                .map(Double::parseDouble)
                .map(transactionAmount -> CustomValidator.validateBetween("交易金额", transactionAmount, 0, Double.MAX_VALUE))
                .orElseThrow(() -> new ValidateException("必须提供transactionAmount"));

        // 插入时不能给的
        Validator.validateNull(task.getStr("taskId"), "taskId由系统自动填充");
        Validator.validateNull(task.getStr("priority"), "priority由系统自动填充");
        Validator.validateNull(task.getStr("releaseTime"), "releaseTime由系统自动填充");
        Validator.validateNull(task.getStr("taskState"), "taskState在创建任务时默认为0");
        Validator.validateNull(task.getStr("distance"), "distance由系统自动算出");
        Validator.validateNull(task.getStr("phoneNumberForNow"), "phoneNumberForNow由系统自动填充");
        Validator.validateNull(task.getStr("leftNumOfPeopleTake"), "leftNumOfPeopleTake由系统算出");
        Validator.validateNull(task.getStr("score"), "score只有在任务完成的时候才能提供");

        // 两种类型都可以给的
        Optional.ofNullable(task.getStr("requireDescription"))
                .map(requireDescription -> Validator.validateMatchRegex(".{1,255}", requireDescription, "requireDescription长度必须在1-255之间"));

        Optional.ofNullable(task.getStr("expectedPeriod"))
                .map(expectedPeriod -> Validator.validateNumber(expectedPeriod, "expectedPeriod不合法"));

        if ("交易".equals(task.getStr("type"))) {
            // 交易必须给的
            Optional.ofNullable(task.getStr("maxNumOfPeopleTake"))
                    .map(maxNumOfPeopleTake -> Validator.validateNumber(maxNumOfPeopleTake, "最大接单人数不合法"))
                    .map(Integer::parseInt)
                    .map(maxNumOfPeopleTake -> CustomValidator.validateBetween("最大接单人数", maxNumOfPeopleTake, 1, Integer.MAX_VALUE))
                    .orElseThrow(() -> new ValidateException("必须提供maxNumOfPeopleTake"));

            // 交易不能给的
            Validator.validateNull(task.getStr("takeoutId"), "交易任务不需要提供takeoutId");
            Validator.validateNull(task.getStr("orderTime"), "交易任务不需要提供orderTime");
            Validator.validateNull(task.getStr("arrivalLocation"), "交易任务不需要提供arrivalLocation");
            Validator.validateNull(task.getStr("arrivalTime"), "交易任务不需要提供arrivalTime");
            Validator.validateNull(task.getStr("targetLocation"), "交易任务不需要提供targetLocation");

        } else if ("外卖".equals(task.getStr("type"))) {
            // 外卖不能给的
            Validator.validateNull(task.getStr("maxNumOfPeopleTake"), "外卖任务默认只有一个接单人，不需要提供maxNumOfPeopleTake");

            // 外卖必须给的
            Optional.ofNullable(task.getStr("takeoutId"))
                    .map(takeoutId -> Validator.validateNumber(takeoutId, "外卖ID不合法"));

            Optional.ofNullable(task.getStr("orderTime"))
                    .map(orderTime -> Validator.validateMatchRegex(DATE_TIME_REGEX, orderTime, "下单时间不合法"))
                    .orElseThrow(() -> new ValidateException("必须提供orderTime"));

            Optional.ofNullable(task.getStr("arrivalTime"))
                    .map(arrivalTime -> Validator.validateMatchRegex(DATE_TIME_REGEX, arrivalTime, "到达时间不合法"))
                    .orElseThrow(() -> new ValidateException("必须提供arrivalTime"));

            Optional.ofNullable(task.getStr("arrivalLocation"))
                    .map(arrivalLocation -> Validator.validateMatchRegex(".{1,255}", arrivalLocation, "到达地点长度必须在1-255之间"))
                    .orElseThrow(() -> new ValidateException("必须提供arrivalLocation"));

            Optional.ofNullable(task.getStr("targetLocation"))
                    .map(targetLocation -> Validator.validateMatchRegex(".{1,255}", targetLocation, "目标地点长度必须在1-255之间"))
                    .orElseThrow(() -> new ValidateException("必须提供targetLocation"));
        }
    }

    public static void update(JSONObject task) {
        // 更新时必须给的
        Optional.ofNullable(task.getStr("taskId"))
                .map(taskId -> Validator.validateNumber(taskId, "任务ID不合法"))
                .map(Long::parseLong)
                .map(taskId -> CustomValidator.validateBetween("任务ID", taskId, 1, Long.MAX_VALUE))
                .map(taskId -> BeanUtils.getBean(TaskMapper.class).exists(new LambdaQueryWrapper<Task>().eq(Task::getTaskId, taskId)))
                .map(exists -> Validator.validateTrue(exists, "任务ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供任务ID"));

        Optional.ofNullable(task.getStr("userId"))
                .map(userId -> Validator.validateNumber(userId, "用户ID不合法"))
                .map(Long::parseLong)
                .map(taskId -> CustomValidator.validateBetween("用户ID", taskId, 1, Long.MAX_VALUE))
                .map(userId -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, userId)))
                .map(exists -> Validator.validateTrue(exists, "用户ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户ID"));

        Optional.ofNullable(task.getStr("type"))
                .map(type -> Validator.validateMatchRegex("(交易|外卖)", type, "任务类型不合法"))
                .orElseThrow(() -> new ValidateException("必须提供任务类型"));

        Validator.validateTrue(task.getStr("type").equals(BeanUtils.getBean(TaskMapper.class).selectById(task.getLong("taskId")).getType()), "禁止修改任务的类型");

        // 任务库内必须存在该任务
        Validator.validateTrue(BeanUtils.getBean(TaskMapper.class).exists(
                        new LambdaQueryWrapper<Task>()
                                .eq(Task::getTaskId, task.getLong("taskId"))
                                .eq(Task::getUserId, task.getLong("userId"))),
                "任务库内不存在该任务");

        // 更新时不能改的
        Validator.validateNull(task.getStr("isHunter"), "禁止修改任务的isHunter");
        Validator.validateNull(task.getStr("transactionAmount"), "禁止修改任务的交易金额");
        Validator.validateNull(task.getStr("priority"), "priority由系统自动填充");
        Validator.validateNull(task.getStr("releaseTime"), "releaseTime由系统自动填充");
        Validator.validateNull(task.getStr("taskState"), "taskState在创建任务时默认为0");
        Validator.validateNull(task.getStr("distance"), "distance由系统自动算出");
        Validator.validateNull(task.getStr("phoneNumberForNow"), "phoneNumberForNow由系统自动填充");
        Validator.validateNull(task.getStr("leftNumOfPeopleTake"), "leftNumOfPeopleTake由系统算出");
        Validator.validateNull(task.getStr("score"), "score只有在任务完成的时候才能提供");

        // 更新时两种类型都可以改的
        Optional.ofNullable(task.getStr("tags"))
                .map(tags -> Validator.validateMatchRegex(JSON_ARRAY_REGEX, tags, "tags必须是json数组"))
                .map(tags -> Validator.validateMatchRegex(".{1,255}", tags, "tags长度必须在1-255之间"));

        Optional.ofNullable(task.getStr("title"))
                .map(title -> Validator.validateMatchRegex(".{1,255}", title, "title长度必须在1-255之间"));

        Optional.ofNullable(task.getStr("requireDescription"))
                .map(requireDescription -> Validator.validateMatchRegex(".{1,255}", requireDescription, "requireDescription长度必须在1-255之间"));

        Optional.ofNullable(task.getStr("expectedPeriod"))
                .map(expectedPeriod -> Validator.validateNumber(expectedPeriod, "expectedPeriod不合法"));

        if ("交易".equals(task.getStr("type"))) {
            // 交易可以改的
            Optional.ofNullable(task.getStr("maxNumOfPeopleTake"))
                    .map(maxNumOfPeopleTake -> Validator.validateNumber(maxNumOfPeopleTake, "最大接单人数不合法"))
                    .map(Integer::parseInt)
                    .map(maxNumOfPeopleTake -> CustomValidator.validateBetween("最大接单人数", maxNumOfPeopleTake, 1, Integer.MAX_VALUE));

            // 交易不能改的
            Validator.validateNull(task.getStr("takeoutId"), "交易任务不能提供takeoutId");
            Validator.validateNull(task.getStr("orderTime"), "交易任务不能提供orderTime");
            Validator.validateNull(task.getStr("arrivalTime"), "交易任务不能提供arrivalTime");
            Validator.validateNull(task.getStr("arrivalLocation"), "交易任务不能提供arrivalLocation");
            Validator.validateNull(task.getStr("targetLocation"), "交易任务不能提供targetLocation");

        } else if ("外卖".equals(task.getStr("type"))) {
            // 外卖不能改的
            Validator.validateNull(task.getStr("maxNumOfPeopleTake"), "外卖任务默认只有一个接单人，不需要提供maxNumOfPeopleTake");

            // 外卖可以改的
            Optional.ofNullable(task.getStr("takeoutId"))
                    .map(takeoutId -> Validator.validateNumber(takeoutId, "外卖ID不合法"));

            Optional.ofNullable(task.getStr("orderTime"))
                    .map(orderTime -> Validator.validateMatchRegex(DATE_TIME_REGEX, orderTime, "下单时间不合法"));

            Optional.ofNullable(task.getStr("arrivalTime"))
                    .map(arrivalTime -> Validator.validateMatchRegex(DATE_TIME_REGEX, arrivalTime, "到达时间不合法"));

            Optional.ofNullable(task.getStr("arrivalLocation"))
                    // 必须在releaseTime之前
                    .map(arrivalLocation -> Validator.validateMatchRegex(".{1,255}", arrivalLocation, "到达地点长度必须在1-255之间"));

            Optional.ofNullable(task.getStr("targetLocation"))
                    .map(targetLocation -> Validator.validateMatchRegex(".{1,255}", targetLocation, "目标地点长度必须在1-255之间"));
        }
    }

    // userId是发布者的userId，taskId是任务的taskId
    public static void delete(Long taskId, Long userId) {
        Optional.ofNullable(taskId)
                .map(id -> CustomValidator.validateBetween("任务ID", id, 1, Long.MAX_VALUE))
                .map(id -> BeanUtils.getBean(TaskMapper.class).exists(new LambdaQueryWrapper<Task>().eq(Task::getTaskId, id)))
                .map(exists -> Validator.validateTrue(exists, "任务ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供任务ID"));

        Optional.ofNullable(userId)
                .map(id -> CustomValidator.validateBetween("用户ID", id, 1, Long.MAX_VALUE))
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "用户ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户ID"));

        final Long role = BeanUtils.getBean(UserRoleMapper.class).selectOne(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId)).getRoleId();
        if (role == Role.USER) {
            Validator.validateTrue(BeanUtils.getBean(TaskMapper.class).exists(new LambdaQueryWrapper<Task>().eq(Task::getTaskId, taskId).eq(Task::getUserId, userId)), "用户" + userId + "不是任务" + taskId + "的发布者");
        } else {
            Validator.validateTrue(BeanUtils.getBean(TaskMapper.class).exists(new LambdaQueryWrapper<Task>().eq(Task::getTaskId, taskId)), "任务" + taskId + "不存在");
        }
    }

    // userId是接单者的userId，taskId是任务ID
    public static void deleteTaskByTaker(Long taskId, Long userId) {
        Optional.ofNullable(taskId)
                .map(id -> CustomValidator.validateBetween("任务ID", id, 1, Long.MAX_VALUE))
                .map(id -> BeanUtils.getBean(TaskMapper.class).exists(new LambdaQueryWrapper<Task>().eq(Task::getTaskId, id)))
                .map(exists -> Validator.validateTrue(exists, "任务ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供任务ID"));

        Optional.ofNullable(userId)
                .map(id -> CustomValidator.validateBetween("用户ID", id, 1, Long.MAX_VALUE))
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "用户ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户ID"));

        Validator.validateTrue(BeanUtils.getBean(UsertaketaskMapper.class).exists(new LambdaQueryWrapper<Usertaketask>().eq(Usertaketask::getTaskId, taskId).eq(Usertaketask::getUserId, userId)), "用户" + userId + "没有接任务" + taskId);
        Validator.validateTrue(BeanUtils.getBean(TaskMapper.class).selectById(taskId).getTaskState() != Task.COMPLETED, "任务" + taskId + "已经完成，无法撤销");
    }

    public static void take(Long taskId, Long userId) {
        Optional.ofNullable(taskId)
                .map(Object::toString)
                .map(id -> Validator.validateNumber(id, "任务ID不合法"))
                .map(id -> CustomValidator.validateBetween("任务ID", id, 1, Long.MAX_VALUE))
                .map(id -> BeanUtils.getBean(TaskMapper.class).exists(new LambdaQueryWrapper<Task>().eq(Task::getTaskId, id)))
                .map(exists -> Validator.validateTrue(exists, "任务ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供任务ID"));

        Optional.ofNullable(userId)
                .map(Object::toString)
                .map(id -> Validator.validateNumber(id, "用户ID不合法"))
                .map(id -> CustomValidator.validateBetween("用户ID", id, 1, Long.MAX_VALUE))
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "用户ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户ID"));

        Validator.validateFalse(BeanUtils.getBean(UsertaketaskMapper.class).exists(new LambdaQueryWrapper<Usertaketask>().eq(Usertaketask::getTaskId, taskId).eq(Usertaketask::getUserId, userId)), "用户" + userId + "已经接过任务" + taskId);
        Validator.validateTrue(BeanUtils.getBean(TaskMapper.class).selectById(taskId).getTaskState() != Task.COMPLETED, "任务" + taskId + "已经完成，无法接取");
    }

    public static void complete(Long taskId, Long userId, Integer score) {
        Optional.ofNullable(taskId)
                .map(id -> CustomValidator.validateBetween("任务ID", id, 1, Long.MAX_VALUE))
                .map(id -> BeanUtils.getBean(TaskMapper.class).exists(new LambdaQueryWrapper<Task>().eq(Task::getTaskId, id)))
                .map(exists -> Validator.validateTrue(exists, "任务ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供任务ID"));

        Optional.ofNullable(userId)
                .map(id -> CustomValidator.validateBetween("用户ID", id, 1, Long.MAX_VALUE))
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "用户ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户ID"));

        Optional.ofNullable(score)
                .map(s -> CustomValidator.validateBetween("评分", s, 1, 5))
                .orElseThrow(() -> new ValidateException("必须提供评分"));

        Validator.validateTrue(BeanUtils.getBean(TaskMapper.class).exists(new LambdaQueryWrapper<Task>().eq(Task::getTaskId, taskId).eq(Task::getUserId, userId)), "用户" + userId + "没有发布任务" + taskId);
        Validator.validateTrue(BeanUtils.getBean(TaskMapper.class).selectById(taskId).getTaskState() != Task.COMPLETED, "任务" + taskId + "已经完成，无法重复完成");
    }
}