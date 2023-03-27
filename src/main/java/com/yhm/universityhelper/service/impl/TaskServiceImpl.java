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
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.dao.UsertaketaskMapper;
import com.yhm.universityhelper.dao.wrapper.TaskQueryWrapper;
import com.yhm.universityhelper.entity.po.Task;
import com.yhm.universityhelper.entity.po.TaskTags;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.entity.po.Usertaketask;
import com.yhm.universityhelper.service.TaskService;
import com.yhm.universityhelper.util.BeanUtils;
import com.yhm.universityhelper.util.JsonUtils;
import com.yhm.universityhelper.util.ReflectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private UsertaketaskMapper usertaketaskMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BeanUtils beanUtils;

    public boolean update(JSONObject json) {
        final Object taskIdObj = json.get("taskId");
        final Object userIdObj = json.get("userId");
        if (ObjectUtil.isEmpty(taskIdObj) || ObjectUtil.isEmpty(userIdObj)) {
            throw new RuntimeException("可能未提供任务id或用户id");
        }

        Long taskId = Long.valueOf(taskIdObj.toString());
        Long userId = Long.valueOf(userIdObj.toString());

        User user = userMapper.selectById(userId);
        if (ObjectUtil.isEmpty(user)) {
            throw new RuntimeException("用户不存在");
        }

        Task task = taskMapper.selectById(taskId);
        for (String key : json.keySet()) {
            if (key.equals("taskId") || key.equals("userId")) {
                continue;
            } else if (key.equals("type")) {
                throw new RuntimeException("任务类型不可变更，由系统自动生成");
            }

            if (StringUtils.containsIgnoreCase(key, "time")) {
                String time = json.get(key).toString().replace(" ", "T");
                ReflectUtils.set(task, key, LocalDateTime.parse(time));
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
        return taskMapper.updateById(task) > 0;
    }

    public boolean insert(JSONObject json) {
        final Object userIdObj = json.get("userId");
        if (ObjectUtil.isEmpty(userIdObj)) {
            throw new RuntimeException("可能未提供用户id或任务类型");
        }

        Long userId = Long.valueOf(userIdObj.toString());

        User user = userMapper.selectById(userId);
        if (ObjectUtil.isEmpty(user)) {
            throw new RuntimeException("用户不存在");
        }

        Task task = new Task();
        for (String key : json.keySet()) {
            if (key.equals("taskId") || key.equals("type")) {
                throw new RuntimeException("任务id和任务类型不可变更，由系统自动生成");
            }

            Object value = json.get(key);
            if (StringUtils.containsIgnoreCase(key, "time")) {
                String time = value.toString().replace(" ", "T");
                ReflectUtils.set(task, key, LocalDateTime.parse(time));
            } else if ("userId".equals(key)) {
                ReflectUtils.set(task, "userId", userId);
            } else if ("tags".equals(key)) {
                JSONArray tags = json.getJSONArray(key);
                ReflectUtils.set(task, key, JsonUtils.jsonArrayToJson(tags));
                for (Object tag : tags) {
                    final TaskTags taskTags = new TaskTags((String)tag);
                    if (!taskTagsMapper.exists(new LambdaUpdateWrapper<TaskTags>().eq(TaskTags::getTag, taskTags.getTag()))) {
                        taskTagsMapper.insert(taskTags);
                    }
                }
            } else {
                ReflectUtils.set(task, key, value);
            }
        }

        return taskMapper.insert(task) > 0;
    }

    @Override
    public boolean delete(Long taskId) {
        boolean result = taskMapper.deleteById(taskId) > 0;
        usertaketaskMapper.delete(new LambdaUpdateWrapper<Usertaketask>().eq(Usertaketask::getTaskId, taskId));
        return result;
    }

    @Override
    public LambdaQueryWrapper<Task> searchWrapper(JSONObject json) {
        TaskQueryWrapper taskQueryWrapper = beanUtils.getBean(TaskQueryWrapper.class);

        if (ObjectUtil.isEmpty(json) || json.isEmpty()) {
            return taskQueryWrapper.getWrapper();
        }

        final Set<String> keys = json.keySet();

        for (String key : keys) {
            Object value = json.get(key);
            if ("userRelease".equals(key) || "userTake".equals(key) || StringUtils.containsIgnoreCase(key, "id")) {
                ReflectUtils.call(taskQueryWrapper, key, TaskQueryWrapper.class, Long.valueOf(value.toString()));
            } else if (StringUtils.containsIgnoreCase(key, "time")) {
                String time = value.toString().replace(" ", "T");
                ReflectUtils.call(taskQueryWrapper, key, TaskQueryWrapper.class, LocalDateTime.parse(time));
            } else if ("tags".equals(key)) {
                JSONArray tags = json.getJSONArray(key);
                ReflectUtils.call(taskQueryWrapper, key, TaskQueryWrapper.class, tags);
            } else {
                ReflectUtils.call(taskQueryWrapper, key, TaskQueryWrapper.class, value);
            }
        }

        return taskQueryWrapper.getWrapper();
    }


    @Override
    public List<OrderItem> sortWrapper(JSONObject sortJson) {
        List<OrderItem> orderItems = new ArrayList<>();

        if (ObjectUtil.isEmpty(sortJson) || sortJson.isEmpty()) {
            return orderItems;
        }

        for (String key : sortJson.keySet()) {
            String value = sortJson.get(key, String.class);
            if (StringUtils.isEmpty(value) || ((!"asc".equalsIgnoreCase(value)) && (!"desc".equalsIgnoreCase(value)))) {
                continue;
            }
            OrderItem orderItem = new OrderItem(key, "asc".equals(value));
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    @Override
    public Page<Task> pageWrapper(JSONObject json) {
        Long current = 1L;
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
        final JSONObject sortJson = json.get("sort", JSONObject.class);
        final String sortType = json.get("sortType", String.class);

        LambdaQueryWrapper<Task> wrapper = searchWrapper(searchJson);
        List<OrderItem> orderItems = sortWrapper(sortJson);
        Page<Task> page = pageWrapper(pageJson);

        if ("attribute".equals(sortType)) {
            page.setOrders(orderItems);
            return taskMapper.selectPage(page, wrapper);
        } else if ("priority".equals(sortType)) {
            List<Task> list = taskMapper.selectList(wrapper);

            list.sort((o1, o2) -> o2.getPriority().compareTo(o1.getPriority()));

            int start, end;
            if (page.getSize() > 0) {
                start = (int)((page.getCurrent() - 1) * page.getSize());
                end = Math.min((int)(start + page.getSize()), list.size());
            } else if (page.getSize() < 0) {
                start = 0;
                end = list.size();
            } else {
                return taskMapper.selectPage(new Page<>(0, 0), null);
            }

            page.setRecords(new ArrayList<>());
            page.setTotal(list.size());
            if (page.getSize() * (page.getCurrent() - 1) <= list.size()) {
                page.setRecords(list.subList(start, end));
            }
            return page;
        }

        return taskMapper.selectPage(new Page<>(0, 0), null);
    }
}
