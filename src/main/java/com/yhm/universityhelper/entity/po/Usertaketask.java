package com.yhm.universityhelper.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uh_usertaketask")
@ApiModel(value = "Usertaketask", description = "任务与其接受者的表")
public class Usertaketask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("taskId")
    private Long taskId;

    @TableField("userId")
    private Long userId;

    public Usertaketask(Long taskId, Long userId) {
        this.taskId = taskId;
        this.userId = userId;
    }
}
