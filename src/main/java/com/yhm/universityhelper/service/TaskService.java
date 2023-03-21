package com.yhm.universityhelper.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhm.universityhelper.entity.po.Task;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yhm
 * @since 2023-03-04
 */

@Transactional
public interface TaskService extends IService<Task> {
    boolean update(JSONObject json);

    boolean insert(JSONObject json);

    Map<String, Object> select(JSONObject json);

    Map<String, Object> sort(JSONObject json);
}
