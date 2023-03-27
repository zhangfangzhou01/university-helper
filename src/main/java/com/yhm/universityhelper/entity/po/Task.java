package com.yhm.universityhelper.entity.po;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yhm.universityhelper.util.ReflectUtils;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

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

    public int autoSetPriority(JSONObject sortJson) {
        final String[] priorityRelatedColumns = {
                "releaseTime",
                "maxNumOfPeopleTake",
                "expectedPeriod",
                "arrivalTime",
                "transactionAmount",
        };
        for (String column : priorityRelatedColumns) {
            Object value = ReflectUtils.get(this, column);
            for (String key : sortJson.keySet()) {
                String sort = sortJson.get(key, String.class);
                if (StringUtils.isEmpty(sort) || ((!"asc".equalsIgnoreCase(sort)) && (!"desc".equalsIgnoreCase(sort)))) {
                    continue;
                }
                if (key.equals(column)) {
                    if (StringUtils.containsIgnoreCase(column, "time")) {
                        String timeStr = value.toString().replace(" ", "T");
                        LocalDateTime time = LocalDateTime.parse(timeStr);
                        if ("asc".equals(sort)) {
                            this.priority += time.getSecond();
                        } else if ("desc".equals(sort)) {
                            this.priority -= time.getSecond();
                        }
                    } else {
                        if ("asc".equals(sort)) {
                            this.priority += Integer.parseInt(value.toString());
                        } else if ("desc".equals(sort)) {
                            this.priority -= Integer.parseInt(value.toString());
                        }
                    }
                }
            }
        }
        return this.priority;
    }
}
