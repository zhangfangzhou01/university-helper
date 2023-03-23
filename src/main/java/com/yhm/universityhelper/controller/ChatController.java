package com.yhm.universityhelper.controller;

import cn.hutool.json.JSONObject;
import com.yhm.universityhelper.entity.dto.ChatUser;
import com.yhm.universityhelper.entity.po.Chat;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.service.ChatService;
import com.yhm.universityhelper.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Api(tags = "聊天管理")
@Controller
@Transactional
public class ChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat")
    public void chat(Authentication authentication, JSONObject msg) {
        String srcUsername = authentication.getName();
        User srcUser = userService.selectByUsername(srcUsername);
        ChatUser srcChatUser = new ChatUser(srcUser);
        System.out.println(srcUsername);

        String destUsername = msg.getStr("to");
        User destUser = userService.selectByUsername(destUsername);
        ChatUser destChatUser = new ChatUser(destUser);

        String message = msg.getStr("content");

        Chat chat = new Chat(srcChatUser, destChatUser, message);
        chatService.insert(chat);

        simpMessagingTemplate.convertAndSendToUser(destUsername, "/queue/chat", chat);
    }

    @MessageMapping("/broadcast")
    public void broadcast(Authentication authentication, JSONObject msg) {
        String srcUsername = authentication.getName();
        User srcUser = userService.selectByUsername(srcUsername);
        ChatUser srcChatUser = new ChatUser(srcUser);
        System.out.println(srcUsername);

        String message = msg.getStr("content");
        userService.update(srcUser);

        Chat chat = new Chat(srcChatUser, message);
        chatService.insert(chat);

        simpMessagingTemplate.convertAndSend("/topic/broadcast", chat);
    }

    @MessageMapping("/notification")
    public void notification(String msg) {
        simpMessagingTemplate.convertAndSend("/topic/notification", msg);
    }
}
