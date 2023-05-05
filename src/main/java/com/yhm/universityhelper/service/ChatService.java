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
    
    void connected(Authentication authentication);
    
    void disconnected(Authentication authentication);

    void chat(Authentication authentication, JSONObject msg);

    void groupChat(Authentication authentication, JSONObject msg);

    void broadcast(Authentication authentication, JSONObject msg);

    void notification(Object obj);
    
    void notificationByUsername(String username, Object obj);

    void notificationByUsernames(List<String> usernames, Object obj);
    
    void notificationByUserId(Long userId, Object obj);
    
    void notificationByUserIds(List<Long> userIds, Object obj);
    
    Set<String> getOnlineUsers();

    int getOnlineUsersCount();
    
    void read(String receiver, String sender);
}
