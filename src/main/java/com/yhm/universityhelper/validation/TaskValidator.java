package com.yhm.universityhelper.validation;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yhm.universityhelper.dao.TaskMapper;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.entity.po.Task;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.util.BeanUtils;

import java.time.LocalDateTime;
import java.util.Optional;

public class TaskValidator extends com.yhm.universityhelper.validation.Validator {
    public static void validateUpdate(JSONObject task) {
        Optional.ofNullable(task.getStr("taskId"))
                .map(taskId -> Validator.validateNumber(taskId, "任务ID不合法"))
                .map(taskId -> TaskValidator.validateBetween("任务ID", taskId, 1, Long.MAX_VALUE))
                .map(taskId -> BeanUtils.getBean(TaskMapper.class).exists(new LambdaQueryWrapper<Task>().eq(Task::getTaskId, taskId)))
                .map(exists -> Validator.validateTrue(exists, "任务ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供任务ID"));

        Optional.ofNullable(task.getStr("userId"))
                .map(userId -> Validator.validateNumber(userId, "用户ID不合法"))
                .map(taskId -> TaskValidator.validateBetween("用户ID", taskId, 1, Long.MAX_VALUE))
                .map(userId -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, userId)))
                .map(exists -> Validator.validateTrue(exists, "用户ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户ID"));

        Optional.ofNullable(BeanUtils.getBean(TaskMapper.class).selectOne(
                new LambdaQueryWrapper<Task>()
                        .eq(Task::getTaskId, task.getLong("taskId"))
                        .eq(Task::getUserId, task.getLong("userId"))
        )).orElseThrow(() -> new ValidateException("任务库内不存在该任务"));

        Optional.ofNullable(task.getStr("type"))
                .map(type -> Validator.validateNull(type, "禁止修改任务类型"));

        Optional.ofNullable(task.getStr("tags"))
                .map(tags -> Validator.validateMatchRegex(JSON_ARRAY_REGEX, tags, "标签格式不合法"));

        Optional.ofNullable(task.getStr("priority"))
                .map(priority -> Validator.validateNull(priority, "禁止修改优先级"));

        Optional.ofNullable(task.getStr("releaseTime"))
                .map(releaseTime -> Validator.validateNull(releaseTime, "禁止修改发布时间"));

        Optional.ofNullable(task.getStr("title"))
                .map(title -> TaskValidator.validateBetween("title", title, 1, 30));

        Optional.ofNullable(task.getStr("requireDescription"))
                .map(requireDescription -> TaskValidator.validateBetween("requireDescription", requireDescription, 0, 255));

        Optional.ofNullable(task.getStr("maxNumOfPeopleTake"))
                .map(maxNumOfPeopleTake -> Validator.validateNumber(maxNumOfPeopleTake, "最大接单人数不合法"));

        Optional.ofNullable(task.getStr("score"))
                .map(score -> Validator.validateNull(score, "禁止修改积分"));

        Optional.ofNullable(task.getStr("taskState"))
                .map(taskState -> Validator.validateNull(taskState, "禁止修改任务状态"));

        Optional.ofNullable(task.getStr("takeoutId"))
                .map(takeoutId -> Validator.validateNumber(takeoutId, "外卖ID不合法"))
                .map(takeoutId -> TaskValidator.validateBetween("takeoutId", takeoutId, 0, Integer.MAX_VALUE));

        Optional.ofNullable(task.getStr("orderTime"))
                .map(orderTime -> Validator.validateMatchRegex(DATE_TIME_REGEX, orderTime, "下单时间格式不合法"));

        Optional.ofNullable(task.getStr("arrivalTime"))
                .map(arrivalTime -> Validator.validateMatchRegex(DATE_TIME_REGEX, arrivalTime, "到达时间格式不合法"));

        Optional.ofNullable(task.getStr("arrivalLocation"))
                .map(arrivalLocation -> TaskValidator.validateBetween("arrivalLocation", arrivalLocation, 0, 255));

        Optional.ofNullable(task.getStr("targetLocation"))
                .map(targetLocation -> TaskValidator.validateBetween("targetLocation", targetLocation, 0, 255));

        Optional.ofNullable(task.getStr("distance"))
                .map(distance -> Validator.validateNull(distance, "距离不合法"));

        Optional.ofNullable(task.getStr("phoneNumForNow"))
                .map(phoneNumForNow -> Validator.validateNull(phoneNumForNow, "禁止修改此单用户对应的手机号"));

        Optional.ofNullable(task.getStr("transactionAmount"))
                .map(transactionAmount -> Validator.validateNumber(transactionAmount, "交易金额不合法"))
                .map(transactionAmount -> TaskValidator.validateBetween("transactionAmount", transactionAmount, 0, Double.MAX_VALUE));

        Optional.ofNullable(task.getStr("expectedPeriod"))
                .map(expectedPeriod -> Validator.validateNumber(expectedPeriod, "期望时长不合法"))
                .map(expectedPeriod -> TaskValidator.validateBetween("expectedPeriod", expectedPeriod, 0, Integer.MAX_VALUE));

        Optional.ofNullable(task.getStr("isHunter"))
                .map(isHunter -> Validator.validateNull(isHunter, "禁止修改是否为猎人任务"));

        Optional.ofNullable(task.getStr("leftNumOfPeopleTake"))
                .map(leftNumOfPeopleTake -> Validator.validateNull(leftNumOfPeopleTake, "禁止修改剩余接单人数"));
    }

    public static void validateSelect(JSONObject task) {
        Optional.ofNullable(task.getStr("sortType"))
                .map(sortType -> Validator.validateMatchRegex("(attribute|priority)", sortType, "排序类型不合法"))
                .orElseThrow(() -> new ValidateException("必须提供排序类型"));

        Optional.ofNullable(task.getJSONObject("page"))
                .map(page -> {
                    Optional.ofNullable(page.getStr("current"))
                            .map(current -> Validator.validateNumber(current, "当前页数不合法"))
                            .map(current -> TaskValidator.validateBetween("当前页数", current, 1, Integer.MAX_VALUE))
                            .orElseThrow(() -> new ValidateException("必须提供当前页数"));
                    Optional.ofNullable(page.getStr("size"))
                            .map(size -> Validator.validateNumber(size, "每页数量不合法"))
                            .map(size -> TaskValidator.validateBetween("每页数量", size, 1, Integer.MAX_VALUE))
                            .orElseThrow(() -> new ValidateException("必须提供每页数量"));
                    return page;
                }).orElseThrow(() -> new ValidateException("必须提供分页信息"));

        Optional.ofNullable(task.getJSONObject("search"))
                .map(search -> {
                    Optional.ofNullable(search.getStr("taskId"))
                            .map(taskId -> Validator.validateNumber(taskId, "任务ID不合法"))
                            .map(taskId -> TaskValidator.validateBetween("任务ID", taskId, 1, Long.MAX_VALUE))
                            .map(taskId -> BeanUtils.getBean(TaskMapper.class).exists(new LambdaQueryWrapper<Task>().eq(Task::getTaskId, taskId)))
                            .map(exists -> Validator.validateTrue(exists, "任务ID不存在"));

                    Optional.ofNullable(search.getStr("userId"))
                            .map(userId -> Validator.validateNumber(userId, "用户ID不合法"))
                            .map(userId -> TaskValidator.validateBetween("用户ID", userId, 1, Long.MAX_VALUE))
                            .map(userId -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, userId)))
                            .map(exists -> Validator.validateTrue(exists, "用户ID不存在"));

                    Optional.ofNullable(search.getStr("type"))
                            .map(type -> Validator.validateMatchRegex("(外卖|交易)", type, "任务类型不合法"));

                    Optional.ofNullable(search.getStr("tags"))
                            .map(tags -> Validator.validateMatchRegex(JSON_OBJECT_REGEX, tags, "标签不合法"));

                    Optional.ofNullable(search.getStr("releaseTimeMin"))
                            .map(releaseTimeMin -> Validator.validateMatchRegex(DATE_TIME_REGEX, releaseTimeMin, "发布时间不合法"));

                    Optional.ofNullable(search.getStr("releaseTimeMax"))
                            .map(releaseTimeMax -> Validator.validateMatchRegex(DATE_TIME_REGEX, releaseTimeMax, "发布时间不合法"));

                    Optional.ofNullable(search.getStr("taskState"))
                            .map(taskState -> Validator.validateMatchRegex("(0|1|2)", taskState, "任务状态不合法"));

                    Optional.ofNullable(search.getStr("arrivalTimeMin"))
                            .map(arrivalTimeMin -> Validator.validateMatchRegex(DATE_TIME_REGEX, arrivalTimeMin, "到达时间不合法"));

                    Optional.ofNullable(search.getStr("arrivalTimeMax"))
                            .map(arrivalTimeMax -> Validator.validateMatchRegex(DATE_TIME_REGEX, arrivalTimeMax, "到达时间不合法"));

                    Optional.ofNullable(search.getStr("arrivalLocation"))
                            .map(arrivalLocation -> TaskValidator.validateBetween("arrivalLocation", arrivalLocation, 0, 255));

                    Optional.ofNullable(search.getStr("targetLocation"))
                            .map(targetLocation -> TaskValidator.validateBetween("targetLocation", targetLocation, 0, 255));

                    Optional.ofNullable(search.getStr("transactionAmountMin"))
                            .map(transactionAmountMin -> Validator.validateNumber(transactionAmountMin, "交易金额不合法"))
                            .map(transactionAmountMin -> TaskValidator.validateBetween("交易金额", transactionAmountMin, 0, Double.MAX_VALUE));

                    Optional.ofNullable(search.getStr("transactionAmountMax"))
                            .map(transactionAmountMax -> Validator.validateNumber(transactionAmountMax, "交易金额不合法"))
                            .map(transactionAmountMax -> TaskValidator.validateBetween("交易金额", transactionAmountMax, 0, Double.MAX_VALUE));

                    Optional.ofNullable(search.getStr("transactionAmount"))
                            .map(transactionAmount -> Validator.validateNumber(transactionAmount, "交易金额不合法"))
                            .map(transactionAmount -> TaskValidator.validateBetween("交易金额", transactionAmount, 0, Double.MAX_VALUE));

                    Optional.ofNullable(search.getStr("isHunter"))
                            .map(isHunter -> Validator.validateMatchRegex("(0|1)", isHunter, "是否猎人不合法"));

                    Optional.ofNullable(search.getStr("expectedPeriod"))
                            .map(expectedPeriod -> Validator.validateNumber(expectedPeriod, "期望时长不合法"))
                            .map(expectedPeriod -> TaskValidator.validateBetween("期望时长", expectedPeriod, 0, Integer.MAX_VALUE));

                    Optional.ofNullable(search.getStr("distance"))
                            .map(distance -> Validator.validateNumber(distance, "距离不合法"))
                            .map(distance -> TaskValidator.validateBetween("距离", distance, 0, Integer.MAX_VALUE));

                    Optional.ofNullable(search.getStr("phoneNumForNow"))
                            .map(phoneNumForNow -> Validator.validateMobile(phoneNumForNow, "手机号不合法"));

                    Optional.ofNullable(search.getStr("transactionAmount"))
                            .map(transactionAmount -> Validator.validateNumber(transactionAmount, "交易金额不合法"))
                            .map(transactionAmount -> TaskValidator.validateBetween("交易金额", transactionAmount, 0, Double.MAX_VALUE));

                    Optional.ofNullable(search.getStr("priority"))
                            .map(priority -> Validator.validateNumber(priority, "优先级不合法"))
                            .map(priority -> TaskValidator.validateBetween("优先级", priority, 0, Integer.MAX_VALUE));

                    Optional.ofNullable(search.getStr("title"))
                            .map(title -> TaskValidator.validateBetween("标题", title, 0, 255));

                    Optional.ofNullable(search.getStr("requireDescription"))
                            .map(requireDescription -> TaskValidator.validateBetween("需求描述", requireDescription, 0, 255));

                    Optional.ofNullable(search.getStr("takeoutId"))
                            .map(takeoutId -> Validator.validateNumber(takeoutId, "外卖ID不合法"))
                            .map(takeoutId -> TaskValidator.validateBetween("外卖ID", takeoutId, 1, Long.MAX_VALUE));

                    Optional.ofNullable(search.getStr("maxNumOfPeopleTake"))
                            .map(maxNumOfPeopleTake -> Validator.validateNumber(maxNumOfPeopleTake, "最大接单人数不合法"))
                            .map(maxNumOfPeopleTake -> TaskValidator.validateBetween("最大接单人数", maxNumOfPeopleTake, 0, Integer.MAX_VALUE));

                    Optional.ofNullable(search.getStr("score"))
                            .map(score -> Validator.validateNumber(score, "评分不合法"))
                            .map(score -> TaskValidator.validateBetween("评分", score, 0, Integer.MAX_VALUE));

                    return search;
                }).orElseThrow(() -> new ValidateException("必须提供查询条件"));

        Optional.ofNullable(task.getJSONArray("sort"))
                .map(sort -> {
                    for (int i = 0; i < sort.size(); i++) {
                        JSONObject sortItem = sort.getJSONObject(i);
                        Assert.isFalse(sortItem.isEmpty(), "排序字段不能为空");
                        Assert.isTrue(sortItem.containsKey("column"), "排序字段缺少column");
                        Assert.isTrue(sortItem.containsKey("order"), "排序字段缺少order");
                        Assert.isTrue(sortItem.containsKey("asc"), "排序字段缺少asc");
                        Optional.ofNullable(sortItem.getStr("field"))
                                .map(field -> Validator.validateMatchRegex("(taskId|userId|releaseTime|taskState|arrivalTime|arrivalLocation|targetLocation|transactionAmount|isHunter|expectedPeriod|distance|phoneNumForNow|priority|title|requireDescription|takeoutId|maxNumOfPeopleTake|score)", field, "排序字段不合法"));
                    }
                    return sort;
                }).orElseThrow(() -> new ValidateException("必须提供排序条件"));
    }

    public static void validateDelete(Long taskId) {
        Optional.ofNullable(taskId)
                .map(Object::toString)
                .map(id -> Validator.validateNumber(id, "任务ID不合法"))
                .map(id -> TaskValidator.validateBetween("任务ID", id, 1, Long.MAX_VALUE))
                .map(id -> BeanUtils.getBean(TaskMapper.class).exists(new LambdaQueryWrapper<Task>().eq(Task::getTaskId, id)))
                .map(exists -> Validator.validateTrue(exists, "任务ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供任务ID"));
    }

    public static void validateInsert(JSONObject task) {
        Optional.ofNullable(task.getStr("userId"))
                .map(userId -> Validator.validateNumber(userId, "用户ID不合法"))
                .map(userId -> TaskValidator.validateBetween("用户ID", userId, 1, Long.MAX_VALUE))
                .map(userId -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, userId)))
                .map(exists -> Validator.validateTrue(exists, "用户ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户ID"));

        Optional.ofNullable(task.getStr("type"))
                .map(type -> Validator.validateMatchRegex("(外卖|交易)", type, "任务类型不合法"));

        Optional.ofNullable(task.getStr("tags"))
                .map(tags -> Validator.validateMatchRegex(JSON_ARRAY_REGEX, tags, "标签格式不合法"));

        Optional.ofNullable(task.getStr("priority"))
                .map(priority -> Validator.validateNumber(priority, "优先级不合法"))
                .map(priority -> TaskValidator.validateBetween("优先级", priority, 0, 2));

        Optional.ofNullable(task.getStr("releaseTime"))
                .map(releaseTime -> Validator.validateMatchRegex(DATE_TIME_REGEX, releaseTime, "发布时间格式不合法"));

        Optional.ofNullable(task.getStr("title"))
                .map(title -> TaskValidator.validateBetween("title", title, 1, 30));

        Optional.ofNullable(task.getStr("requireDescription"))
                .map(requireDescription -> TaskValidator.validateBetween("requireDescription", requireDescription, 0, 255));

        Optional.ofNullable(task.getStr("maxNumOfPeopleTake"))
                .map(maxNumOfPeopleTake -> Validator.validateNumber(maxNumOfPeopleTake, "最大接单人数不合法"));

        Optional.ofNullable(task.getStr("score"))
                .map(score -> Validator.validateNumber(score, "积分不合法"))
                .map(score -> TaskValidator.validateBetween("积分", score, 0, Integer.MAX_VALUE));

        Optional.ofNullable(task.getStr("taskState"))
                .map(taskState -> Validator.validateNumber(taskState, "任务状态不合法"))
                .map(taskState -> TaskValidator.validateBetween("任务状态", taskState, 0, 2));

        Optional.ofNullable(task.getStr("takeoutId"))
                .map(takeoutId -> Validator.validateNumber(takeoutId, "外卖ID不合法"))
                .map(takeoutId -> TaskValidator.validateBetween("takeoutId", takeoutId, 0, Integer.MAX_VALUE));

        Optional.ofNullable(task.getStr("orderTime"))
                .map(orderTime -> Validator.validateMatchRegex(DATE_TIME_REGEX, orderTime, "下单时间格式不合法"))
                .map(orderTime -> Validator.validateTrue(LocalDateTime.parse(orderTime.replace(" ", "T")).isBefore(LocalDateTime.parse(task.getStr("releaseTime").replace(" ", "T"))), "下单时间不合法"));

        Optional.ofNullable(task.getStr("arrivalTime"))
                .map(arrivalTime -> Validator.validateMatchRegex(DATE_TIME_REGEX, arrivalTime, "到达时间格式不合法"))
                .map(arrivalTime -> Validator.validateTrue(LocalDateTime.parse(arrivalTime.replace(" ", "T")).isAfter(LocalDateTime.parse(task.getStr("releaseTime").replace(" ", "T"))), "到达时间不合法"));

        Optional.ofNullable(task.getStr("arrivalLocation"))
                .map(arrivalLocation -> TaskValidator.validateBetween("arrivalLocation", arrivalLocation, 0, 255));

        Optional.ofNullable(task.getStr("targetLocation"))
                .map(targetLocation -> TaskValidator.validateBetween("targetLocation", targetLocation, 0, 255));

        Optional.ofNullable(task.getStr("distance"))
                .map(distance -> Validator.validateNull(distance, "距离不合法"));

        Optional.ofNullable(task.getStr("phoneNumForNow"))
                .map(phoneNumForNow -> Validator.validateNumber(phoneNumForNow, "手机号不合法"))
                .map(phoneNumForNow -> TaskValidator.validateBetween("手机号", phoneNumForNow, 0, 11));

        Optional.ofNullable(task.getStr("transactionAmount"))
                .map(transactionAmount -> Validator.validateNumber(transactionAmount, "交易金额不合法"))
                .map(transactionAmount -> TaskValidator.validateBetween("transactionAmount", transactionAmount, 0, Double.MAX_VALUE));

        Optional.ofNullable(task.getStr("expectedPeriod"))
                .map(expectedPeriod -> Validator.validateNumber(expectedPeriod, "期望时长不合法"))
                .map(expectedPeriod -> TaskValidator.validateBetween("expectedPeriod", expectedPeriod, 0, Integer.MAX_VALUE));

        Optional.ofNullable(task.getStr("isHunter"))
                .map(isHunter -> Validator.validateNull(isHunter, "禁止修改是否为猎人任务"));

        Optional.ofNullable(task.getStr("leftNumOfPeopleTake"))
                .map(leftNumOfPeopleTake -> Validator.validateNull(leftNumOfPeopleTake, "禁止修改剩余接单人数"));
    }
}