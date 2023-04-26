package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.config.RedisCache;
import com.yhm.universityhelper.entity.po.Comment;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */

@CacheNamespace(implementation = RedisCache.class)
public interface CommentMapper extends BaseMapper<Comment> {

}
