package com.yhm.universityhelper.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Api(tags = "页面跳转")
@Controller
public class WebviewController {
    @ApiOperation(value = "主页面", notes = "主页面")
    @GetMapping("/index")
    public String hello() {
        return "index.html";
    }
}
