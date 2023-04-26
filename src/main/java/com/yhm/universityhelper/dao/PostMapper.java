package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.config.RedisCache;
import com.yhm.universityhelper.entity.po.Post;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 * 论坛表，存储论坛信息 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */

@CacheNamespace(implementation = RedisCache.class, flushInterval = 291000)
public interface PostMapper extends BaseMapper<Post> {

}
