package com.yhm.universityhelper.validation;

import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/*@TableId(value = "userId", type = IdType.AUTO)
    private Long userId;

    @TableField("username")
    private String username;

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

    @TableField("school")
    private String school;

    @TableField("score")
    private Integer score;

    @TableField("createTime")
    private LocalDateTime createTime;

    @TableField("banned")
    private Boolean banned;

    @TableField("passwordErrorCount")
    private int passwordErrorCount;

    @TableField("unlockTime")
    private LocalDateTime unlockTime;*/

public class UserValidator {

    @Autowired
    private UserService userService;

    public void updateValidate(JSONObject user) {
        Optional.ofNullable(user.getLong("userId"))
                .map(userId -> Validator.validateNull(userId, "禁止修改用户ID"));

        Optional.ofNullable(user.getStr("username"))
                .map(username -> Validator.validateNumber(username, "用户名不合法"))
                .map(username -> new LambdaQueryWrapper<User>().eq(User::getUsername, username))
                .orElseThrow(() -> new RuntimeException("必须提供用户名"));

        Optional.ofNullable(user.getStr("avatar"))
                .map(avatar -> Validator.validateUrl(avatar, "头像地址不合法"));

        Optional.ofNullable(user.getStr("description"));
//                .map(description -> Validator.isPlateNumber()
    }
}
