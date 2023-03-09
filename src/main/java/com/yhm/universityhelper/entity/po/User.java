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
    private Integer userId;
    @TableField("username")
    private String username;
    @TableField("nickname")
    private String nickname;
    @TableField("password")
    private String password;
    @TableField("sex")
    private String sex;
    @TableField("pictureId")
    private Integer pictureId;
    @TableField("description")
    private String description;
    @TableField("school")
    private String school;
    @TableField("studentId")
    private Integer studentId;
    @TableField("email")
    private String email;
    @TableField("phone")
    private String phone;
    @TableField("location")
    private String location;

}
