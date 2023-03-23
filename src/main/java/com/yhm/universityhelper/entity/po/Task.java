package com.yhm.universityhelper.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yhm.universityhelper.util.JsonUtils;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

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
    private Integer taskId;

    @TableField("userId")
    private Integer userId;

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

    public void autoSetPriority(String priorityTags) {
        Map<String, Object> data = JsonUtils.jsonToMap(priorityTags);
        final Set<String> keys = data.keySet();
        for (String key : keys) {

        }
    }
}
