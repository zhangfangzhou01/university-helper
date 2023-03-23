package com.yhm.universityhelper.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Api(tags = "页面跳转")
@Controller
public class WebviewController {
    @ApiOperation(value = "用户页面", notes = "用户页面")
    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @ApiOperation(value = "管理员页面", notes = "管理员页面")
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @ApiOperation(value = "hello页面", notes = "hello页面")
    @GetMapping("/index")
    public String hello() {
        return "index.html";
    }
}
