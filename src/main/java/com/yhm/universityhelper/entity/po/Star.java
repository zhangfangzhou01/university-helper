package com.yhm.universityhelper.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uh_star")
@ApiModel(value = "UhStar对象", description = "")
public class Star implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "starId", type = IdType.AUTO)
    private Long starId;

    @TableField("userId")
    private Long userId;

    @TableField("postId")
    private Long postId;

    @Version
    @TableField("version")
    private Integer version;

    public Star(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
