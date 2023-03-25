package com.yhm.universityhelper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yhm.universityhelper.entity.po.Task;

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
    boolean update(String json);

    boolean insert(String json);

    Map<String, Object> select(String json);

    Map<String, Object> sort(String json);
}
