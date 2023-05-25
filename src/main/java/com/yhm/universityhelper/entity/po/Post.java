package com.yhm.universityhelper.entity.po;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
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
@TableName(value = "uh_post", autoResultMap = true)
@ApiModel(value = "UhPost对象", description = "论坛表，存储论坛信息")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "postId", type = IdType.AUTO)
    private Long postId;

    @TableField("userId")
    private Long userId;

    @TableField("username")
    private String username;

    @TableField("title")
    private String title;

    @TableField(value = "tags", typeHandler = JacksonTypeHandler.class)
    private JSONArray tags;
    
    @ApiModelProperty(value = "帖子发布时间")
    @TableField("releaseTime")
    private LocalDateTime releaseTime;

    @TableField("lastModifyTime")
    private LocalDateTime lastModifyTime;

    @TableField("content")
    private String content;

    @TableField("likeNum")
    private Long likeNum;

    @TableField("starNum")
    private Long starNum;

    @TableField("commentNum")
    private Long commentNum;
    
    @TableField("viewNum")
    private Long viewNum;

    @Version
    @TableField("version")
    private Integer version;
}
