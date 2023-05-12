package com.yhm.universityhelper.entity.po;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("uh_comment")
@ApiModel(value = "UhComment对象", description = "")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "commentId", type = IdType.AUTO)
    private Long commentId;

    @TableField("userId")
    private Long userId;

    @TableField("postId")
    private Long postId;

    @TableField("releaseTime")
    private LocalDateTime releaseTime;

    @TableField("content")
    private String content;

    @TableField("likeNum")
    private Long likeNum;

    @TableField("replayCommentId")
    private Long replayCommentId;
    
    @TableField(value = "images", typeHandler = JacksonTypeHandler.class)
    private JSONArray images;

    @Version
    @TableField("version")
    private Integer version;
}
