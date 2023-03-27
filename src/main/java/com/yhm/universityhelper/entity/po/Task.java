package com.yhm.universityhelper.entity.po;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author yhm
 * @since 2023-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uh_task")
@ApiModel(value = "UhTask对象", description = "")
public class Task implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "taskId", type = IdType.AUTO)
    private Long taskId;

    @TableField("userId")
    private Long userId;

    @TableField("tags")
    private String tags;

    @TableField("priority")
    private Integer priority;

    @TableField("releaseTime")
    private LocalDateTime releaseTime;

    @TableField("title")
    private String title;

    @TableField("requireDescription")
    private String requireDescription;

    @TableField("maxNumOfPeopleTake")
    private Integer maxNumOfPeopleTake;

    @TableField("score")
    private Integer score;

    @TableField("taskState")
    private Integer taskState;

    @TableField("takeoutId")
    private Integer takeoutId;

    @TableField("orderTime")
    private LocalDateTime orderTime;

    @TableField("arrivalTime")
    private LocalDateTime arrivalTime;

    @TableField("arrivalLocation")
    private String arrivalLocation;

    @TableField("targetLocation")
    private String targetLocation;

    @TableField("distance")
    private Integer distance;

    @TableField("phoneNumForNow")
    private Integer phoneNumForNow;

    @TableField("transactionAmount")
    private Integer transactionAmount;

    @TableField("expectedPeriod")
    private Integer expectedPeriod;

    @TableField("isHunter")
    private Integer isHunter;

    // 类型只能是外卖或者交易
    @Pattern(regexp = "^(外卖|交易)$", message = "类型只能是外卖或者交易")
    @TableField("type")
    private String type;

    @Override
    public int compareTo(Object o) {
        return ((Task)o).getPriority() - this.getPriority();
    }

    public JSONArray getTagArray() {
        return new JSONArray(tags);
    }

    // TODO: 接下来要归一化加权求优先值
    // TODO: 任务=发布时间+最大人数+期望时间+到达时间+交易金额
    public int autoSetPriority(
            LocalDateTime releaseTimeMax,
            LocalDateTime releaseTimeMin,
            Integer maxNumOfPeopleTakeMax,
            Integer maxNumOfPeopleTakeMin,
            Integer expectedPeriodMax,
            Integer expectedPeriodMin,
            LocalDateTime arrivalTimeMax,
            LocalDateTime arrivalTimeMin,
            Integer transactionAmountMax,
            Integer transactionAmountMin
    ) {
//            JSONObject jsonObject = (JSONObject)obj;
//            if (ObjectUtil.isEmpty(jsonObject) || jsonObject.isEmpty()) {
//                continue;
//            }
//
//            Integer order = jsonObject.get("order", Integer.class);
//            String column = jsonObject.get("column", String.class);
//            Boolean asc = jsonObject.get("asc", Boolean.class);
//            if (ObjectUtil.isEmpty(order) || ObjectUtil.isEmpty(column) || ObjectUtil.isEmpty(asc)) {
//                continue;
//            }

//            Object value = ReflectUtils.get(this, column);
//            if (column.equals("releaseTime")) {
////                if (StringUtils.containsIgnoreCase(priorityColumn, "time")) {
////                    String timeStr = value.toString().replace(" ", "T");
////                    LocalDateTime time = LocalDateTime.parse(timeStr);
////                    if (asc) {
//////                            this.priority += time.toEpochSecond();
////                    } else {
//////                            this.priority += time.toEpochSecond();
////                    }
////                } else {
////                    if (asc) {
////                        this.priority -= Integer.parseInt(value.toString());
////                    } else {
////                        this.priority += Integer.parseInt(value.toString());
////                    }
////                }
//                Long releaseTimeSecond = ((LocalDateTime)value).toEpochSecond(ZoneOffset.of("+8"));
//                Long releaseTimeMaxSecond = releaseTimeMax.toEpochSecond(ZoneOffset.of("+8"));
//                Long releaseTimeMinSecond = releaseTimeMin.toEpochSecond(ZoneOffset.of("+8"));
//
//                if (asc) {
//                    priority -=
//                }
//            }
        return 0;
    }
}
