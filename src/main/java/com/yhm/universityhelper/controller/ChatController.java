package com.yhm.universityhelper.controller;

import cn.hutool.json.JSONObject;
import com.yhm.universityhelper.service.ChatService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Api(tags = "聊天管理")
@Controller
public class ChatController {
    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat")
    public void chat(Authentication authentication, JSONObject msg) {
        chatService.chat(authentication, msg);
    }

    @MessageMapping("/broadcast")
    public void broadcast(Authentication authentication, JSONObject msg) {
        chatService.broadcast(authentication, msg);
    }

    @MessageMapping("/notification")
    public void notification(String msg) {
        chatService.notification(msg);
    }
}
