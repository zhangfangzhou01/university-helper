package com.yhm.universityhelper.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Api(tags = "页面跳转")
@Controller
public class WebviewController {
    @ApiOperation(value = "用户页面", notes = "用户页面")
    @GetMapping("/user")
    public @NotNull String user() {
        return "user";
    }

    @ApiOperation(value = "管理员页面", notes = "管理员页面")
    @GetMapping("/admin")
    public @NotNull String admin() {
        return "admin";
    }
}
