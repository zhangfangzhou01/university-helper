package com.yhm.universityhelper.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhm.universityhelper.entity.po.Chat;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yhm
 * @since 2023-03-23
 */

public interface ChatService extends IService<Chat> {

    void chat(Authentication authentication, JSONObject msg);

    void groupChat(Authentication authentication, JSONObject msg);

    void broadcast(Authentication authentication, JSONObject msg);

    void notification(String msg);
    
    void notificationByUsername(String username, String msg);

    void notificationByUsernames(List<String> usernames, String msg);
    
    void notificationByUserId(Long userId, String msg);
    
    void notificationByUserIds(List<Long> userIds, String msg);
    
    Set<String> getOnlineUsers();

    int getOnlineUsersCount();
    
    void read(String receiver, String sender);
}
