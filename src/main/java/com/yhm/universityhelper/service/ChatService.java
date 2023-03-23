package com.yhm.universityhelper.service;

import com.yhm.universityhelper.entity.po.Chat;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhm
 * @since 2023-03-23
 */

@Transactional
public interface ChatService extends IService<Chat> {

}
