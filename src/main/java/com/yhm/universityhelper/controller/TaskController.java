package com.yhm.universityhelper.controller;

import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "任务管理")
@RestController
@RequestMapping("/task")
public class TaskController {
    final
    TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * taskId    不可改
     * userId  任务创建者的 id | 不可改
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
     * // 注册或者修改任务信息
     *
     * @return ResponseResult
     */
    @ApiOperation(value = "修改任务信息", notes = "修改任务信息")
    @PostMapping("/update")
    @DynamicParameters(
            name = "TaskUpdateDto",
            properties = {
                    @DynamicParameter(name = "taskId",
                            value = "任务 Id",
                            required = true,
                            dataTypeClass = Integer.class,
                            example = "1"),
                    @DynamicParameter(name = "userId",
                            value = "任务创建者的 Id",
                            required = true,
                            dataTypeClass = Integer.class,
                            example = "1"),
                    @DynamicParameter(name = "tags",
                            value = "目前核心 tag 有 “外卖” “交易”(可选)",
                            dataTypeClass = List.class,
                            example = "外卖"),
                    @DynamicParameter(name = "priority",
                            value = "任务优先级(可选)",
                            dataTypeClass = Integer.class,
                            example = "1"),
                    @DynamicParameter(name = "releaseTime",
                            value = "任务发布时间(可选)",
                            dataTypeClass = LocalDateTime.class,
                            example = "2020-12-12 12:12:12"),
                    @DynamicParameter(name = "title",
                            value = "任务标题(可选)",
                            dataTypeClass = String.class,
                            example = "任务标题"),
                    @DynamicParameter(name = "requireDescription",
                            value = "任务描述(可选)",
                            dataTypeClass = String.class,
                            example = "任务描述"),
                    @DynamicParameter(name = "maxNumOfPeopleTake",
                            value = "最大接单人数(可选)",
                            dataTypeClass = Integer.class,
                            example = "1"),
                    @DynamicParameter(name = "completeFlag",
                            value = "任务完成标志(可选)",
                            dataTypeClass = Integer.class,
                            example = "1"),
                    @DynamicParameter(name = "takeoutId",
                            value = "订单号(可选)",
                            dataTypeClass = Integer.class,
                            example = "1"),
                    @DynamicParameter(name = "orderTime",
                            value = "订单时间(可选)",
                            dataTypeClass = LocalDateTime.class,
                            example = "2020-12-12 12:12:12"),
                    @DynamicParameter(name = "arrivalTime",
                            value = "外卖送达时间(可选)",
                            dataTypeClass = LocalDateTime.class,
                            example = "2020-12-12 12:12:12"),
                    @DynamicParameter(name = "arrivalLocation",
                            value = "外卖送达地点(可选)",
                            dataTypeClass = String.class,
                            example = "外卖送达地点"),
                    @DynamicParameter(name = "targetLocation",
                            value = "外卖目标地点(可选)",
                            dataTypeClass = String.class,
                            example = "外卖目标地点"),
                    @DynamicParameter(name = "distance",
                            value = "外卖送到学校的地点 和 外卖接受者在学校的地点 的 距离(可选)",
                            dataTypeClass = Integer.class,
                            example = "1"),
                    @DynamicParameter(name = "phoneNumForNow",
                            value = "任务创建时用户手机号，防止后续更改手机号的问题(可选)",
                            dataTypeClass = String.class,
                            example = "12345678901"),
                    @DynamicParameter(name = "transactionAmount",
                            value = "交易金额(可选)",
                            dataTypeClass = Double.class,
                            example = "1.0"),
                    @DynamicParameter(name = "expectedPeriod",
                            value = "task 可以显示的天数，超出就不显示了，表示任务过期(可选)",
                            dataTypeClass = Integer.class,
                            example = "1")
            }
    )
    public @Nullable ResponseResult update(@RequestBody JSONObject json) {
        return taskService.update(json)
                ? ResponseResult.success("任务信息修改成功")
                : ResponseResult.failure("任务信息修改失败");
    }

