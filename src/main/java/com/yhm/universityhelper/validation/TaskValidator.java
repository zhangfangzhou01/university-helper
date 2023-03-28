package com.yhm.universityhelper.validation;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.util.BeanUtils;

import java.util.Optional;

public class TaskValidator extends com.yhm.universityhelper.validation.Validator {
    public static void validateUpdate(JSONObject task) {
        Optional.ofNullable(task.getLong("taskId"))
                .map(taskId -> Validator.validateNull(taskId, "禁止修改任务ID"));

        Optional.ofNullable(task.getStr("userId"))
                .map(userId -> Validator.validateNumber(userId, "用户ID不合法"))
                .map(userId -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, userId)))
                .map(exists -> Validator.validateTrue(exists, "用户ID不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户ID"));

        Optional.ofNullable(task.getStr("tags"))
                .map(tags -> Validator.validateMatchRegex(JSON_ARRAY_REGEX, tags, "标签格式不合法"));

        Optional.ofNullable(task.getStr("priority"))
                .map(priority -> Validator.validateNull(priority, "禁止修改优先级"));

        Optional.ofNullable(task.getStr("releaseTime"))
                .map(releaseTime -> Validator.validateMatchRegex(DATE_TIME_REGEX, releaseTime, "发布时间格式不合法"));


    }
}
