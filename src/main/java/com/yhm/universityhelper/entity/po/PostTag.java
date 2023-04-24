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
@TableName("uh_post_tags")
@ApiModel(value = "UhPostTags对象", description = "")
public class PostTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("tag")
    private String tag;

    @Version
    @TableField("version")
    private Integer version;

    public PostTag(String tag) {
        this.tag = tag;
    }
}
