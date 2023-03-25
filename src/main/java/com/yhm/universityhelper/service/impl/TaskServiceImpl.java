package com.yhm.universityhelper.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhm.universityhelper.dao.TaskMapper;
import com.yhm.universityhelper.dao.TaskTagsMapper;
import com.yhm.universityhelper.dao.wrapper.TaskQueryWrapper;
import com.yhm.universityhelper.entity.po.Task;
import com.yhm.universityhelper.entity.po.TaskTags;
import com.yhm.universityhelper.service.TaskService;
import com.yhm.universityhelper.util.BeanUtils;
import com.yhm.universityhelper.util.JsonUtils;
import com.yhm.universityhelper.util.ReflectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yhm
 * @since 2023-03-04
 */

@Transactional
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskTagsMapper taskTagsMapper;

    @Autowired
    private BeanUtils beanUtils;

    public boolean update(JSONObject json) {
        Long taskId = Long.valueOf(json.get("taskId").toString());
        Long userId = Long.valueOf(json.get("userId").toString());
        String type = (String)json.get("type");

        if (ObjectUtil.isEmpty(taskId) || ObjectUtil.isEmpty(type) || ObjectUtil.isEmpty(userId)) {
            return false;
        }
        Task task = taskMapper.selectById(taskId);
        for (String key : json.keySet()) {
            if (key.equals("taskId") || key.equals("userId") || key.equals("type")) {
                continue;
            }
            if (StringUtils.containsIgnoreCase(key, "time")) {
                String time = json.get(key).toString().replace(" ", "T");
                ReflectUtils.set(task, key, LocalDateTime.parse(time));
                continue;
            }
            ReflectUtils.set(task, key, json.get(key));
        }
        return taskMapper.updateById(task) > 0;
    }

    public boolean insert(JSONObject json) {
        Long userId = Long.valueOf(json.get("userId").toString());
        String type = (String)json.get("type");

        if (ObjectUtil.isEmpty(userId) || ObjectUtil.isEmpty(type)) {
            return false;
        }
        Task task = new Task();
        for (String key : json.keySet()) {
            if (key.equals("taskId") || key.equals("type")) {
                continue;
            }
            if (StringUtils.containsIgnoreCase(key, "time")) {
                String time = json.get(key).toString().replace(" ", "T");
                ReflectUtils.set(task, key, LocalDateTime.parse(time));
            } else if (key.equals("userId")) {
                ReflectUtils.set(task, "userId", userId);
            } else if (key.equals("tags")) {
                JSONArray tags = json.getJSONArray(key);
                ReflectUtils.set(task, key, JsonUtils.jsonArrayToJson(tags));
                for (Object tag : tags) {
                    final TaskTags taskTags = new TaskTags((String)tag);
                    if (!taskTagsMapper.exists(new LambdaUpdateWrapper<TaskTags>().eq(TaskTags::getTag, taskTags.getTag()))) {
                        taskTagsMapper.insert(taskTags);
                    }
                }
            } else {
                ReflectUtils.set(task, key, json.get(key));
            }
        }

        // 插入的时候 不需要计算 priority

        return taskMapper.insert(task) > 0;
    }

    @Override
    public boolean delete(Long taskId) {
        return taskMapper.deleteById(taskId) > 0;
    }

    @Override
    public LambdaQueryWrapper<Task> searchWrapper(JSONObject json) {
        TaskQueryWrapper taskQueryWrapper = beanUtils.getBean(TaskQueryWrapper.class);

        if (ObjectUtil.isEmpty(json) || json.isEmpty()) {
            return taskQueryWrapper.getWrapper();
        }

        final Set<String> keys = json.keySet();

        for (String key : keys) {
            if (StringUtils.equals(key, "userRelease") || StringUtils.equals(key, "userTake")) {
                ReflectUtils.call(taskQueryWrapper, key, Void.class, Long.valueOf(json.get(key).toString()));
            } else if (StringUtils.containsIgnoreCase(key, "time")) {
                String time = json.get(key).toString().replace(" ", "T");
                ReflectUtils.call(taskQueryWrapper, key, Void.class, LocalDateTime.parse(time));
            } else if (StringUtils.containsIgnoreCase(key, "date")) {
                String date = json.get(key).toString();
                ReflectUtils.call(taskQueryWrapper, key, Void.class, LocalDate.parse(date));
            } else if (StringUtils.equals(key, "tags")) {
                JSONArray tags = json.getJSONArray(key);
                ReflectUtils.call(taskQueryWrapper, key, Void.class, tags);
            } else {
                ReflectUtils.call(taskQueryWrapper, key, Void.class, json.get(key));
            }
        }

        return taskQueryWrapper.getWrapper();
    }

    @Override
    public List<OrderItem> sortWrapper(JSONArray sortJson) {
        List<OrderItem> orderItems = new ArrayList<>();

        if (ObjectUtil.isEmpty(sortJson) || sortJson.isEmpty()) {
            return orderItems;
        }

        for (JSONObject sort : sortJson.toList(JSONObject.class)) {
            String column = sort.get("column", String.class);
            Boolean asc = sort.get("asc", Boolean.class);

            if (ObjectUtil.isEmpty(column) || ObjectUtil.isEmpty(asc)) {
                continue;
            }

            OrderItem orderItem = new OrderItem(column, asc);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    @Override
    public Page<Task> pageWrapper(JSONObject json) {
        Long current = 0L;
        Long size = -1L;

        if (ObjectUtil.isNotEmpty(json) && (!json.isEmpty())) {
            current = json.get("current", Long.class);
            size = json.get("size", Long.class);
        }

        return new Page<>(current, size);
    }

    @Override
    public Page<Task> select(JSONObject json) {
        final JSONObject searchJson = json.get("search", JSONObject.class);
        final JSONObject pageJson = json.get("page", JSONObject.class);
        final JSONArray sortJson = json.get("sort", JSONArray.class);

        LambdaQueryWrapper<Task> wrapper = searchWrapper(searchJson);
        List<OrderItem> orderItems = sortWrapper(sortJson);
        Page<Task> page = pageWrapper(pageJson);
        page.addOrder(orderItems);

        taskMapper.selectPage(page, wrapper);

        return page;
    }

//    @Override
//    public List<Task> searchList(JSONObject json) {
//        return taskMapper.selectList(searchWrapper(json));
//    }
//
//    public List<Task> selectList(JSONObject json) {
//        final JSONObject searchJson = json.get("search", JSONObject.class);
//        final JSONArray sortJson = json.get("sort", JSONArray.class);
//
//        final List<Task> tasksResult = searchList(searchJson);
//
//        // todo 计算 priority
//
//        // todo 排序
//
//        return tasksResult;
//    }
}
