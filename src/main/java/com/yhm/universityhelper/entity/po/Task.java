package com.yhm.universityhelper.entity.po;

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
import java.util.Comparator;
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
@ApiModel(value="UhTask对象", description="")
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
        return ((Task) o).getPriority() - this.getPriority();
    }

    // 比较器
    public static final Comparator<Task> COMPARATOR_RELEASETIIME_ASC = (o1, o2) -> o1.getReleaseTime().getSecond()-o2.getReleaseTime().getSecond();
    public static final Comparator<Task> COMPARATOR_RELEASETIIME_DESC = (o1, o2) -> o2.getReleaseTime().getSecond()-o1.getReleaseTime().getSecond();

    public static final Comparator<Task> COMPARATOR_EXPERIODTIME_ASC = (o1, o2) -> o1.getExpectedPeriod()-o2.getExpectedPeriod();
    public static final Comparator<Task> COMPARATOR_EXPERIODTIME_DESC = (o1, o2) -> o2.getExpectedPeriod()-o1.getExpectedPeriod();

    public static final Comparator<Task> COMPARATOR_MAXNUMOFPEOPLE_ASC = (o1, o2) -> o1.getMaxNumOfPeopleTake()-o2.getMaxNumOfPeopleTake();
    public static final Comparator<Task> COMPARATOR_MAXNUMOFPEOPLE_DESC = (o1, o2) -> o2.getMaxNumOfPeopleTake()-o1.getMaxNumOfPeopleTake();

    public static final Comparator<Task> COMPARATOR_ARRIVALTIME_ASC= (o1, o2) -> o1.getArrivalTime().getSecond()-o2.getArrivalTime().getSecond();
    public static final Comparator<Task> COMPARATOR_ARRIVALTIME_DESC= (o1, o2) -> o2.getArrivalTime().getSecond()-o1.getArrivalTime().getSecond();

    public static final Comparator<Task> COMPARATOR_TRANSACTIONAMOUNT_ASC = (o1, o2) -> o1.getTransactionAmount()-o2.getTransactionAmount();
    public static final Comparator<Task> COMPARATOR_TRANSACTIONAMOUNT_DESC = (o1, o2) -> o2.getTransactionAmount()-o1.getTransactionAmount();


    public void autoSetPriority(Map<String, Object> sortData) {
        final Set<String> keys = sortData.keySet();
        // releaseTime 字段加权
        if("asc".equals(sortData.get("releaseTime"))){
            this.priority += this.releaseTime.getSecond();
        }else if("desc".equals(sortData.get("releaseTime"))){
            this.priority += this.releaseTime.getSecond();
        }
        // maxNumOfPeople 字段加权
        if("asc".equals(sortData.get("maxNumOfPeopleTake"))){
            this.priority += this.maxNumOfPeopleTake;
        }else if("desc".equals(sortData.get("maxNumOfPeopleTake"))){
            this.priority -= this.maxNumOfPeopleTake;
        }
        // expectedPeriod 字段加权
        if("asc".equals(sortData.get("expectedPeriod"))){
            this.priority += this.expectedPeriod;
        }else if("desc".equals(sortData.get("expectedPeriod"))){
            this.priority -= this.expectedPeriod;
        }
        // arrivalTime 字段加权
        if("asc".equals(sortData.get("arrivalTime"))){
            this.priority += this.arrivalTime.getSecond();
        }else if("desc".equals(sortData.get("arrivalTime"))){
            this.priority -= this.arrivalTime.getSecond();
        }
        // transactionAmount 字段加权
        if("asc".equals(sortData.get("transactionAmount"))){
            this.priority += this.transactionAmount;
        }else if("desc".equals(sortData.get("transactionAmount"))){
            this.priority -= this.transactionAmount;
        }
    }

}
