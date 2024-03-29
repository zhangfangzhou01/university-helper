package com.yhm.universityhelper.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    // 参数超过6个用json方便传
    Long update(JSONObject json);

    Long insert(JSONObject json);

    /**
     * @param taskId 用于任务发布者删除自己发布的任务，并且任务接取表会级联删除相关记录
     * @return
     */
    List<String> delete(Long taskId);

    /**
     * @param taskId // 用于接取了该任务的用户删除该任务
     * @return
     */
    boolean deleteTaskByTaker(Long taskId);

    /**
     * @param taskId
     * @param userId 用户接取任务
     * @return
     */
    boolean take(Long taskId, Long userId);

    /**
     * @param taskId
     * @param userId 任务发布者可以决定任务是否完成
     * @return
     */
    boolean complete(Long taskId, Long userId, Integer score);

    QueryWrapper<Task> searchWrapper(JSONObject json);

    List<OrderItem> sortWrapper(JSONArray json);

    Page<Task> pageWrapper(JSONObject json);

    Page<Task> select(JSONObject json);
    
    Long selectTaskCount(Long userId);

    /**
     * @param json 获取用户接取的所有任务
     * @return
     */
    Page<Task> selectYourTake(JSONObject json);

    /**
     * @param json 获取用户发布的所有任务
     * @return
     */
    Page<Task> selectYourPublish(JSONObject json);

    List<String> selectAllTaskTags();
}
