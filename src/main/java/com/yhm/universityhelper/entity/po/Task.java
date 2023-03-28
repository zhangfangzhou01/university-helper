package com.yhm.universityhelper.entity.po;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
    private Double transactionAmount;

    @TableField("expectedPeriod")
    private Integer expectedPeriod;

    @TableField("isHunter")
    private Integer isHunter;

    @TableField("type")
    private String type;

    @Override
    public int compareTo(Object o) {
        if (ObjectUtil.isEmpty(o) || !(o instanceof Task)) {
            return 0;
        }
        return ((Task)o).getPriority() - this.getPriority();
    }

    public JSONArray getTagArray() {
        return new JSONArray(tags);
    }

    // TODO: 接下来要归一化加权求优先值
    // TODO: 任务=发布时间+最大人数+期望时间+到达时间+交易金额
    public void autoSetPriority(
            Integer releaseTimeMax,
            Integer releaseTimeMin,
            Integer expectedPeriodMax,
            Integer expectedPeriodMin,
            Integer arrivalTimeMin,
            Integer arrivalTimeMax,
            Integer transactionAmountMax,
            Integer transactionAmountMin
    ) {
            double tmp = 0;
            // 公共考虑的属性有 发布时间， 预计时间
            tmp += Math.toIntExact((this.releaseTime.toEpochSecond(ZoneOffset.of("+8")) - releaseTimeMin) / (releaseTimeMax - releaseTimeMin));
            tmp += (this.expectedPeriod - expectedPeriodMin)/(expectedPeriodMax-expectedPeriodMin);
            if("外卖".equals(this.type) ){
                // 外卖考虑的属性有 送达时间
                tmp += Math.toIntExact((this.releaseTime.toEpochSecond(ZoneOffset.of("+8")) - arrivalTimeMin) / (arrivalTimeMax - arrivalTimeMin));
                this.priority = (int)(tmp*100)/4;
            }else if("交易".equals(this.type)){
                // 交易考虑的属性有 交易金额
                tmp += (this.transactionAmount - transactionAmountMin)/(transactionAmountMax-transactionAmountMin);
                this.priority = (int)(tmp*100)/4;
            }
    }
}
