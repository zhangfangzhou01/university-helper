package com.yhm.universityhelper.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhm.universityhelper.entity.po.Chat;
import org.springframework.security.core.Authentication;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yhm
 * @since 2023-03-23
 */

public interface ChatService extends IService<Chat> {
    int insert(Chat chat);

    void chat(Authentication authentication, JSONObject msg);

    void broadcast(Authentication authentication, JSONObject msg);

    void notification(String msg);
}
