package com.yhm.universityhelper.controller;

import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.service.UserService;
import com.yhm.universityhelper.validation.UserValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "登录管理")
@RestController
public class LoginController {
    @Autowired
    private UserService userService;
    
    // 登录
    @ApiOperation(value = "用户名密码登录")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "登录成功"),
            @ApiResponse(code = 500, message = "服务器响应错误"),
            @ApiResponse(code = 400, message = "参数错误"),
            @ApiResponse(code = 404, message = "资源未找到"),
            @ApiResponse(code = 405, message = "请求方法不支持"),
            @ApiResponse(code = 1001, message = "注册失败"),
            @ApiResponse(code = 1002, message = "用户名已存在"),
            @ApiResponse(code = 1003, message = "用户名不存在"),
            @ApiResponse(code = 1004, message = "用户名或密码错误"),
            @ApiResponse(code = 1005, message = "密码错误"),
            @ApiResponse(code = 1006, message = "账号过期"),
            @ApiResponse(code = 1007, message = "密码过期"),
            @ApiResponse(code = 1008, message = "账号不可用"),
            @ApiResponse(code = 1009, message = "账号锁定"),
            @ApiResponse(code = 1010, message = "用户未登录"),
            @ApiResponse(code = 1011, message = "用户权限不足"),
            @ApiResponse(code = 1012, message = "会话已超时"),
            @ApiResponse(code = 1013, message = "账号超时或账号在另一个地方登录"),
            @ApiResponse(code = 1014, message = "Token令牌验证失败")
    })
    @PostMapping("/login")
    public void login(@RequestParam String username, @RequestParam String password, @RequestParam Boolean rememberMe) {
    }

    @ApiOperation(value = "邮箱验证码登录")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "登录成功"),
            @ApiResponse(code = 500, message = "服务器响应错误"),
            @ApiResponse(code = 400, message = "参数错误"),
            @ApiResponse(code = 404, message = "资源未找到"),
            @ApiResponse(code = 405, message = "请求方法不支持"),
            @ApiResponse(code = 1001, message = "注册失败"),
            @ApiResponse(code = 1002, message = "用户名已存在"),
            @ApiResponse(code = 1003, message = "用户名不存在"),
            @ApiResponse(code = 1004, message = "用户名或密码错误"),
            @ApiResponse(code = 1005, message = "密码错误"),
            @ApiResponse(code = 1006, message = "账号过期"),
            @ApiResponse(code = 1007, message = "密码过期"),
            @ApiResponse(code = 1008, message = "账号不可用"),
            @ApiResponse(code = 1009, message = "账号锁定"),
            @ApiResponse(code = 1010, message = "用户未登录"),
            @ApiResponse(code = 1011, message = "用户权限不足"),
            @ApiResponse(code = 1012, message = "会话已超时"),
            @ApiResponse(code = 1013, message = "账号超时或账号在另一个地方登录"),
            @ApiResponse(code = 1014, message = "Token令牌验证失败")
    })
    @PostMapping("/email/login")
    public void emailLogin(@RequestParam String email, @RequestParam String code) {
    }

    @ApiOperation(value = "发送邮箱验证码")
    @PostMapping("/sendEmailCode")
    public ResponseResult<Object> sendEmailCode(@RequestParam String email) {
        UserValidator.sendEmailCode(email);
        userService.sendEmailCode(email);
        return ResponseResult.ok("发送成功");
    }

    // 登出
    @ApiOperation(value = "登出")
    @PostMapping("/logout")
    public void logout() {
    }
}