    @ApiOperation(value = "创建任务信息", notes = "创建任务信息")
    @DynamicParameters(
            name = "TaskInsertDto",
            properties = {
                    @DynamicParameter(name = "userId",
                            value = "任务创建者的 Id",
                            required = true,
                            dataTypeClass = Integer.class,
                            example = "1"),
                    @DynamicParameter(name = "tags",
                            value = "目前核心 tag 有 “外卖” “交易”",
                            required = true,
                            dataTypeClass = List.class,
                            example = "外卖"),
                    @DynamicParameter(name = "priority",
                            value = "任务优先级",
                            required = true,
                            dataTypeClass = Integer.class,
                            example = "1"),
                    @DynamicParameter(name = "releaseTime",
                            value = "任务发布时间",
                            required = true,
                            dataTypeClass = LocalDateTime.class,
                            example = "2020-12-12 12:12:12"),
                    @DynamicParameter(name = "title",
                            value = "任务标题",
                            required = true,
                            dataTypeClass = String.class,
                            example = "任务标题"),
                    @DynamicParameter(name = "requireDescription",
                            value = "任务描述",
                            required = true,
                            dataTypeClass = String.class,
                            example = "任务描述"),
                    @DynamicParameter(name = "maxNumOfPeopleTake",
                            value = "最大接单人数",
                            required = true,
                            dataTypeClass = Integer.class,
                            example = "1"),
                    @DynamicParameter(name = "completeFlag",
                            value = "任务完成标志",
                            required = true,
                            dataTypeClass = Integer.class,
                            example = "1"),
                    @DynamicParameter(name = "takeoutId",
                            value = "订单号",
                            required = true,
                            dataTypeClass = Integer.class,
                            example = "1"),
                    @DynamicParameter(name = "orderTime",
                            value = "订单时间",
                            required = true,
                            dataTypeClass = LocalDateTime.class,
                            example = "2020-12-12 12:12:12"),
                    @DynamicParameter(name = "arrivalTime",
                            value = "外卖送达时间",
                            required = true,
                            dataTypeClass = LocalDateTime.class,
                            example = "2020-12-12 12:12:12"),
                    @DynamicParameter(name = "arrivalLocation",
                            value = "外卖送达地点",
                            required = true,
                            dataTypeClass = String.class,
                            example = "外卖送达地点"),
                    @DynamicParameter(name = "targetLocation",
                            value = "外卖目标地点",
                            required = true,
                            dataTypeClass = String.class,
                            example = "外卖目标地点"),
                    @DynamicParameter(name = "distance",
                            value = "外卖送到学校的地点 和 外卖接受者在学校的地点 的 距离",
                            required = true,
                            dataTypeClass = Integer.class,
                            example = "1"),
                    @DynamicParameter(name = "phoneNumForNow",
                            value = "任务创建时用户手机号，防止后续更改手机号的问题",
                            required = true,
                            dataTypeClass = String.class,
                            example = "12345678901"),
                    @DynamicParameter(name = "transactionAmount",
                            value = "交易金额",
                            required = true,
                            dataTypeClass = Double.class,
                            example = "1.0"),
                    @DynamicParameter(name = "expectedPeriod",
                            value = "task 可以显示的天数，超出就不显示了，表示任务过期",
                            required = true,
                            dataTypeClass = Integer.class,
                            example = "1")
            }
    )
    @PostMapping("/insert")
    public @Nullable ResponseResult insert(@RequestBody JSONObject json) {
        return taskService.insert(json)
                ? ResponseResult.success("任务信息创建成功")
                : ResponseResult.failure("任务信息创建失败");
    }

    /**
     * @param json 此json 里设定了选择标准
     *             "taskId":
     *             "userId":
     *             "sex":
     *             "type":
     *             "releaseTimeMax":
     *             "releaseTimeMin":
     *             "completeFlag":
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
                    @DynamicParameter(name = "taskId",
                            value = "任务 Id(可选)",
                            dataTypeClass = Integer.class,
                            example = "1"),
                    @DynamicParameter(name = "userId",
                            value = "任务创建者的 Id(可选)",
                            dataTypeClass = String.class,
                            example = "1"),
                    @DynamicParameter(name = "sex",
                            value = "性别(可选)",
                            dataTypeClass = String.class,
                            example = "男或女"),
                    @DynamicParameter(name = "type",
                            value = "目前核心 tag 有 “外卖” “交易”(可选)",
                            dataTypeClass = List.class,
                            example = "外卖"),
                    @DynamicParameter(name = "releaseTimeMax",
                            value = "任务发布时间最大值(可选)",
                            dataTypeClass = LocalDateTime.class,
                            example = "2020-12-12 12:12:12"),
                    @DynamicParameter(name = "releaseTimeMin",
                            value = "任务发布时间最小值(可选)",
                            dataTypeClass = LocalDateTime.class,
                            example = "2020-12-12 12:12:12"),
                    @DynamicParameter(name = "completeFlag",
                            value = "任务完成标志(可选)",
                            dataTypeClass = Integer.class,
                            example = "1"),
                    @DynamicParameter(name = "arrivalTimeMax",
                            value = "外卖送达时间最大值(可选)",
                            dataTypeClass = LocalDateTime.class,
                            example = "2020-12-12 12:12:12"),
                    @DynamicParameter(name = "arrivalTimeMin",
                            value = "外卖送达时间最小值(可选)",
                            dataTypeClass = LocalDateTime.class,
                            example = "2020-12-12 12:12:12"),
                    @DynamicParameter(name = "arrivalLocation",
                            value = "外卖送达地点(可选)",
                            dataTypeClass = String.class,
                            example = "外卖送达地点"),
                    @DynamicParameter(name = "targetLocation",
                            value = "外卖目标地点(可选)",
                            dataTypeClass = String.class,
                            example = "外卖目标地点"),
                    @DynamicParameter(name = "transactionMax",
                            value = "交易金额最大值(可选)",
                            dataTypeClass = Double.class,
                            example = "1.0"),
                    @DynamicParameter(name = "transactionMin",
                            value = "交易金额最小值(可选)",
                            dataTypeClass = Double.class,
                            example = "1.0")
            }
    )
    @PostMapping("/select")
    public @NotNull ResponseResult select(@RequestBody JSONObject json) {
        return ResponseResult.success(taskService.select(json), "获取任务信息成功");
    }

}
