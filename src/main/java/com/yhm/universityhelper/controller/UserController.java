package com.yhm.universityhelper.controller;

import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    // 修改个人信息
    @ApiOperation(value = "修改个人信息", notes = "修改个人信息")
    @DynamicParameters(
            name = "UserUpdateDto",
            properties = {
                    @DynamicParameter(name = "username",
                            value = "用户名",
                            required = true,
                            dataTypeClass = String.class,
                            example = "username"
                    ),
                    @DynamicParameter(name = "nickname",
                            value = "昵称(可选)",
                            dataTypeClass = String.class,
                            example = "nickname"
                    ),
                    @DynamicParameter(name = "sex",
                            value = "性别(可选)",
                            dataTypeClass = String.class,
                            example = "男或女"
                    ),
                    @DynamicParameter(name = "pictureId",
                            value = "头像id(可选)",
                            dataTypeClass = Integer.class,
                            example = "1"
                    ),
                    @DynamicParameter(name = "description",
                            value = "个人简介(可选)",
                            dataTypeClass = String.class,
                            example = "个人简介"
                    ),
                    @DynamicParameter(name = "school",
                            value = "学校(可选)",
                            dataTypeClass = String.class,
                            example = "学校"
                    ),
                    @DynamicParameter(name = "studentId",
                            value = "学号(可选)",
                            dataTypeClass = Integer.class,
                            example = "1"
                    ),
                    @DynamicParameter(name = "email",
                            value = "邮箱(可选)",
                            dataTypeClass = String.class,
                            example = "邮箱"
                    ),
                    @DynamicParameter(name = "phone",
                            value = "电话(可选)",
                            dataTypeClass = String.class,
                            example = "电话"
                    ),
                    @DynamicParameter(name = "location",
                            value = "地址(可选)",
                            dataTypeClass = String.class,
                            example = "地址"
                    ),
            }
    )
    @PostMapping("/update")
    public ResponseResult update(@RequestBody JSONObject json) {
        return userService.update(json)
                ? ResponseResult.success("个人信息修改成功")
                : ResponseResult.failure("个人信息修改失败");
    }

    // 获得个人信息
    @ApiOperation(value = "获取个人信息", notes = "获取个人信息")
    @DynamicParameters(
            name = "UserSelectDto",
            properties = {
                    @DynamicParameter(name = "usernames",
                            value = "用户名数组",
                            required = true,
                            dataTypeClass = List.class,
                            example = "[\"username1\",\"username2\"]"
                    ),
            }
    )
    @PostMapping("/select")
    public ResponseResult select(@RequestBody JSONObject json) {
        return ResponseResult.success(userService.select(json));
    }
}
