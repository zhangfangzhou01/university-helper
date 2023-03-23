package com.yhm.universityhelper.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhm.universityhelper.dao.TaskMapper;
import com.yhm.universityhelper.entity.po.Task;
import com.yhm.universityhelper.service.TaskService;
import com.yhm.universityhelper.util.JsonUtils;
import com.yhm.universityhelper.util.ReflectUtils;
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

    public boolean update(String json) {
        Map<String, Object> data = JsonUtils.jsonToMap(json);

        Integer taskId = (Integer)data.get("taskId");
        Integer userId = (Integer)data.get("userId");
        String type = (String)data.get("type");

        if (ObjectUtil.isEmpty(taskId) || ObjectUtil.isEmpty(type) || ObjectUtil.isEmpty(userId)) {
            return false;
        }
        Task task = taskMapper.selectById(taskId);
        for (String key : data.keySet()) {
            if (key.equals("taskId") || key.equals("userId") || key.equals("type")) {
                continue;
            }
            ReflectUtils.set(task, key, data.get(key));
        }
        return taskMapper.updateById(task) > 0;
    }

    public boolean insert(String json) {
        Map<String, Object> data = JsonUtils.jsonToMap(json);

        Integer userId = (Integer)data.get("userId");
        String type = (String)data.get("type");

        if (ObjectUtil.isEmpty(userId) || ObjectUtil.isEmpty(type)) {
            return false;
        }
        Task task = new Task();
        for (String key : data.keySet()) {
            if (key.equals("taskId") || key.equals("userId") || key.equals("type")) {
                continue;
            }
            ReflectUtils.set(task, key, data.get(key));
        }

        // 插入的时候 不需要计算 priority

        return taskMapper.insert(task) > 0;
    }

    public Map<String, Object> select(String json) {
        Map<String, Object> data = JsonUtils.jsonToMap(json);
        final Integer userId = (Integer)data.get("userId");
        final Set<String> keys = data.keySet();

        List<ArrayList<Task>> taskss = new ArrayList<>();
        List<Task> tasksResult;

        if(keys.contains("userRelease")) {
            taskss.add(taskMapper.selectByUserRelease(userId));
        }else if(keys.contains("userTake")){
            taskss.add(taskMapper.selectByUserTake(userId));
        }else if(keys.contains("type")){
            if ("全部".equals(data.get("type"))) {
                taskss.add(taskMapper.selectAllType());
            } else {
                taskss.add(taskMapper.selectByType((String)data.get("type")));
            }
        }else if(keys.contains("releaseTimeMax")){
            taskss.add(taskMapper.selectReleaseTimeMax((LocalDateTime)data.get("releaseTimeMax")));
        }else if(keys.contains("releaseTimeMin")){
            taskss.add(taskMapper.selectReleaseTimeMin((LocalDateTime)data.get("releaseTimeMin")));
        }else if(keys.contains("maxNumOfPeople")){
            taskss.add(taskMapper.selectByMaxNumOfPeople((Integer)data.get("maxNumOfPeople")));
        }else if(keys.contains("taskState")){
            taskss.add(taskMapper.selectByTaskState((Integer) data.get("taskState")));
        }else if(keys.contains("arrivalTimeMax")){
            taskss.add(taskMapper.selectArrivalTimeMax((LocalDateTime)data.get("arrivalTimeMax")));
        }else if(keys.contains("arrivalTimeMin")){
            taskss.add(taskMapper.selectArrivalTimeMin((LocalDateTime)data.get("arrivalTimeMin")));
        }else if(keys.contains("arrivalLocation")){
            taskss.add(taskMapper.selectByArrivalLocation((String)data.get("arrivalLocation")));
        }else if(keys.contains("targetLocation")){
            taskss.add(taskMapper.selectByTargetLocation((String)data.get("targetLocation")));
        }else if(keys.contains("transactionTimeMax")){
            taskss.add(taskMapper.selectTransactionAmountMax((Integer)data.get("transactionAmountMax")));
        }else if(keys.contains("transactionTimeMin")) {
            taskss.add(taskMapper.selectTransactionAmountMin((Integer) data.get("transactionAmountMin")));
        }

        // 做交集
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
    public Map<String, Object> sort(String json) {
        Map<String, Object> data = JsonUtils.jsonToMap(json);
        final List<Map<String, String>> sortBy = (List<Map<String, String>>)data.get("sortBy");
        final Map<String, Object> tasks = (Map<String, Object>)data.get("tasks");

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
