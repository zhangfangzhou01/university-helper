package com.yhm.universityhelper.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.yhm.universityhelper.entity.po.UserRole;
import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.service.ChatService;
import com.yhm.universityhelper.service.UserService;
import com.yhm.universityhelper.validation.CustomValidator;
import com.yhm.universityhelper.validation.UserValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

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
        UserValidator.update(json);
        CustomValidator.auth(json.getStr("username"), UserRole.USER_CAN_CHANGE_SELF);
        return userService.update(json)
                ? ResponseResult.ok("个人信息修改成功")
                : ResponseResult.fail("个人信息修改失败");
    }

    // 获得个人信息
    @ApiOperation(value = "获取个人信息", notes = "获取个人信息")
    @PostMapping("/select")
    public ResponseResult<Map<String, Object>> select(@Validated @RequestBody JSONArray json) {
        UserValidator.select(json);
        return ResponseResult.ok(userService.select(json), "获取个人信息成功");
    }

    // 修改密码
    @ApiOperation(value = "修改密码")
    @PostMapping("/changePassword")
    public ResponseResult<Object> changePassword(@RequestParam String username, @RequestParam String oldPassword, @RequestParam String newPassword) {
        UserValidator.changePassword(username, oldPassword, newPassword);
        CustomValidator.auth(username, UserRole.USER_CAN_CHANGE_SELF);
        return userService.changePassword(username, oldPassword, newPassword)
                ? ResponseResult.ok("修改成功")
                : ResponseResult.fail("修改失败");
    }

    // 注册
    @ApiOperation(value = "注册")
    @PostMapping("/insert")
    public ResponseResult<Object> register(@RequestParam String username, @RequestParam String password) {
        UserValidator.register(username, password);
        return userService.register(username, password)
                ? ResponseResult.ok("注册成功")
                : ResponseResult.fail("注册失败");
    }

    // 删除用户
    @ApiOperation(value = "删除用户")
    @PostMapping("/delete")
    public ResponseResult<Object> delete(@RequestParam String username) {
        UserValidator.delete(username);
        CustomValidator.auth(username, UserRole.USER_CAN_CHANGE_SELF);
        Map<Long, List<String>> taskIdAndUsernames = userService.delete(username);

        taskIdAndUsernames.forEach((taskId, usernames) -> {
            chatService.notification(usernames, "任务" + taskId + "已被任务发布者删除");
        });
        return ResponseResult.ok("删除成功");
    }

    // 封禁(解封)用户
    @ApiOperation(value = "封禁(解封)用户")
    @PostMapping("/ban")
    public ResponseResult<Object> ban(@RequestParam String username, @RequestParam boolean ban) {
        UserValidator.ban(username, ban);
        CustomValidator.auth(username, UserRole.USER_CAN_CHANGE_NOBODY);
        return userService.ban(username, ban)
                ? ResponseResult.ok("操作成功")
                : ResponseResult.fail("操作失败");
    }

    // 设置用户角色
    @ApiOperation(value = "设置用户角色")
    @PostMapping("/setRole")
    public ResponseResult<Object> setRole(@RequestParam String username, @RequestParam String role) {
        UserValidator.setRole(username, role);
        CustomValidator.auth(username, UserRole.USER_CAN_CHANGE_NOBODY);
        return userService.setRole(username, role)
                ? ResponseResult.ok("设置成功")
                : ResponseResult.fail("设置失败");
    }

    @ApiOperation(value = "关注")
    @PostMapping("/follow")
    public ResponseResult<Object> follow(@RequestParam String follower, @RequestParam String followed) {
        UserValidator.follow(follower, followed);
        CustomValidator.auth(follower, UserRole.USER_CAN_CHANGE_SELF);
        return userService.follow(follower, followed)
                ? ResponseResult.ok("关注成功")
                : ResponseResult.fail("关注失败");
    }

    @ApiOperation(value = "取消关注")
    @PostMapping("/unfollow")
    public ResponseResult<Object> unfollow(@RequestParam String follower, @RequestParam String followed) {
        UserValidator.unfollow(follower, followed);
        CustomValidator.auth(follower, UserRole.USER_CAN_CHANGE_SELF);
        return userService.unfollow(follower, followed)
                ? ResponseResult.ok("取消关注成功")
                : ResponseResult.fail("取消关注失败");

    }

    @ApiOperation(value = "拉黑")
    @PostMapping("/block")
    public ResponseResult<Object> block(@RequestParam String blocker, @RequestParam String blocked) {
        UserValidator.block(blocker, blocked);
        CustomValidator.auth(blocker, UserRole.USER_CAN_CHANGE_SELF);
        return userService.block(blocker, blocked)
                ? ResponseResult.ok("拉黑成功")
                : ResponseResult.fail("拉黑失败");
    }

    @ApiOperation(value = "取消拉黑")
    @PostMapping("/unblock")
    public ResponseResult<Object> unblock(@RequestParam String blocker, @RequestParam String blocked) {
        UserValidator.unblock(blocker, blocked);
        CustomValidator.auth(blocker, UserRole.USER_CAN_CHANGE_SELF);
        return userService.unblock(blocker, blocked)
                ? ResponseResult.ok("取消拉黑成功")
                : ResponseResult.fail("取消拉黑失败");
    }

    @ApiOperation(value = "获取关注列表")
    @PostMapping("/getFollowedList")
    public ResponseResult<List<String>> getFollowedList(@RequestParam String username) {
        UserValidator.getFollowedList(username);
        CustomValidator.auth(username, UserRole.USER_CAN_CHANGE_SELF);
        return ResponseResult.ok(userService.getFollowedList(username), "获取关注列表成功");
    }

    @ApiOperation(value = "获取粉丝列表")
    @PostMapping("/getFollowerList")
    public ResponseResult<List<String>> getFollowerList(@RequestParam String username) {
        UserValidator.getFollowerList(username);
        CustomValidator.auth(username, UserRole.USER_CAN_CHANGE_SELF);
        return ResponseResult.ok(userService.getFollowerList(username), "获取粉丝列表成功");
    }

    @ApiOperation(value = "获取关注人数")
    @PostMapping("/getFollowedCount")
    public ResponseResult<Long> getFollowedCount(@RequestParam String username) {
        UserValidator.getFollowedCount(username);
        CustomValidator.auth(username, UserRole.USER_CAN_CHANGE_SELF);
        return ResponseResult.ok(userService.getFollowedCount(username), "获取关注人数成功");
    }

    @ApiOperation(value = "获取粉丝人数")
    @PostMapping("/getFollowerCount")
    public ResponseResult<Long> getFollowerCount(@RequestParam String username) {
        UserValidator.getFollowerCount(username);
        CustomValidator.auth(username, UserRole.USER_CAN_CHANGE_SELF);
        return ResponseResult.ok(userService.getFollowerCount(username), "获取粉丝人数成功");
    }

    @ApiOperation(value = "获取拉黑列表")
    @PostMapping("/getBlockedList")
    public ResponseResult<List<String>> getBlockedList(@RequestParam String username) {
        UserValidator.getBlockedList(username);
        CustomValidator.auth(username, UserRole.USER_CAN_CHANGE_SELF);
        return ResponseResult.ok(userService.getBlockedList(username), "获取拉黑列表成功");
    }

    @ApiOperation(value = "获取拉黑人数")
    @PostMapping("/getBlockedCount")
    public ResponseResult<Long> getBlockedCount(@RequestParam String username) {
        UserValidator.getBlockedCount(username);
        CustomValidator.auth(username, UserRole.USER_CAN_CHANGE_SELF);
        return ResponseResult.ok(userService.getBlockedCount(username), "获取拉黑人数成功");
    }
}
