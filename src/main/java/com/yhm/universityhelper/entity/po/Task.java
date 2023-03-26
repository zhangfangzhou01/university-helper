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

    public final String[] priorityColumn = {
            "releaseTime",
            "maxNumOfPeopleTake",
            "expectedPeriod",
            "arrivalTime",
            "transactionAmount"
    };

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

    @Override
    public int compareTo(Object o) {
        return ((Task)o).getPriority() - this.getPriority();
    }

    public JSONArray getTagArray() {
        return new JSONArray(tags);
    }

    public String getType() {
        return new JSONArray(tags).get(0).toString();
    }
}
