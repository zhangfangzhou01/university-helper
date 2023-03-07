package com.yhm.universityhelper.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhm.universityhelper.entity.po.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yhm
 * @since 2023-03-04
 */
public interface TaskService extends IService<Task> {
    boolean update(JSONObject json);
    boolean insert(JSONObject json);
    @NotNull Map<String, Object> select(JSONObject json);
    @Nullable Map<String, Object> sort(JSONObject json);
}
