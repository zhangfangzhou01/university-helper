package com.yhm.universityhelper.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

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
    public ResponseResult<Object> update(@RequestBody JSONObject json) {
        return userService.update(json)
                ? ResponseResult.ok("个人信息修改成功")
                : ResponseResult.fail("个人信息修改失败");
    }

    // 获得个人信息
    @ApiOperation(value = "获取个人信息", notes = "获取个人信息")
    @PostMapping("/select")
    public ResponseResult<Map<String, Object>> select(@RequestBody JSONArray json) {
        return ResponseResult.ok(userService.select(json), "获取个人信息成功");
    }

    // 修改密码
    @ApiOperation(value = "修改密码")
    @PostMapping("/changePassword")
    public ResponseResult<Object> changePassword(@RequestParam String username, @RequestParam String oldPassword, @RequestParam String newPassword) {
        return userService.changePassword(username, oldPassword, newPassword)
                ? ResponseResult.ok("修改成功")
                : ResponseResult.fail("修改失败");
    }

    // 注册
    @ApiOperation(value = "注册")
    @PostMapping("/insert")
    public ResponseResult<Object> register(@RequestParam String username, @RequestParam String password) {
        return userService.register(username, password)
                ? ResponseResult.ok("注册成功")
                : ResponseResult.fail("注册失败");
    }

    // 删除用户
    @ApiOperation(value = "删除用户")
    @PostMapping("/delete")
    public ResponseResult<Object> delete(@RequestParam String username) {
        return userService.delete(username)
                ? ResponseResult.ok("删除成功")
                : ResponseResult.fail("删除失败");
    }

    // 封禁(解封)用户
    @ApiOperation(value = "封禁(解封)用户")
    @PostMapping("/ban")
    public ResponseResult<Object> ban(@RequestParam String username, @RequestParam boolean ban) {
        return userService.ban(username, ban)
                ? ResponseResult.ok("操作成功")
                : ResponseResult.fail("操作失败");
    }
}
