package com.yhm.universityhelper.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.yhm.universityhelper.entity.po.Task;
import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "任务管理")
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskService taskService;

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
                    @DynamicParameter(name = "type",
                            value = "任务核心类别",
                            required = true,
                            dataTypeClass = String.class,
                            example = "外卖"
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
                            example = "0:发布但未领取 1:发布已领取 2:发布已完成"
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
                    ),
                    @DynamicParameter(name = "isHunter",
                            value = "任务双向类型",
                            required = true,
                            dataTypeClass = Integer.class,
                            example = "0: 雇主 1: 猎人"
                    ),
            })
    @PostMapping("/update")
    public ResponseResult update(@RequestBody JSONObject json) {
        return taskService.update(json)
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
                            example = "0:发布但未领取 1:发布已领取 2:发布已完成"
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
    public ResponseResult insert(@RequestBody JSONObject json) {
        return taskService.insert(json)
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
     * @return
     */
    @ApiOperation(value = "获取任务信息", notes = "获取任务信息")
    @DynamicParameters(
            name = "TaskSelectDto",
            properties = {
//                    @DynamicParameter(name = "taskId",
//                            value = "任务 Id",
//                            required = true,
//                            dataTypeClass = Integer.class,
//                            example = "1234321432"
//                    ),
//                    @DynamicParameter(name = "userId",
//                            value = "用户Id",
//                            required = true,
//                            dataTypeClass = Integer.class,
//                            example = "123344"
//                    ),
//                    @DynamicParameter(name = "sex",
//                            value = "性别",
//                            required = true,
//                            dataTypeClass = String.class,
//                            example = "男"
//                    ),
//                    @DynamicParameter(name = "type",
//                            value = "任务核心类型",
//                            required = true,
//                            dataTypeClass = String.class,
//                            example = "外卖"
//                    ),
//                    @DynamicParameter(name = "tags",
//                            value = "任务自定义tag",
//                            required = true,
//                            dataTypeClass = String.class,
//                            example = "辣椒, 红色"
//                    ),
//                    @DynamicParameter(name = "releaseTimeMin",
//                            value = "任务发布时间左区间",
//                            required = true,
//                            dataTypeClass = LocalDateTime.class,
//                            example = "2020-12-12 12:12:12"
//                    ),
//                    @DynamicParameter(name = "releaseTimeMax",
//                            value = "任务发布时间右区间",
//                            required = true,
//                            dataTypeClass = LocalDateTime.class,
//                            example = "2020-12-12 12:12:12"
//                    ),
//                    @DynamicParameter(name = "taskState",
//                            value = "任务状态",
//                            required = true,
//                            dataTypeClass = String.class,
//                            example = "0:发布但未领取 1:发布已领取 2:发布已完成"
//                    ),
//                    @DynamicParameter(name = "arrivalTimeMin",
//                            value = "外卖送达时间左区间",
//                            required = true,
//                            dataTypeClass = LocalDateTime.class,
//                            example = "2020-12-12 12:12:12"
//                    ),
//                    @DynamicParameter(name = "arrivalTimeMax",
//                            value = "外卖送达时间右区间",
//                            required = true,
//                            dataTypeClass = LocalDateTime.class,
//                            example = "2020-12-12 12:12:12"
//                    ),
//                    @DynamicParameter(name = "arrivalLocation",
//                            value = "外卖到校地点",
//                            required = true,
//                            dataTypeClass = LocalDateTime.class,
//                            example = "东校区西门"
//                    ),
//                    @DynamicParameter(name = "targetLocation",
//                            value = "外卖校内送达地点",
//                            required = true,
//                            dataTypeClass = LocalDateTime.class,
//                            example = "东八426"
//                    ),
//                    @DynamicParameter(name = "transactionAmountMin",
//                            value = "交易金额左区间",
//                            required = true,
//                            dataTypeClass = LocalDateTime.class,
//                            example = "100.00"
//                    ),
//                    @DynamicParameter(name = "transactionAmountMax",
//                            value = "交易金额右区间",
//                            required = true,
//                            dataTypeClass = LocalDateTime.class,
//                            example = "100.00"
//                    ),
//                    @DynamicParameter(name = "isHunter",
//                            value = "任务双向类型",
//                            required = true,
//                            dataTypeClass = Integer.class,
//                            example = "0: 雇主 1: 猎人"
//                    ),
                    @DynamicParameter(
                            name = "search",
                            value = "搜索",
                            required = true,
                            dataTypeClass = List.class,
                            example = "{\n" +
                                    "    \"taskId\": 1234321432,\n" +
                                    "    \"userId\": 123344,\n" +
                                    "    \"type\": \"外卖\",\n" +
                                    "    \"tags\": \"辣椒, 红色\",\n" +
                                    "    \"releaseTimeMin\": \"2020-12-12 12:12:12\",\n" +
                                    "    \"releaseTimeMax\": \"2020-12-12 12:12:12\",\n" +
                                    "    \"taskState\": \"0:发布但未领取 1:发布已领取 2:发布已完成\",\n" +
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
    @ApiImplicitParam(name = "taskId", value = "任务Id", required = true, dataType = "Long", example = "1234321432")
    @PostMapping("/delete")
    public ResponseResult<Boolean> delete(@RequestParam Long taskId, @RequestParam Long userId) {
        return taskService.delete(taskId, userId)
                ? ResponseResult.ok("删除任务信息成功")
                : ResponseResult.fail("删除任务信息失败");
    }
}
