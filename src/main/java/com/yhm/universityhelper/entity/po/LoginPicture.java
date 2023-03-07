package com.yhm.universityhelper.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author yhm
 * @since 2023-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uh_login_picture")
@ApiModel(value = "LoginPicture", description = "")
public class LoginPicture implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pictureId", type = IdType.AUTO)
    private Integer pictureId;
    @TableField("picture")
    private Blob picture;
    @TableField("userId")
    private Integer userId;
    @TableField("isHeadPortrait")
    private Boolean isHeadPortrait;
    @TableField("createTime")
    private LocalDateTime createTime;
    @TableField("description")
    private String description;
}
