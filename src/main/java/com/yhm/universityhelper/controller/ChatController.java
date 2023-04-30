package com.yhm.universityhelper.controller;

import cn.hutool.json.JSONObject;
import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.service.ChatService;
import com.yhm.universityhelper.validation.ChatValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Api(tags = "聊天管理")
@Controller
public class ChatController {
    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat")
    public void chat(Authentication authentication, JSONObject msg) {
        ChatValidator.chat(authentication, msg);
        chatService.chat(authentication, msg);
    }

    @MessageMapping("/groupChat")
    public void groupChat(Authentication authentication, JSONObject msg) {
        ChatValidator.groupChat(authentication, msg);
        chatService.groupChat(authentication, msg);
    }

    @MessageMapping("/broadcast")
    public void broadcast(Authentication authentication, JSONObject msg) {
        chatService.broadcast(authentication, msg);
    }

    @PostMapping("/onlineUsers")
    @ResponseBody
    @ApiOperation(value = "获取在线用户列表")
    public ResponseResult<Set<String>> onlineUsers() {
        return ResponseResult.ok(chatService.getOnlineUsers(), "获取在线用户列表成功");
    }

    @GetMapping("/onlineUsersCount")
    @ResponseBody
    @ApiOperation(value = "获取在线用户数量")
    public ResponseResult<Integer> onlineUsersCount() {
        return ResponseResult.ok(chatService.getOnlineUsersCount(), "获取在线用户数量成功");
    }
    
    @PostMapping("/read")
    @ResponseBody
    @ApiOperation(value = "标记消息为已读")
    public ResponseResult<Object> read(String receiver, String sender) {
        chatService.read(receiver, sender);
        return ResponseResult.ok("标记消息为已读成功");
    }
}