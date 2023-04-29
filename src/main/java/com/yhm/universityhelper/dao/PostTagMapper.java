package com.yhm.universityhelper.dao;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.config.RedisCache;
import com.yhm.universityhelper.entity.po.PostTag;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */

@CacheNamespace(implementation = RedisCache.class, flushInterval = 86400000L)
public interface PostTagMapper extends BaseMapper<PostTag> {
    void insertBatch(JSONArray postTags);
}
