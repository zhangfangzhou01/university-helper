package com.yhm.universityhelper.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.yhm.universityhelper.entity.po.UserRole;
import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.service.ChatService;
import com.yhm.universityhelper.service.UserService;
import com.yhm.universityhelper.util.TokenUtils;
import com.yhm.universityhelper.validation.CustomValidator;
import com.yhm.universityhelper.validation.UserValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Shinki
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private TokenUtils tokenUtils;

    // 修改个人信息
    @ApiOperation(value = "修改个人信息", notes = "修改个人信息")
    @DynamicParameters(
            name = "UserUpdateDto",
            properties = {
                    @DynamicParameter(name = "userId",
                            value = "用户id(必填)",
                            required = true,
                            dataTypeClass = Long.class,
                            example = "1"
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
    public ResponseResult<Map<String, Object>> select(@RequestBody JSONArray json) {
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

    // 删除用户
    @ApiOperation(value = "删除用户")
    @PostMapping("/delete")
    public ResponseResult<Object> delete(@RequestParam Long userId) {
        UserValidator.delete(userId);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        Map<Long, List<String>> taskIdAndUsernames = userService.delete(userId);
        Thread.startVirtualThread(() ->
                taskIdAndUsernames.forEach((taskId, usernames) -> chatService.notificationByUsernames(usernames, "任务" + taskId + "已被任务发布者删除"))
        );
        ResponseResult<Object> responseResult = ResponseResult.ok("删除成功");
        tokenUtils.expireToken(userId);
        return responseResult;
    }

    // 封禁(解封)用户
    @ApiOperation(value = "封禁(解封)用户")
    @PostMapping("/ban")
    public ResponseResult<Object> ban(@RequestParam Long userId, @RequestParam boolean ban) {
        UserValidator.ban(userId, ban);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_NOBODY);
        ResponseResult<Object> responseResult = userService.ban(userId, ban)
                ? ResponseResult.ok("操作成功")
                : ResponseResult.fail("操作失败");
        tokenUtils.expireToken(userId);
        return responseResult;
    }

    // 设置用户角色
    @ApiOperation(value = "设置用户角色")
    @PostMapping("/setRole")
    public ResponseResult<Object> setRole(@RequestParam Long userId, @RequestParam String role) {
        UserValidator.setRole(userId, role);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_NOBODY);
        ResponseResult<Object> responseResult = userService.setRole(userId, role)
                ? ResponseResult.ok("设置成功")
                : ResponseResult.fail("设置失败");
        tokenUtils.expireToken(userId);
        return responseResult;
    }

    // 关注
    @ApiOperation(value = "关注")
    @PostMapping("/follow")
    public ResponseResult<Object> follow(@RequestParam Long followerId, @RequestParam Long followedId) {
        UserValidator.follow(followerId, followedId);
        CustomValidator.auth(followerId, UserRole.USER_CAN_CHANGE_SELF);
        return userService.follow(followerId, followedId)
                ? ResponseResult.ok("关注成功")
                : ResponseResult.fail("关注失败");
    }

    // 取消关注
    @ApiOperation(value = "取消关注")
    @PostMapping("/unfollow")
    public ResponseResult<Object> unfollow(@RequestParam Long followerId, @RequestParam Long followedId) {
        UserValidator.unfollow(followerId, followedId);
        CustomValidator.auth(followerId, UserRole.USER_CAN_CHANGE_SELF);
        return userService.unfollow(followerId, followedId)
                ? ResponseResult.ok("取消关注成功")
                : ResponseResult.fail("取消关注失败");
    }

    // 拉黑
    @ApiOperation(value = "拉黑")
    @PostMapping("/block")
    public ResponseResult<Object> block(@RequestParam Long blockerId, @RequestParam Long blockedId) {
        UserValidator.block(blockerId, blockedId);
        CustomValidator.auth(blockerId, UserRole.USER_CAN_CHANGE_SELF);
        return userService.block(blockerId, blockedId)
                ? ResponseResult.ok("拉黑成功")
                : ResponseResult.fail("拉黑失败");
    }

    // 取消拉黑
    @ApiOperation(value = "取消拉黑")
    @PostMapping("/unblock")
    public ResponseResult<Object> unblock(@RequestParam Long blockerId, @RequestParam Long blockedId) {
        UserValidator.unblock(blockerId, blockedId);
        CustomValidator.auth(blockerId, UserRole.USER_CAN_CHANGE_SELF);
        return userService.unblock(blockerId, blockedId)
                ? ResponseResult.ok("取消拉黑成功")
                : ResponseResult.fail("取消拉黑失败");
    }

    // 获取关注列表
    @ApiOperation(value = "获取关注列表")
    @PostMapping("/selectFollowedList")
    public ResponseResult<List<String>> getFollowedList(@RequestParam Long userId) {
        UserValidator.selectFollowedList(userId);
        return ResponseResult.ok(userService.selectFollowedList(userId), "获取关注列表成功");
    }

    // 获取粉丝列表
    @ApiOperation(value = "获取粉丝列表")
    @PostMapping("/selectFollowerList")
    public ResponseResult<List<String>> getFollowerList(@RequestParam Long userId) {
        UserValidator.selectFollowerList(userId);
        return ResponseResult.ok(userService.selectFollowerList(userId), "获取粉丝列表成功");
    }

    // 获取关注人数
    @ApiOperation(value = "获取关注人数")
    @PostMapping("/selectFollowedCount")
    public ResponseResult<Long> getFollowedCount(@RequestParam Long userId) {
        UserValidator.selectFollowedCount(userId);
        return ResponseResult.ok(userService.selectFollowedCount(userId), "获取关注人数成功");
    }

    // 获取粉丝人数
    @ApiOperation(value = "获取粉丝人数")
    @PostMapping("/selectFollowerCount")
    public ResponseResult<Long> selectFollowerCount(@RequestParam Long userId) {
        UserValidator.selectFollowerCount(userId);
        return ResponseResult.ok(userService.selectFollowerCount(userId), "获取粉丝人数成功");
    }

    // 获取拉黑列表
    @ApiOperation(value = "获取拉黑列表")
    @PostMapping("/selectBlockedList")
    public ResponseResult<List<String>> getBlockedList(@RequestParam Long userId) {
        UserValidator.selectBlockedList(userId);
        return ResponseResult.ok(userService.selectBlockedList(userId), "获取拉黑列表成功");
    }

    // 获取拉黑人数
    @ApiOperation(value = "获取拉黑人数")
    @PostMapping("/selectBlockedCount")
    public ResponseResult<Long> getBlockedCount(@RequestParam Long userId) {
        UserValidator.selectBlockedCount(userId);
        return ResponseResult.ok(userService.selectBlockedCount(userId), "获取拉黑人数成功");
    }

    // 修改邮箱
    @ApiOperation(value = "修改邮箱")
    @PostMapping("/changeEmail")
    public ResponseResult<Object> changeEmail(@RequestParam Long userId, @RequestParam String email) {
        UserValidator.changeEmail(userId, email);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return userService.changeEmail(userId, email)
                ? ResponseResult.ok("修改邮箱成功")
                : ResponseResult.fail("修改邮箱失败");
    }
}
