package com.yhm.universityhelper.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * follower 关注 followed
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uh_follow")
@ApiModel(value = "UhFollow对象", description = "follower 关注 followed")
public class Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "关注者 Id(粉丝)")
    @TableField("follower")
    private Long followerId;

    @ApiModelProperty(value = "被关注者")
    @TableField("followed")
    private Long followedId;

    public Follow(Long followerId, Long followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
    }

}
