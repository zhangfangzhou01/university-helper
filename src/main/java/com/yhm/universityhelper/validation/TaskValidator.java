package com.yhm.universityhelper.validation;

import cn.hutool.core.util.ObjectUtil;
import com.yhm.universityhelper.entity.po.Task;

public class TaskValidator {
    public static void updateValidator(Task task) {
        String type = task.getType();
        if (ObjectUtil.isEmpty(type)) {
            throw new RuntimeException("任务类型不能为空");
        }

    }
}
