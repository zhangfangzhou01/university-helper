package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.config.MybatisRedisCache;
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

@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface PostMapper extends BaseMapper<Post> {

}
