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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uh_user")
@ApiModel(value = "User", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "userId", type = IdType.AUTO)
    private Long userId;
    @TableField("studentId")
    private Integer studentId;
    @TableField("avatar")
    private String avatar;
    @TableField("description")
    private String description;
    @TableField("location")
    private String location;
    @TableField("phone")
    private String phone;
    @TableField("email")
    private String email;
    @TableField("sex")
    private String sex;
    @TableField("password")
    private String password;
    @TableField("nickname")
    private String nickname;
    @TableField("username")
    private String username;
    @TableField("school")
    private String school;
    @TableField("score")
    private Integer score;
    @TableField("createTime")
    private LocalDateTime createTime;
}
