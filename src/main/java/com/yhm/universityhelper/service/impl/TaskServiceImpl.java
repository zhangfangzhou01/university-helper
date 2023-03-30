package com.yhm.universityhelper.service.impl;

import cn.hutool.core.lang.Pair;
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
import com.yhm.universityhelper.dao.wrapper.TaskWrapper;
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
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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
    private TaskTagsMapper taskTagsMapper;

    @Autowired
    private UsertaketaskMapper usertaketaskMapper;

    @Autowired
    private UserMapper userMapper;

    public boolean update(JSONObject json) {
        Long taskId = json.getLong("taskId");
        Task task = taskMapper.selectById(taskId);

        // TODO: 前端要针对类型，对某些字段设置为不可修改
        for (String key : json.keySet()) {
            if (key.equals("taskId") || key.equals("userId")) {
                continue;
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
        boolean result = taskMapper.updateById(task) > 0;

        if (!result) {
            throw new RuntimeException("更新任务失败，事务回滚");
        }
        return true;
    }

    // TODO: 有一些系统自动算出来的变量，还没有写，比如releaseTime之类的
    public boolean insert(JSONObject json) {
        final Long userId = json.getLong("userId");
        Task task = new Task();
        for (String key : json.keySet()) {
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
        // 对title，requireDescription，targetLocation， arrivalLocation做全文索引


        // 初始生成时，剩余可接取人数等于最大可接取人数
        task.setLeftNumOfPeopleTake(task.getMaxNumOfPeopleTake());
        boolean result = taskMapper.insert(task) > 0;

        if (!result) {
            throw new RuntimeException("发布任务失败，事务回滚");
        }
        return true;
    }

    @Override
    public Pair<Boolean, List<String>> delete(Long taskId, Long userId) {
        // 任务发布者删除自己发布的任务
        boolean result = taskMapper.deleteById(taskId) > 0;
        // 级联删除任务接取表里的相关记录
        List<Long> userIds = usertaketaskMapper.selectList(new LambdaQueryWrapper<Usertaketask>().eq(Usertaketask::getTaskId, taskId))
                .stream()
                .map(Usertaketask::getUserId)
                .collect(Collectors.toList());
        if (userIds.isEmpty()) {
//            throw new RuntimeException("任务接取表中没有该任务的接取记录");
            return new Pair<>(true, new ArrayList<>());
        }

        List<String> usernames = userMapper.selectBatchIds(userIds)
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
        result &= usertaketaskMapper.delete(new LambdaUpdateWrapper<Usertaketask>().eq(Usertaketask::getTaskId, taskId)) > 0;

        if (!result) {
            throw new RuntimeException("删除任务失败，事务回滚");
        }
        return new Pair<>(true, usernames);
    }

    @Override
    public boolean deleteTaskByTaker(Long taskId, Long userId) {
        Task task = taskMapper.selectById(userId);
        // 撤销任务接取，任务剩余可接取人数+1， 任务状态可能改变
        task.setLeftNumOfPeopleTake(task.getLeftNumOfPeopleTake() + 1);
        if (task.getLeftNumOfPeopleTake().equals(task.getMaxNumOfPeopleTake())) {
            task.setTaskState(Task.NOT_TAKE);
        }
        boolean result = usertaketaskMapper
                .delete(new LambdaUpdateWrapper<Usertaketask>()
                        .eq(Usertaketask::getTaskId, taskId)
                        .eq(Usertaketask::getUserId, userId)) > 0
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
        boolean flag = true;
        for (Usertaketask usertaketask : allTake) {
            final User user = userMapper.selectById(usertaketask.getUserId());
            user.setScore(user.getScore() + score);
            flag &= userMapper.updateById(user) > 0;
            flag &= usertaketaskMapper.deleteById(usertaketask) > 0;
        }
        // 接取任务后，任务的剩余可接取人数字段无效, 任务变成了已完成
        Task task = taskMapper.selectById(taskId);
        task.setScore(score);
        task.setTaskState(Task.COMPLETED);
        boolean result = taskMapper.updateById(task) > 0 && flag;

        if (!result) {
            throw new RuntimeException("完成任务失败，事务回滚");
        }
        return true;
    }

    @Override
    public LambdaQueryWrapper<Task> searchWrapper(JSONObject json) {
        TaskWrapper taskWrapper = BeanUtils.getBean(TaskWrapper.class);

        if (ObjectUtil.isEmpty(json) || json.isEmpty()) {
            return taskWrapper.getWrapper();
        }

        final Set<String> keys = json.keySet();

        for (String key : keys) {
            Object value = json.get(key);
            if ("userRelease".equals(key) || "userTake".equals(key) || StringUtils.containsIgnoreCase(key, "id")) {
                ReflectUtils.call(taskWrapper, key, TaskWrapper.class, Long.valueOf(value.toString()));
            } else if (StringUtils.containsIgnoreCase(key, "time")) {
                String time = value.toString().replace(" ", "T");
                ReflectUtils.call(taskWrapper, key, TaskWrapper.class, LocalDateTime.parse(time));
            } else if ("tags".equals(key)) {
                JSONArray tags = json.getJSONArray(key);
                ReflectUtils.call(taskWrapper, key, TaskWrapper.class, tags);
            } else {
                ReflectUtils.call(taskWrapper, key, TaskWrapper.class, value);
            }
        }

        return taskWrapper.getWrapper();
    }

    @Override
    public List<OrderItem> sortWrapper(JSONArray sortJson) {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Pair<Integer, OrderItem>> orderItemPairs = new ArrayList<>();

        if (ObjectUtil.isEmpty(sortJson) || sortJson.isEmpty()) {
            return orderItems;
        }

        for (Object obj : sortJson) {
            JSONObject jsonObject = (JSONObject)obj;
            if (ObjectUtil.isEmpty(jsonObject) || jsonObject.isEmpty()) {
                continue;
            }

            Integer order = jsonObject.get("order", Integer.class);
            String column = jsonObject.get("column", String.class);
            Boolean asc = jsonObject.get("asc", Boolean.class);
            if (ObjectUtil.isEmpty(order) || ObjectUtil.isEmpty(column) || ObjectUtil.isEmpty(asc)) {
                continue;
            }

            if ("priority".equals(column)) {
                continue;
            }

            OrderItem orderItem = new OrderItem(column, asc);
            orderItemPairs.add(new Pair<>(order, orderItem));
        }

        orderItemPairs.sort(Comparator.comparing(Pair::getKey));
        orderItems = orderItemPairs.stream().map(Pair::getValue).collect(Collectors.toList());

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
        final JSONArray sortJson = json.get("sort", JSONArray.class);
        final String sortType = json.get("sortType", String.class);

        LambdaQueryWrapper<Task> wrapper = searchWrapper(searchJson);
        List<OrderItem> orderItems = sortWrapper(sortJson);
        Page<Task> page = pageWrapper(pageJson);

        if (ObjectUtil.isNotEmpty(searchJson)) {
            for (String column : TaskWrapper.FUZZY_QUERY_COLUMNS) {
                if (searchJson.containsKey(column)) {
                    page.addOrder(TaskWrapper.fuzzySearch(column, searchJson.getStr(column)));
                }
            }
        }

        if ("attribute".equals(sortType)) {
            page.addOrder(orderItems);
//            return taskMapper.selectPage(page, wrapper);
        } else if ("priority".equals(sortType)) {
//            List<Task> list = taskMapper.selectList(wrapper);

            Integer releaseTimeMax = Math.toIntExact(taskMapper.selectOne(new LambdaQueryWrapper<Task>().orderByDesc(Task::getReleaseTime).last("limit 1")).getReleaseTime().toEpochSecond(ZoneOffset.of("+8")));
            Integer releaseTimeMin = Math.toIntExact(taskMapper.selectOne(new LambdaQueryWrapper<Task>().orderByAsc(Task::getReleaseTime).last("limit 1")).getReleaseTime().toEpochSecond(ZoneOffset.of("+8")));
            Integer leftNumOfPeopleTakeMax = taskMapper.selectOne(new LambdaQueryWrapper<Task>().orderByDesc(Task::getLeftNumOfPeopleTake).last("limit 1")).getLeftNumOfPeopleTake();
            Integer leftNumOfPeopleTakeMin = taskMapper.selectOne(new LambdaQueryWrapper<Task>().orderByAsc(Task::getLeftNumOfPeopleTake).last("limit 1")).getLeftNumOfPeopleTake();
            Integer expectedPeriodMax = taskMapper.selectOne(new LambdaQueryWrapper<Task>().orderByDesc(Task::getExpectedPeriod).last("limit 1")).getExpectedPeriod();
            Integer expectedPeriodMin = taskMapper.selectOne(new LambdaQueryWrapper<Task>().orderByAsc(Task::getExpectedPeriod).last("limit 1")).getExpectedPeriod();
            Integer arrivalTimeMax = Math.toIntExact(taskMapper.selectOne(new LambdaQueryWrapper<Task>().eq(Task::getType, "外卖").orderByDesc(Task::getArrivalTime).last("limit 1")).getArrivalTime().toEpochSecond(ZoneOffset.of("+8")));
            Integer arrivalTimeMin = Math.toIntExact(taskMapper.selectOne(new LambdaQueryWrapper<Task>().eq(Task::getType, "外卖").orderByAsc(Task::getArrivalTime).last("limit 1")).getArrivalTime().toEpochSecond(ZoneOffset.of("+8")));
            Integer transactionAmountMax = (taskMapper.selectOne(new LambdaQueryWrapper<Task>().eq(Task::getType, "交易").orderByDesc(Task::getTransactionAmount).last("limit 1")).getTransactionAmount()).intValue();
            Integer transactionAmountMin = (taskMapper.selectOne(new LambdaQueryWrapper<Task>().eq(Task::getType, "交易").orderByAsc(Task::getTransactionAmount).last("limit 1")).getTransactionAmount()).intValue();

            page.addOrder(TaskWrapper.prioritySort(releaseTimeMax, releaseTimeMin, leftNumOfPeopleTakeMax, leftNumOfPeopleTakeMin, expectedPeriodMax, expectedPeriodMin, arrivalTimeMax, arrivalTimeMin, transactionAmountMax, transactionAmountMin));

//            return taskMapper.selectPage(page, wrapper);
//            list.forEach(task -> {
//                task.autoSetPriority(
//                        releaseTimeMax,
//                        releaseTimeMin,
//                        leftNumOfPeopleTakeMax,
//                        leftNumOfPeopleTakeMin,
//                        expectedPeriodMax,
//                        expectedPeriodMin,
//                        arrivalTimeMax,
//                        arrivalTimeMin,
//                        transactionAmountMax,
//                        transactionAmountMin
//                );
//            });
//
//            list.sort(Task::compareTo);
//
//            int start, end;
//            if (page.getSize() > 0) {
//                start = (int)((page.getCurrent() - 1) * page.getSize());
//                end = Math.min((int)(start + page.getSize()), list.size());
//            } else if (page.getSize() < 0) {
//                start = 0;
//                end = list.size();
//            } else {
//                return taskMapper.selectPage(new Page<>(0, 0), null);
//            }
//
//            page.setRecords(new ArrayList<>());
//            page.setTotal(list.size());
//            if (page.getSize() * (page.getCurrent() - 1) <= list.size()) {
//                page.setRecords(list.subList(start, end));
//            }
//            return page;
        }

        return taskMapper.selectPage(page, wrapper);
    }

    @Override
    public Page<Task> selectYourTake(JSONObject json) {
        final Long userId = json.getLong("userId");
        final JSONObject pageJson = json.getJSONObject("page");
        Page<Task> page = pageWrapper(pageJson);
        LambdaQueryWrapper<Task> wrapper = BeanUtils.getBean(TaskWrapper.class).userTake(userId).getWrapper();
        return taskMapper.selectPage(page, wrapper);
    }

    @Override
    public Page<Task> selectYourPublish(JSONObject json) {
        final Long userId = json.getLong("userId");
        final JSONObject pageJson = json.getJSONObject("page");
        Page<Task> page = pageWrapper(pageJson);
        LambdaQueryWrapper<Task> wrapper = BeanUtils.getBean(TaskWrapper.class).userRelease(userId).getWrapper();

        return taskMapper.selectPage(page, wrapper);
    }
}
