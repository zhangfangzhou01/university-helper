package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.config.RedisCache;
import com.yhm.universityhelper.entity.po.Chat;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yhm
 * @since 2023-03-23
 */

@CacheNamespace(implementation = RedisCache.class, flushInterval = 188000)
public interface ChatMapper extends BaseMapper<Chat> {
    @Select("select count(*) from universityhelper.uh_chat where toUsername = #{receiver} and fromUsername = #{sender} and `isRead` = false")
    long selectUnreadCount(String receiver, String sender);

    @Select("select * from universityhelper.uh_chat where toUsername = #{receiver} and fromUsername = #{sender} and `isRead` = false")
    List<Chat> selectUnread(String receiver, String sender);

    @Select("select * from universityhelper.uh_chat where toUsername = #{receiver} and fromUsername = #{sender}")
    List<Chat> selectAll(String receiver, String sender);

    @Select("select * from universityhelper.uh_chat where toUsername = #{receiver} and fromUsername = #{sender} order by time desc limit 1")
    Chat selectLast(String receiver, String sender);
    
    @Select("select * from universityhelper.uh_chat where toUsername = #{receiver} and fromUsername = #{sender} and `isRead` = false order by time desc limit 1")
    Chat selectLastUnread(String receiver, String sender);
}
