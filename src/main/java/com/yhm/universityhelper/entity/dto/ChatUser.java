package com.yhm.universityhelper.entity.dto;


import com.yhm.universityhelper.entity.po.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatUser {
    private String username;
    private String avatar;
    private String nickname;
    private String lastMessage;
    private LocalDateTime lastTime;
    private String description;

    public ChatUser(User user) {
        this.username = user.getUsername();
        this.avatar = user.getAvatar();
        this.nickname = user.getNickname();
        this.description = user.getDescription();
        this.lastMessage = user.getLastMessage();
        this.lastTime = user.getLastTime();
    }
}
