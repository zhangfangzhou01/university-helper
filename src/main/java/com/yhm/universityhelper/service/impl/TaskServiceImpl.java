package com.yhm.universityhelper.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhm.universityhelper.dao.TaskMapper;
import com.yhm.universityhelper.dao.TaskTagMapper;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.dao.UsertaketaskMapper;
import com.yhm.universityhelper.dao.wrapper.CustomTaskWrapper;
import com.yhm.universityhelper.entity.po.Task;
import com.yhm.universityhelper.entity.po.TaskTag;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.entity.po.Usertaketask;
import com.yhm.universityhelper.service.TaskService;
import com.yhm.universityhelper.util.BeanUtils;
import com.yhm.universityhelper.util.ReflectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private TaskTagMapper taskTagMapper;

    @Autowired
    private UsertaketaskMapper usertaketaskMapper;

    @Autowired
    private UserMapper userMapper;

    private static final HashMap<String, String> ATTRIBUTE_AND_CLASSNAME = new HashMap<>() {{
        this.put("taskId", "Long");
        this.put("userId", "Long");
        this.put("type", "String");
        this.put("tags", "JSONArray");
        this.put("releaseTime", "LocalDateTime");
        this.put("title", "String");
        this.put("requireDescription", "String");
        this.put("maxNumOfPeopleTake", "Integer");
        this.put("leftNumOfPeopleTake", "Integer");
        this.put("expectedPeriod", "Integer");
        this.put("score", "Integer");
        this.put("taskState", "Integer");
        this.put("takeoutId", "Integer");
        this.put("orderTime", "LocalDateTime");
        this.put("arrivalTime", "LocalDateTime");
        this.put("arrivalLocation", "String");
        this.put("targetLocation", "String");
        this.put("distance", "Integer");
        this.put("phoneNumForNow", "String");
        this.put("transactionAmount", "Double");
        this.put("isHunter", "Integer");
    }};

    private static void setTask(JSONObject json, Task task, TaskTagMapper taskTagMapper) {
        for (String key : json.keySet()) {
            switch (ATTRIBUTE_AND_CLASSNAME.get(key)) {
                case "Long" -> ReflectUtils.set(task, key, Long.parseLong(json.getStr(key)));
                case "String" -> ReflectUtils.set(task, key, json.getStr(key));
                case "JSONArray" -> {
                    JSONArray tags = json.getJSONArray(key);
                    ReflectUtils.set(task, key, tags);
                    Thread.startVirtualThread(() -> {
                        if (!tags.isEmpty()) {
                            taskTagMapper.insertBatch(tags);
                        }
                    });
                }
                case "LocalDateTime" -> {
                    String time = json.get(key).toString().replace(' ', 'T');
                    ReflectUtils.set(task, key, LocalDateTime.parse(time));
                }
                case "Integer" -> ReflectUtils.set(task, key, Integer.parseInt(json.getStr(key)));
                case "Double" -> ReflectUtils.set(task, key, Double.parseDouble(json.getStr(key)));
                default -> ReflectUtils.set(task, key, json.get(key));
            }
        }
    }

    @Override
    public Long insert(JSONObject json) {
        final Long userId = json.getLong("userId");
        final String type = json.getStr("type");
        Task task = new Task();
        setTask(json, task, taskTagMapper);
        // 初始生成时，剩余可接取人数等于最大可接取人数
        task.setReleaseTime(LocalDateTime.now());
        task.setPhoneNumForNow(userMapper.selectById(userId).getPhone());
        task.setLeftNumOfPeopleTake(task.getMaxNumOfPeopleTake());
        boolean result = taskMapper.insert(task) > 0;

        if (!result) {
            throw new RuntimeException("发布任务失败，事务回滚");
        }
        return task.getTaskId();
    }

    @Override
    public Long update(JSONObject json) {
        Long taskId = json.getLong("taskId");
        Task task = taskMapper.selectById(taskId);

        // TODO: 前端要针对类型，对某些字段设置为不可修改
        setTask(json, task, taskTagMapper);
        boolean result = taskMapper.updateById(task) > 0;

        if (!result) {
            throw new RuntimeException("更新任务失败，事务回滚");
        }
        return task.getTaskId();
    }

    @Override
    public List<String> delete(Long taskId) {
        // 任务发布者删除自己发布的任务
        boolean result = taskMapper.deleteById(taskId) > 0;
        // 级联删除任务接取表里的相关记录
        List<Long> userIds = usertaketaskMapper.selectList(new LambdaQueryWrapper<Usertaketask>().eq(Usertaketask::getTaskId, taskId))
                .stream()
                .map(Usertaketask::getUserId)
                .collect(Collectors.toList());
        if (userIds.isEmpty()) {
//            throw new RuntimeException("任务接取表中没有该任务的接取记录");
            return Collections.emptyList();
        }

        List<String> usernames = userMapper.selectBatchIds(userIds)
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
        result &= usertaketaskMapper.delete(new LambdaUpdateWrapper<Usertaketask>().eq(Usertaketask::getTaskId, taskId)) > 0;

        if (!result) {
            throw new RuntimeException("删除任务失败，事务回滚");
        }
        return usernames;
    }

    @Override
    public boolean deleteTaskByTaker(Long taskId) {
        Task task = taskMapper.selectById(taskId);
        // 撤销任务接取，任务剩余可接取人数+1， 任务状态可能改变
        task.setLeftNumOfPeopleTake(task.getLeftNumOfPeopleTake() + 1);
        if (task.getLeftNumOfPeopleTake().equals(task.getMaxNumOfPeopleTake())) {
            task.setTaskState(Task.NOT_TAKE);
        }
        boolean result = usertaketaskMapper
                .delete(new LambdaUpdateWrapper<Usertaketask>()
                        .eq(Usertaketask::getTaskId, taskId)) > 0
                &&
                taskMapper.updateById(task) > 0;

        if (!result) {
            throw new RuntimeException("撤销任务接取失败，事务回滚");
        }
        return true;
    }

    @Override
    public boolean take(Long taskId, Long userId) {
        // 任务接取表添加记录
        Usertaketask usertaketask = new Usertaketask(taskId, userId);

        // 接取任务后，任务的剩余可接取人数-1, 任务变成了已接取状态
        Task task = taskMapper.selectById(taskId);
        task.setLeftNumOfPeopleTake(task.getLeftNumOfPeopleTake() - 1);
        task.setTaskState(Task.TAKE);

        if (task.getLeftNumOfPeopleTake() < 0) {
            throw new RuntimeException("任务剩余可接取人数不足");
        }

        boolean result = usertaketaskMapper.insert(usertaketask) > 0 && taskMapper.updateById(task) > 0;

        if (!result) {
            throw new RuntimeException("接取任务失败，事务回滚");
        }
        return true;
    }

    @Override
    public boolean complete(Long taskId, Long userId, Integer score) {
        // 删除usertaketask 相关的接取记录
        List<Usertaketask> allTake = usertaketaskMapper.selectList(new LambdaQueryWrapper<Usertaketask>().eq(Usertaketask::getTaskId, taskId));
        boolean result = true;
        for (Usertaketask usertaketask : allTake) {
            final User user = userMapper.selectById(usertaketask.getUserId());
            user.setScore(user.getScore() + score);
            result &= userMapper.updateById(user) > 0;
            result &= usertaketaskMapper.deleteById(usertaketask) > 0;
        }
        // 接取任务后，任务的剩余可接取人数字段无效, 任务变成了已完成
        Task task = taskMapper.selectById(taskId);
        task.setScore(score);
        task.setTaskState(Task.COMPLETED);
        result &= taskMapper.updateById(task) > 0;

        if (!result) {
            throw new RuntimeException("完成任务失败，事务回滚");
        }
        return true;
    }

    @Override
    public QueryWrapper<Task> searchWrapper(JSONObject json) {
        CustomTaskWrapper customTaskWrapper = BeanUtils.getBean(CustomTaskWrapper.class);

        if (ObjectUtil.isEmpty(json) || json.isEmpty()) {
            return customTaskWrapper.getQueryWrapper();
        }

        final Set<String> keys = json.keySet();
        for (String key : keys) {
            Object value = json.get(key);
            if ("userRelease".equals(key) || "userTake".equals(key) || StringUtils.containsIgnoreCase(key, "id")) {
                ReflectUtils.call(customTaskWrapper, key, Long.valueOf(value.toString()));
            } else if (StringUtils.containsIgnoreCase(key, "time")) {
                String time = value.toString().replace(' ', 'T');
                ReflectUtils.call(customTaskWrapper, key, LocalDateTime.parse(time));
            } else if ("tags".equals(key)) {
                JSONArray tags = json.getJSONArray(key);
                ReflectUtils.call(customTaskWrapper, key, tags);
            } else {
                ReflectUtils.call(customTaskWrapper, key, value);
            }
        }

        return customTaskWrapper.getQueryWrapper();
    }

    @Override
    public List<OrderItem> sortWrapper(JSONArray sortJson) {
        List<OrderItem> orderItems = new ArrayList<>();
        if (ObjectUtil.isEmpty(sortJson) || sortJson.isEmpty()) {
            return orderItems;
        }

        for (Object obj : sortJson) {
            JSONObject jsonObject = (JSONObject) obj;
            if (ObjectUtil.isEmpty(jsonObject) || jsonObject.isEmpty()) {
                continue;
            }

            String column = jsonObject.get("column", String.class);
            Boolean asc = jsonObject.get("asc", Boolean.class);
            if (ObjectUtil.isEmpty(column) || ObjectUtil.isEmpty(asc)) {
                throw new RuntimeException("排序参数缺失，需要提供column和asc参数");
            }

            OrderItem orderItem = new OrderItem(column, asc);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    @Override
    public Page<Task> pageWrapper(JSONObject json) {
        Long current = 1L;
        Long size = -1L;

        if (ObjectUtil.isNotEmpty(json) && (!json.isEmpty())) {
            Validator.validateNotNull(json.get("current"), "分页参数缺失，需要提供current参数");
            Validator.validateNotNull(json.get("size"), "分页参数缺失，需要提供size参数");

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
        final String sortType = json.get("sortType", String.class);

        QueryWrapper<Task> wrapper = searchWrapper(searchJson);
        Page<Task> page = pageWrapper(pageJson);

        if (ObjectUtil.isNotNull(sortJson) && (!sortJson.isEmpty())) {
            Validator.validateNotNull(sortType, "排序类型不能为空");
            Validator.validateMatchRegex("^(attribute|priority)$", sortType, "排序类型不正确，只能是attribute或者priority");
        }

        if ("attribute".equals(sortType)) {
            page.addOrder(sortWrapper(sortJson));
        } else if ("priority".equals(sortType)) {
            page.addOrder(CustomTaskWrapper.prioritySort());
        }

        return taskMapper.selectPage(page, wrapper);
    }

    @Override
    public Long selectTaskCount(Long userId) {
        return taskMapper.selectCount(new LambdaQueryWrapper<Task>().eq(Task::getUserId, userId));
    }

    @Override
    public Page<Task> selectYourTake(JSONObject json) {
        final Long userId = json.getLong("userId");
        final JSONObject pageJson = json.getJSONObject("page");
        Page<Task> page = pageWrapper(pageJson);
        LambdaQueryWrapper<Task> wrapper = BeanUtils.getBean(CustomTaskWrapper.class).userTake(userId).getLambdaQueryWrapper();
        return taskMapper.selectPage(page, wrapper);
    }

    @Override
    public Page<Task> selectYourPublish(JSONObject json) {
        final Long userId = json.getLong("userId");
        final JSONObject pageJson = json.getJSONObject("page");
        Page<Task> page = pageWrapper(pageJson);
        LambdaQueryWrapper<Task> wrapper = BeanUtils.getBean(CustomTaskWrapper.class).userRelease(userId).getLambdaQueryWrapper();

        return taskMapper.selectPage(page, wrapper);
    }

    @Override
    public List<String> selectAllTaskTags() {
        return taskTagMapper.selectList(null).stream().map(TaskTag::getTag).collect(Collectors.toList());
    }
}
