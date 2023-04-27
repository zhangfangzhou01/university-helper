package com.yhm.universityhelper.dao;

import com.github.yulichang.base.MPJBaseMapper;
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

@CacheNamespace(implementation = RedisCache.class)
public interface PostMapper extends MPJBaseMapper<Post> {

}
