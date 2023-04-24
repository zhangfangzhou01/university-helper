package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.config.RedisCache;
import com.yhm.universityhelper.entity.po.Follow;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 * follower 关注 followed Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */

@CacheNamespace(implementation = RedisCache.class, eviction = RedisCache.class)
public interface FollowMapper extends BaseMapper<Follow> {

}
