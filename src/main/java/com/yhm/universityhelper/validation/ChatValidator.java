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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ChatValidator extends CustomValidator {
    public static boolean isBlacklisted(String srcUsername, String destUsername) {
        AtomicReference<List<Object>> srcUserIdLst = new AtomicReference<>();
        AtomicReference<List<Object>> destUserIdLst = new AtomicReference<>();
        final Thread srcUserIdThread = Thread.ofVirtual().unstarted(() -> srcUserIdLst.set(BeanUtils.getBean(UserMapper.class).selectObjs(new LambdaQueryWrapper<User>().select(User::getUserId).eq(User::getUsername, srcUsername))));
        final Thread destUserIdThread = Thread.ofVirtual().unstarted(() -> destUserIdLst.set(BeanUtils.getBean(UserMapper.class).selectObjs(new LambdaQueryWrapper<User>().select(User::getUserId).eq(User::getUsername, destUsername))));
        srcUserIdThread.start();
        destUserIdThread.start();
        try {
            srcUserIdThread.join();
            destUserIdThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Long srcUserId = (Long)srcUserIdLst.get().get(0);
        Long destUserId = (Long)destUserIdLst.get().get(0);
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
