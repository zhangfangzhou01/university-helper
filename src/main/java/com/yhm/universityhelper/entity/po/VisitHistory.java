package com.yhm.universityhelper.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户访问post的历史记录
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uh_visit_history")
@ApiModel(value = "UhVisitHistory对象", description = "用户访问post的历史记录")
public class VisitHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("userId")
    private Long userId;

    @TableField("postId")
    private Long postId;

    @TableField("visitTime")
    private LocalDateTime visitTime;

    @Version
    @TableField("version")
    private Integer version;

    public VisitHistory(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
        this.visitTime = LocalDateTime.now();
    }

}
