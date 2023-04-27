package com.yhm.universityhelper.validation;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yhm.universityhelper.dao.BlacklistMapper;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.entity.po.Blacklist;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.util.BeanUtils;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ChatValidator extends CustomValidator {

    public static boolean isBlacklisted(String srcUsername, String destUsername) {
        List<Map<String, Object>> userIds = BeanUtils.getBean(UserMapper.class).selectMaps(new LambdaQueryWrapper<User>().select(User::getUserId).in(User::getUsername, srcUsername, destUsername));
        Long srcUserId;
        Long destUserId;
        try {
            System.out.println(userIds);
            srcUserId = (Long) userIds.get(0).get("userId");
            destUserId = (Long) userIds.get(1).get("userId");
        } catch (Exception ignored) {
            srcUserId = (Long) userIds.get(1).get("userId");
            destUserId = (Long) userIds.get(0).get("userId");
        }
        
        return BeanUtils.getBean(BlacklistMapper.class).exists(new LambdaQueryWrapper<Blacklist>().eq(Blacklist::getBlockedId, srcUserId).eq(Blacklist::getBlockerId, destUserId));
    }

    // A拉黑B以后，B不能再给A发消息
    public static void chat(Authentication authentication, JSONObject msg) {
        Optional.ofNullable(msg.getStr("to"))
                .orElseThrow(() -> new RuntimeException("to字段不能为空"));
        
        Optional.ofNullable(authentication.getName())
                .orElseThrow(() -> new RuntimeException("未登录"));
        
        String srcUsername = authentication.getName();
        String destUsername = msg.getStr("to");
        
        if (isBlacklisted(srcUsername, destUsername)) {
            throw new RuntimeException(srcUsername + "已被" + destUsername + "拉黑" + "，不能再给" + srcUsername + "发消息");
        }
    }
    
    public static void groupChat(Authentication authentication, JSONObject msg) {
        Optional.ofNullable(msg.getJSONArray("to"))
                .orElseThrow(() -> new RuntimeException("to字段不能为空"));
        
        Optional.ofNullable(authentication.getName())
                .orElseThrow(() -> new RuntimeException("未登录"));
        
        String srcUsername = authentication.getName();
        List<String> destUsernames = msg.getJSONArray("to").toList(String.class);
        List<String> exceptionStr = new ArrayList<>();
        for (String destUsername : destUsernames) {
            if (isBlacklisted(srcUsername, destUsername)) {
                exceptionStr.add(srcUsername + "已被" + destUsername + "拉黑" + "，不能再给" + srcUsername + "发消息");
            }
        }
        if (!exceptionStr.isEmpty()) {
            throw new RuntimeException(String.join("\n", exceptionStr));
        }
    }
}
