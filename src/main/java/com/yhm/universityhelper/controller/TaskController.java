package com.yhm.universityhelper.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.yhm.universityhelper.config.RabbitConfig;
import com.yhm.universityhelper.dao.TaskMapper;
import com.yhm.universityhelper.entity.po.Task;
import com.yhm.universityhelper.entity.po.UserRole;
import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.service.ChatService;
import com.yhm.universityhelper.service.TaskService;
import com.yhm.universityhelper.validation.CustomValidator;
import com.yhm.universityhelper.validation.TaskValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "任务管理")
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * taskId    不可改
     * userId  任务创建者的 Id | 不可改
     * tags  目前核心 tag 有 “外卖” “交易” | 可改
     * priority 不可改
     * releaseTime 不可改
     * title 可改
     * requireDescription 可改
     * maxNumOfPeopleTake （）可改
     * completeFlag 不可改
     * takeoutId  订单号 | 可改
     * orderTime 可改
     * arrivalTime 可改
     * arrivalLocation 可改
     * targetLocation 可改
     * distance   外卖送到学校的地点 和 外卖接受者在学校的地点 的 距离
     * phoneNumForNow  任务创建时用户手机号，防止后续更改手机号的问题
     * transactionAmount 可改
     * expectedPeriod task 可以显示的天数，超出就不显示了，表示任务过期  | 可改
     * selectCode selectCode, 01字符串，逐个对应用以判断，各字段是否是被选择的（有效的） | 前端设定
     * // 注册或者修改任务信息
     *
     * @return ResponseResult
     */
    @ApiOperation(value = "修改任务信息", notes = "修改任务信息")
    @DynamicParameters(
            name = "TaskUpdateDto",
            properties = {
                    @DynamicParameter(name = "userId",
                            value = "用户Id",
                            required = true,
                            dataTypeClass = Integer.class,
                            example = "2345346364"
                    ),
                    @DynamicParameter(name = "title",
                            value = "任务标题",
                            required = true,
                            dataTypeClass = String.class,
                            example = "这个是外卖任务"
                    ),
                    @DynamicParameter(name = "requireDescription",
                            value = "任务描述",
                            required = true,
                            dataTypeClass = String.class,
                            example = "帮我拿外卖"
                    ),
                    @DynamicParameter(name = "orderTime",
                            value = "外卖订单时间",
                            required = true,
                            dataTypeClass = LocalDateTime.class,
                            example = "2020-12-12 12:12:12"
                    ),
                    @DynamicParameter(name = "arrivalTime",
                            value = "外卖送达时间",
                            required = true,
                            dataTypeClass = String.class,
                            example = "2020-12-12 12:12:12"
                    ),
                    @DynamicParameter(name = "arrivalLocation",
                            value = "外卖到校地点",
                            required = true,
                            dataTypeClass = String.class,
                            example = "东校区西门"
                    ),
                    @DynamicParameter(name = "targetLocation",
                            value = "外卖校内送达地点",
                            required = true,
                            dataTypeClass = String.class,
                            example = "东八426"
                    ),
                    @DynamicParameter(name = "transactionAmount",
                            value = "交易金额",
                            required = true,
                            dataTypeClass = Double.class,
                            example = "100.00"
                    )
            })
    @PostMapping("/update")
    public ResponseResult<Object> update(@RequestBody JSONObject json) {
        TaskValidator.update(json);
        CustomValidator.auth(json.getLong("userId"), UserRole.USER_CAN_CHANGE_SELF);
        final Long taskId = taskService.update(json);
        final Integer formerTaskState = taskMapper.selectTaskStateByTaskId(taskId);
        rabbitTemplate.convertAndSend(RabbitConfig.TASK_AUTO_DELETE_EXCHANGE_NAME, RabbitConfig.TASK_AUTO_DELETE_ROUTING_KEY, "{'taskId':" + taskId + ",'taskState':" + formerTaskState + ",'type':'" + json.getStr("type") + "','userId':" + json.getLong("userId") + "}");
        return ObjectUtils.isNotNull(taskId) && taskId > 0
                ? ResponseResult.ok("任务信息修改成功")
                : ResponseResult.fail("任务信息修改失败");
    }

    @ApiOperation(value = "创建任务信息", notes = "创建任务信息")
    @DynamicParameters(
            name = "TaskInsertDto",
            properties = {
                    @DynamicParameter(name = "userId",
                            value = "用户Id",
                            required = true,
                            dataTypeClass = Integer.class,
                            example = "1244435"
                    ),
                    @DynamicParameter(name = "type",
                            value = "任务核心类别",
                            required = true,
                            dataTypeClass = String.class,
                            example = "外卖"
                    ),
                    @DynamicParameter(name = "tags",
                            value = "任务自定义tag",
                            required = true,
                            dataTypeClass = String.class,
                            example = "辣椒, 红色"
                    ),
                    @DynamicParameter(name = "title",
                            value = "任务标题",
                            required = true,
                            dataTypeClass = String.class,
                            example = "这个是外卖任务"
                    ),
                    @DynamicParameter(name = "requireDescription",
                            value = "任务描述",
                            required = true,
                            dataTypeClass = String.class,
                            example = "帮我拿外卖"
                    ),
                    @DynamicParameter(name = "releaseTime",
                            value = "任务发布时间",
                            required = true,
                            dataTypeClass = LocalDateTime.class,
                            example = "2020-12-12 12:12:12"
                    ),
                    @DynamicParameter(name = "taskState",
                            value = "任务状态",
                            required = true,
                            dataTypeClass = Integer.class,
                            example = "0:任务未发布 1:已发布未领取"
                    ),
                    @DynamicParameter(name = "orderTime",
                            value = "外卖订单时间",
                            required = true,
                            dataTypeClass = LocalDateTime.class,
                            example = "2020-12-12 12:12:12"
                    ),
                    @DynamicParameter(name = "arrivalTime",
                            value = "外卖送达时间",
                            required = true,
                            dataTypeClass = LocalDateTime.class,
                            example = "2020-12-12 12:12:12"
                    ),
                    @DynamicParameter(name = "arrivalLocation",
                            value = "外卖到校地点",
                            required = true,
                            dataTypeClass = String.class,
                            example = "东校区西门"
                    ),
                    @DynamicParameter(name = "targetLocation",
                            value = "外卖校内送达地点",
                            required = true,
                            dataTypeClass = String.class,
                            example = "东八426"
                    ),
                    @DynamicParameter(name = "transactionAmount",
                            value = "交易金额",
                            required = true,
                            dataTypeClass = Double.class,
                            example = "100.00"
                    ),
                    @DynamicParameter(name = "isHunter",
                            value = "任务双向类型",
                            required = true,
                            dataTypeClass = Integer.class,
                            example = "0: 雇主 1: 猎人"
                    ),
            })
    @PostMapping("/insert")
    public ResponseResult<Object> insert(@RequestBody JSONObject json) {
        TaskValidator.insert(json);
        CustomValidator.auth(json.getLong("userId"), UserRole.USER_CAN_CHANGE_SELF);
        final Long taskId = taskService.insert(json);
        final Integer formerTaskState = taskMapper.selectTaskStateByTaskId(taskId);
        rabbitTemplate.convertAndSend(RabbitConfig.TASK_AUTO_DELETE_EXCHANGE_NAME, RabbitConfig.TASK_AUTO_DELETE_ROUTING_KEY, "{'taskId':" + taskId + ",'taskState':" + formerTaskState + ",'type':'" + json.getStr("type") + "','userId':" + json.getLong("userId") + "}");
        return ObjectUtils.isNotNull(taskId) && taskId > 0
                ? ResponseResult.ok("任务信息创建成功")
                : ResponseResult.fail("任务信息创建失败");
    }

    /**
     * @param json 此json 里设定了选择标准
     *             "taskId":
     *             "userId":
     *             "sex":
     *             "type":
     *             "releaseTimeMax":
     *             "releaseTimeMin":
     *             "taskState":
     *             "arrivalTimeMax":
     *             "arrivalTimeMin":
     *             "arrivalLocation":
     *             "targetLocation":
     *             "transactionMax":
     *             "transactionMin":
     */
    @ApiOperation(value = "获取任务信息", notes = "获取任务信息")
    @DynamicParameters(
            name = "TaskSelectDto",
            properties = {
                    @DynamicParameter(
                            name = "search",
                            value = "搜索",
                            required = true,
                            dataTypeClass = List.class,
//                            public CustomTaskWrapper userRelease(Long userId) {
//                            queryWrapper.eq("userId", userId);
//            return this;
//            }
//
//            public CustomTaskWrapper userTake(Long userId) {
//            final List<Long> taskIds = usertaketaskMapper.selectList(new LambdaQueryWrapper<Usertaketask>().eq(Usertaketask::getUserId, userId)).stream().map(Usertaketask::getTaskId).collect(Collectors.toList());
//
//            if (taskIds.isEmpty()) {
//            queryWrapper.eq("taskId", -1);
//} else {
//        queryWrapper.in("taskId", taskIds);
//        }
//        return this;
//        }
//
//public CustomTaskWrapper type(String type) {
//        queryWrapper.eq("type", type);
//        return this;
//        }
//
//public CustomTaskWrapper releaseTimeMax(LocalDateTime releaseTimeMax) {
//        queryWrapper.le("releaseTime", releaseTimeMax);
//        return this;
//        }
//
//public CustomTaskWrapper releaseTimeMin(LocalDateTime releaseTimeMin) {
//        queryWrapper.ge("releaseTime", releaseTimeMin);
//        return this;
//        }
//
//public CustomTaskWrapper releaseTime(LocalDateTime releaseTime) {
//        queryWrapper.eq("releaseTime", releaseTime);
//        return this;
//        }
//
//public CustomTaskWrapper releaseDate(LocalDate releaseTime) {
//        queryWrapper.ge("releaseTime", releaseTime.atStartOfDay()).le("releaseTime", releaseTime.plusDays(1).atStartOfDay());
//        return this;
//        }
//
//public CustomTaskWrapper maxNumOfPeopleTake(Integer maxNumOfPeopleTake) {
//        queryWrapper.le("maxNumOfPeopleTake", maxNumOfPeopleTake);
//        return this;
//        }
//
//public CustomTaskWrapper taskState(Integer taskState) {
//        queryWrapper.eq("taskState", taskState);
//        return this;
//        }
//
//public CustomTaskWrapper arrivalTimeMax(LocalDateTime arrivalTimeMax) {
//        queryWrapper.le("arrivalTime", arrivalTimeMax);
//        return this;
//        }
//
//public CustomTaskWrapper arrivalTimeMin(LocalDateTime arrivalTimeMin) {
//        queryWrapper.ge("arrivalTime", arrivalTimeMin);
//        return this;
//        }
//
//public CustomTaskWrapper arrivalLocation(String arrivalLocation) {
//        queryWrapper.apply("(@arrivalLocationMatchingDegree := " + fuzzyQuery("arrivalLocation", arrivalLocation) + ")").apply("@arrivalLocationMatchingDegree > 0");/*.orderByDesc("@arrivalLocationMatchingDegree");*/
//        return this;
//        }
//
//public CustomTaskWrapper targetLocation(String targetLocation) {
//        queryWrapper.apply("(@targetLocationMatchingDegree := " + fuzzyQuery("targetLocation", targetLocation) + ")").apply("@targetLocationMatchingDegree > 0");/*.orderByDesc("@targetLocationMatchingDegree");*/
//        return this;
//        }
//
//public CustomTaskWrapper transactionAmountMax(Integer transactionAmountMax) {
//        queryWrapper.le("transactionAmount", transactionAmountMax);
//        return this;
//        }
//
//public CustomTaskWrapper transactionAmountMin(Integer transactionAmountMin) {
//        queryWrapper.ge("transactionAmount", transactionAmountMin);
//        return this;
//        }
//
//public CustomTaskWrapper taskId(Long taskId) {
//        queryWrapper.eq("taskId", taskId);
//        return this;
//        }
//
//public CustomTaskWrapper userId(Long userId) {
//        queryWrapper.eq("userId", userId);
//        return this;
//        }
//
//public CustomTaskWrapper tags(JSONArray tags) {
//        for (Object tag : tags) {
//        queryWrapper.like("tags", tag.toString());
//        }
//        return this;
//        }
//
//public CustomTaskWrapper title(String title) {
//        queryWrapper.apply("(@titleMatchingDegree := " + fuzzyQuery("title", title) + ")").apply("@titleMatchingDegree > 0");/*.orderByDesc("@titleMatchingDegree");*/
//        return this;
//        }
//
//public CustomTaskWrapper requireDescription(String requireDescription) {
//        queryWrapper.apply("(@requireDescriptionMatchingDegree := " + fuzzyQuery("requireDescription", requireDescription) + ")").apply("@requireDescriptionMatchingDegree > 0");/*.orderByDesc("@requireDescriptionMatchingDegree");*/
//        return this;
//        }
//
//public CustomTaskWrapper score(Integer score) {
//        queryWrapper.eq("score", score);
//        return this;
//        }
//
//
//public CustomTaskWrapper takeoutId(Integer takeoutId) {
//        queryWrapper.eq("takeoutId", takeoutId);
//        return this;
//        }
//
//public CustomTaskWrapper orderTime(LocalDateTime orderTime) {
//        queryWrapper.eq("orderTime", orderTime);
//        return this;
//        }
//
//public CustomTaskWrapper arrivalTime(LocalDateTime arrivalTime) {
//        queryWrapper.eq("arrivalTime", arrivalTime);
//        return this;
//        }
//
//public CustomTaskWrapper distance(Integer distance) {
//        queryWrapper.eq("distance", distance);
//        return this;
//        }
//
//public CustomTaskWrapper phoneNumForNow(Integer phoneNumForNow) {
//        queryWrapper.eq("phoneNumForNow", phoneNumForNow);
//        return this;
//        }
//
//public CustomTaskWrapper transactionAmount(Double transactionAmount) {
//        queryWrapper.eq("transactionAmount", transactionAmount);
//        return this;
//        }
//
//public CustomTaskWrapper expectedPeriod(Integer expectedPeriod) {
//        queryWrapper.eq("expectedPeriod", expectedPeriod);
//        return this;
//        }
//
//public CustomTaskWrapper isHunter(Integer isHunter) {
//        queryWrapper.eq("isHunter", isHunter);
//        return this;
//        }
//
//public CustomTaskWrapper leftNumOfPeopleTake(Integer leftNumOfPeopleTake) {
//        queryWrapper.eq("leftNumOfPeopleTake", leftNumOfPeopleTake);
//        return this;
//        }
                            example = "{\n" +
                                    "    \"taskId\": 1234321432,\n" +
                                    "    \"userId\": 123344,\n" +
                                    "    \"type\": \"外卖\",\n" +
                                    "    \"tags\": \"辣椒, 红色\",\n" +
                                    "    \"releaseTimeMin\": \"2020-12-12 12:12:12\",\n" +
                                    "    \"releaseTimeMax\": \"2020-12-12 12:12:12\",\n" +
                                    "    \"taskState\": \"0:未发布 1:已发布未领取 2:已领取未完成 3:已完成\",\n" +
                                    "    \"arrivalTimeMin\": \"2020-12-12 12:12:12\",\n" +
                                    "    \"arrivalTimeMax\": \"2020-12-12 12:12:12\",\n" +
                                    "    \"arrivalLocation\": \"东校区西门\",\n" +
                                    "    \"targetLocation\": \"东八426\",\n" +
                                    "    \"transactionAmountMin\": 100.00,\n" +
                                    "    \"transactionAmountMax\": 100.00,\n" +
                                    "    \"isHunter\": \"0: 雇主 1: 猎人\"\n" +
                                    "}"
                    ),
                    @DynamicParameter(
                            name = "sortType",
                            value = "排序方式(priority or attribute)",
                            required = true,
                            dataTypeClass = String.class,
                            example = "priority"
                    ),
                    @DynamicParameter(
                            name = "sort",
                            value = "排序",
                            required = true,
                            dataTypeClass = List.class,
                            example = "[\n" +
                                    "    {\n" +
                                    "        \"column\": \"taskId\",\n" +
                                    "        \"asc\": true\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "        \"column\": \"userId\",\n" +
                                    "        \"asc\": true\n" +
                                    "    }\n" +
                                    "]"
                    ),
                    @DynamicParameter(
                            name = "page",
                            value = "分页",
                            required = true,
                            dataTypeClass = List.class,
                            example = "{\n" +
                                    "    \"current\": 1,\n" +
                                    "    \"size\": 10\n" +
                                    "}"
                    )
            })
    @PostMapping("/select")
    public ResponseResult<Page<Task>> select(@RequestBody JSONObject json) {
        return ResponseResult.ok(taskService.select(json), "获取任务信息成功");
    }

    @ApiOperation(value = "删除任务信息", notes = "根据任务Id删除任务信息")
    @PostMapping("/delete")
    public ResponseResult<Object> delete(@RequestParam Long taskId, @RequestParam Long userId) {
        TaskValidator.delete(taskId);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        List<String> usernames = taskService.delete(taskId);
        Thread.startVirtualThread(() ->
                chatService.notificationByUsernames(usernames, "任务" + taskId + "已被任务发布者删除")
        );
        return ResponseResult.ok("删除任务信息成功");
    }

    // deleteTaskByTaker
    @ApiOperation(value = "接单者撤销接单", notes = "接单者撤销接单")
    @PostMapping("/deleteTaskByTaker")
    public ResponseResult<Object> deleteTaskByTaker(@RequestParam Long taskId, @RequestParam Long userId) {
        TaskValidator.deleteTaskByTaker(taskId, userId);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return taskService.deleteTaskByTaker(taskId)
                ? ResponseResult.ok("撤销接单成功")
                : ResponseResult.fail("撤销接单失败");
    }

    @ApiOperation(value = "用户接单", notes = "用户接单")
    @PostMapping("/take")
    public ResponseResult<Object> take(@RequestParam Long taskId, @RequestParam Long userId) {
        TaskValidator.take(taskId, userId);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return taskService.take(taskId, userId)
                ? ResponseResult.ok("接单成功")
                : ResponseResult.fail("接单失败");
    }

    @ApiOperation(value = "用户完成任务", notes = "用户完成任务")
    @PostMapping("/complete")
    public ResponseResult<Object> complete(@RequestParam Long taskId, @RequestParam Long userId, @RequestParam Integer score) {
        TaskValidator.complete(taskId, userId, score);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return taskService.complete(taskId, userId, score)
                ? ResponseResult.ok("完成任务成功")
                : ResponseResult.fail("完成任务失败");
    }

    @ApiOperation(value = "获取所有任务标签", notes = "获取所有任务标签")
    @PostMapping("/selectAllTaskTags")
    public ResponseResult<List<String>> selectAllTaskTags() {
        return ResponseResult.ok(taskService.selectAllTaskTags(), "获取所有任务标签成功");
    }
}
