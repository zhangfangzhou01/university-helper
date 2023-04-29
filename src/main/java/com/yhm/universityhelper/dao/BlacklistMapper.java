package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.config.RedisCache;
import com.yhm.universityhelper.entity.po.Blacklist;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */

// 一天后过期
@CacheNamespace(implementation = RedisCache.class, flushInterval = 86400000L)
public interface BlacklistMapper extends BaseMapper<Blacklist> {

}
