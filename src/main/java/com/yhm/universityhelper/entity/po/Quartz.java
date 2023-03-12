package com.yhm.universityhelper.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.quartz.Job;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quartz {
    /**
     * 任务id
     */
    private String id;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务执行类
     */
    private Class<? extends Job> jobClass;

    /**
     * 任务状态 启动还是暂停
     */
    private Integer status;

    /**
     * 任务运行时间表达式
     */
    private String cronExpression;
}
