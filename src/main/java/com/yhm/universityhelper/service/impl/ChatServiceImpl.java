package com.yhm.universityhelper.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhm.universityhelper.dao.ChatMapper;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.dao.WebsocketOnlineMapper;
import com.yhm.universityhelper.entity.dto.ChatUser;
import com.yhm.universityhelper.entity.po.Chat;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.entity.po.WebscoketOnline;
import com.yhm.universityhelper.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yhm
 * @since 2023-03-23
 */

@Transactional
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WebsocketOnlineMapper websocketOnlineMapper;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private static final Set<String> ONLINE_USERS = ConcurrentHashMap.newKeySet();

    @Override
    public void connected(Authentication authentication) {
        String username = authentication.getName();
        ONLINE_USERS.add(username);
        websocketOnlineMapper.insertOrUpdate(new WebscoketOnline(username, true));
    }
    
    @Override
    public void disconnected(Authentication authentication) {
        String username = authentication.getName();
        ONLINE_USERS.remove(username);
        websocketOnlineMapper.insertOrUpdate(new WebscoketOnline(username, false));
    }
    
    @Override
    public void chat(Authentication authentication, JSONObject msg) {
        String srcUsername = authentication.getName();
        User srcUser = userMapper.selectByUsername(srcUsername);
        ChatUser srcChatUser = new ChatUser(srcUser);

        String destUsername = msg.getStr("to");
        User destUser = userMapper.selectByUsername(destUsername);
        ChatUser destChatUser = new ChatUser(destUser);

        String message = msg.getStr("content");

        Chat chat = new Chat(srcChatUser, destChatUser, message);
        chatMapper.insert(chat);

        simpMessagingTemplate.convertAndSendToUser(destUsername, "/queue/chat", chat);
    }

    @Override
    public void groupChat(Authentication authentication, JSONObject msg) {
        String srcUsername = authentication.getName();
        User srcUser = userMapper.selectByUsername(srcUsername);
        ChatUser srcChatUser = new ChatUser(srcUser);

        List<String> destUsernames = msg.getJSONArray("to").toList(String.class);
        List<User> destUsers = userMapper.selectList(new LambdaQueryWrapper<User>().in(User::getUsername, destUsernames));
        List<ChatUser> destChatUsers = destUsers.stream().map(ChatUser::new).collect(Collectors.toList());

        String message = msg.getStr("content");

        List<Chat> chats = destChatUsers.parallelStream().map(destChatUser -> new Chat(srcChatUser, destChatUser, message)).toList();
        Thread.startVirtualThread(() -> chatMapper.insertBatch(chats));
        for (Chat chat : chats) {
            Thread.startVirtualThread(() -> simpMessagingTemplate.convertAndSendToUser(chat.getToUsername(), "/queue/chat", chat));
        }
    }

    @Override
    public void broadcast(Authentication authentication, JSONObject msg) {
        String srcUsername = authentication.getName();
        User srcUser = userMapper.selectByUsername(srcUsername);
        ChatUser srcChatUser = new ChatUser(srcUser);

        String message = msg.getStr("content");

        Chat chat = new Chat(srcChatUser, message);
        chatMapper.insert(chat);

        simpMessagingTemplate.convertAndSend("/topic/broadcast", chat);
    }

    @Override
    public void notification(String msg) {
        simpMessagingTemplate.convertAndSend("/topic/notification", msg);
    }

    @Override
    public void notificationByUsername(String username, String msg) {
        simpMessagingTemplate.convertAndSendToUser(username, "/topic/notification", msg);
    }

    @Override
    public void notificationByUsernames(List<String> usernames, String msg) {
        for (String username : usernames) {
            Thread.startVirtualThread(() -> simpMessagingTemplate.convertAndSendToUser(username, "/topic/notification", msg));
        }
    }

    @Override
    public void notificationByUserId(Long userId, String msg) {
        final String username = userMapper.selectUsernameByUserId(userId);
        simpMessagingTemplate.convertAndSendToUser(username, "/topic/notification", msg);
    }

    @Override
    public void notificationByUserIds(List<Long> userIds, String msg) {
        final List<String> usernames = userMapper.selectBatchUsernameByBatchUserId(userIds);
        for (String username : usernames) {
            Thread.startVirtualThread(() -> simpMessagingTemplate.convertAndSendToUser(username, "/topic/notification", msg));
        }
    }

    @Override
    public Set<String> getOnlineUsers() {
        return ONLINE_USERS;
    }

    @Override
    public int getOnlineUsersCount() {
        return ONLINE_USERS.size();
    }

    @Override
    public void read(String receiver, String sender) {
        final List<Chat> chats = chatMapper.selectUnread(receiver, sender);
        chats.forEach(chat -> chat.setIsRead(true));
        this.updateBatchById(chats);
    }
}
