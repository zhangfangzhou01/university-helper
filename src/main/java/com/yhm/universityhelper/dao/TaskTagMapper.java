package com.yhm.universityhelper.dao;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.config.RedisCache;
import com.yhm.universityhelper.entity.po.TaskTag;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yhm
 * @since 2023-03-26
 */

@CacheNamespace(implementation = RedisCache.class, flushInterval = 86400000L)
public interface TaskTagMapper extends BaseMapper<TaskTag> {
    void insertBatch(JSONArray taskTags);
}
