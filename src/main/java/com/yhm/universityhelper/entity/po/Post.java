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
import java.time.LocalDateTime;

/**
 * <p>
 * 论坛表，存储论坛信息
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uh_post")
@ApiModel(value="UhPost对象", description="论坛表，存储论坛信息")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "postId", type = IdType.AUTO)
    private Long postId;

    @TableField("userId")
    private Long userId;

    @TableField("title")
    private String title;

    @TableField("tags")
    private String tags;

    @ApiModelProperty(value = "帖子发布时间")
    @TableField("releaseTime")
    private LocalDateTime releaseTime;

    @TableField("lastModifyTime")
    private LocalDateTime lastModifyTime;

    @TableField("content")
    private String content;

    @TableField("likeNum")
    private Long likeNum;

    @TableField("dislikeNum")
    private Long dislikeNum;

    @TableField("starNum")
    private Long starNum;

    @TableField("commentNum")
    private Long commentNum;


}
