package com.yhm.universityhelper.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhm.universityhelper.dao.TaskMapper;
import com.yhm.universityhelper.entity.po.Task;
import com.yhm.universityhelper.service.TaskService;
import com.yhm.universityhelper.util.ReflectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yhm
 * @since 2023-03-04
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    @Autowired
    TaskMapper taskMapper;

    public boolean update(@NotNull JSONObject json) {

        Integer taskId = (Integer)json.get("taskId");
        Integer userId = (Integer)json.get("userId");
        String type = (String)json.get("type");

        if (ObjectUtil.isEmpty(taskId) || ObjectUtil.isEmpty(type) || ObjectUtil.isEmpty(userId)) {
            return false;
        }
        Task task = taskMapper.selectById(taskId);
        ReflectUtils.setBatchWithout(task, json, "taskId", "userId", "type", "releaseTime", "priority", "completeFlag", "phoneNumForNow");
        return taskMapper.updateById(task) > 0;
    }

    public boolean insert(@NotNull JSONObject json) {
        Integer userId = (Integer)json.get("userId");
        String type = (String)json.get("type");

        if (ObjectUtil.isEmpty(userId) || ObjectUtil.isEmpty(type)) {
            return false;
        }
        Task task = new Task();
        ReflectUtils.setBatchWithout(task, json, "taskId", "userId", "type");

        // todo 计算 priority

        return taskMapper.insert(task) > 0;
    }

    @Override
    public @NotNull Map<String, Object> select(@NotNull JSONObject json) {
        final Integer userId = (Integer)json.get("userId");
        final Set<String> keys = json.keySet();

        List<ArrayList<Task>> taskss = new ArrayList<>();
        List<Task> tasksResult;
        for (String key : keys) {
            switch (key) {
                case "userRelease":
                    taskss.add(taskMapper.selectByUserRelease(userId));
                case "userTake":
                    taskss.add(taskMapper.selectByUserTake(userId));
                case "type":
                    if ("全部".equals(json.get("type"))) {
                        taskss.add(taskMapper.selectAllType());
                    } else {
                        taskss.add(taskMapper.selectByType((String)json.get("type")));
                    }
                    // 这里都是开区间
                case "releaseTimeMax":
                    taskss.add(taskMapper.selectReleaseTimeMax((LocalDateTime)json.get("releaseTimeMax")));
                case "releaseTimeMin":
                    taskss.add(taskMapper.selectReleaseTimeMin((LocalDateTime)json.get("releaseTimeMin")));
                case "maxNumOfPeople":
                    taskss.add(taskMapper.selectByMaxNumOfPeople((Integer)json.get("maxNumOfPeople")));
                case "completeFlag":
                    taskss.add(taskMapper.selectByCompleteFlag((Integer)json.get("completeFlag")));
                case "arrivalTimeMax":
                    taskss.add(taskMapper.selectArrivalTimeMax((LocalDateTime)json.get("arrivalTimeMax")));
                case "arrivalTimeMin":
                    taskss.add(taskMapper.selectArrivalTimeMin((LocalDateTime)json.get("arrivalTimeMin")));
                case "arrivalLocation":
                    taskss.add(taskMapper.selectByArrivalLocation((String)json.get("arrivalLocation")));
                case "targetLocation":
                    taskss.add(taskMapper.selectByTargetLocation((String)json.get("targetLocation")));
                case "transactionTimeMax":
                    taskss.add(taskMapper.selectTransactionAmountMax((Integer)json.get("transactionAmountMax")));
                case "transactionTimeMin":
                    taskss.add(taskMapper.selectTransactionAmountMin((Integer)json.get("transactionAmountMin")));
            }
        }

        tasksResult = taskss.stream()
                .reduce((tasks1, tasks2) -> {
                    tasks1.retainAll(tasks2);
                    return tasks1;
                }).orElse(new ArrayList<>());

        // todo 计算 priority


        // todo 排序


        Map<String, Object> result = new HashMap<>();
        for (Task task : tasksResult) {
            result.put(task.getTaskId().toString(), task);
        }

        return result;
    }

    @Override
    public @Nullable Map<String, Object> sort(@NotNull JSONObject json) {
        final List<Map<String, String>> sortBy = (List<Map<String, String>>)json.get("sortBy");
        final Map<String, Object> tasks = (Map<String, Object>)json.get("tasks");

        List<Task> tasksResult = new ArrayList<>();
        for (String key : tasks.keySet()) {
            tasksResult.add((Task)tasks.get(key));
        }

        // sortBy是类似于 [{"userId": "asc"}, {"releaseTime": "desc"}] 的结构
        // 也就是说，先按照userId升序排列，再按照releaseTime降序排列
        // 这里的asc和desc是字符串，不是boolean
        // 这里的userId和releaseTime是Task类的属性名
        // 写一个方法，根据sortBy的结构，对tasksResult进行排序

        tasksResult.sort((task1, task2) -> {
            for (Map<String, String> map : sortBy) {
                for (String key : map.keySet()) {
//                    if (map.get(key).equals("asc")) {
//                    } else {
//                    }
                }
            }
            return 0;
        });
        return null;
    }
}
