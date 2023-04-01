package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.entity.po.Chat;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yhm
 * @since 2023-03-23
 */

@Mapper
public interface ChatMapper extends BaseMapper<Chat> {
    @Insert("insert into universityhelper.uh_chat (fromUsername, toUsername, content, time) values (#{fromUsername}, #{toUsername}, #{content}, #{time})")
    int insert(Chat chat);
}
