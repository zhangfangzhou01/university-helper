package com.yhm.universityhelper.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 任务与其接受者的表
 * </p>
 *
 * @author yhm
 * @since 2023-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uh_usertaketask")
@ApiModel(value = "Usertaketask", description = "任务与其接受者的表")
public class Usertaketask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    @TableField("taskId")
    private Integer taskId;

    @TableField("userId")
    private Integer userId;


}
