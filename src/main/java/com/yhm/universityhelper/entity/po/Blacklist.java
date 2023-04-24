package com.yhm.universityhelper.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@TableName("uh_blacklist")
@ApiModel(value = "UhBlacklist对象", description = "")
public class Blacklist implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "拉黑别人的人的userId")
    @TableField("blocker")
    private Long blockerId;

    @ApiModelProperty(value = "被拉黑的人的userId")
    @TableField("blocked")
    private Long blockedId;
    
    @Version
    @TableField("version")
    private Integer version;

    public Blacklist(Long blockerId, Long blockedId) {
        this.blockerId = blockerId;
        this.blockedId = blockedId;
    }
}
