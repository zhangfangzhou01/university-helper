package com.yhm.universityhelper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yhm.universityhelper.entity.po.Chat;

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
}
