package com.yhm.universityhelper.entity.po;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "uh_task", autoResultMap = true)
@ApiModel(value = "UhTask对象", description = "")
public class Task implements Serializable {

    final static public int NOT_PUBLISH = 0;
    final static public int NOT_TAKE = 1;
    final static public int TAKE = 2;
    final static public int COMPLETED = 3;
    private static final long serialVersionUID = 1L;
    @TableId(value = "taskId", type = IdType.AUTO)
    private Long taskId;

    @TableField("userId")
    private Long userId;

    @TableField("type")
    private String type;

    @TableField(value = "tags", typeHandler = JacksonTypeHandler.class)
    private JSONArray tags;

    @TableField(value = "images", typeHandler = JacksonTypeHandler.class)
    private JSONArray images;

    @TableField("releaseTime")
    private LocalDateTime releaseTime;

    @TableField("title")
    private String title;

    @TableField("requireDescription")
    private String requireDescription;

    @TableField("maxNumOfPeopleTake")
    private Integer maxNumOfPeopleTake = 1;

    @TableField("leftNumOfPeopleTake")
    private Integer leftNumOfPeopleTake;

    @TableField("expectedPeriod")
    private Integer expectedPeriod;

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
    private String phoneNumForNow;

    @TableField("transactionAmount")
    private Double transactionAmount;

    @TableField("isHunter")
    private Integer isHunter;

    @Version
    @TableField("version")
    private Integer version;
}
