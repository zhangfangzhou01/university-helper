package com.yhm.universityhelper.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhm.universityhelper.entity.po.Task;

import java.util.List;

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

    boolean delete(Long taskId);

    LambdaQueryWrapper<Task> searchWrapper(JSONObject json);

    List<OrderItem> sortWrapper(JSONObject json);

    Page<Task> pageWrapper(JSONObject json);

    Page<Task> select(JSONObject json);
}
