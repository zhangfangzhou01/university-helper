package com.yhm.universityhelper.controller;

import com.yhm.universityhelper.entity.dto.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;

public class ChatController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/greeting")
    public void greeting(String message) {
        simpMessagingTemplate.convertAndSend("/topic/greeting", message);
    }

    @MessageMapping("/chat")
    public void chat(Principal principal, Chat chat){
        chat.setFrom(principal.getName());
        simpMessagingTemplate.convertAndSendToUser(chat.getTo(),"/queue/chat",chat);
    }

    @MessageMapping("/ws/chat")
    public void handleChat(Principal principal, String msg) {
        String destUser = msg.substring(msg.lastIndexOf(";") + 1, msg.length());
        String message = msg.substring(0, msg.lastIndexOf(";"));
        simpMessagingTemplate.convertAndSendToUser(destUser, "/queue/chat", new Chat(message, principal.getName()));
    }

    @MessageMapping("/ws/notification")
    @SendTo("/topic/notification")
    public String handleNotification() {
        return "System Notification";
    }
}
