package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.config.RedisCache;
import com.yhm.universityhelper.entity.po.WebscoketOnline;
import org.apache.ibatis.annotations.CacheNamespace;

@CacheNamespace(implementation = RedisCache.class, flushInterval = 86400000L)

public interface WebsocketOnlineMapper extends BaseMapper<WebscoketOnline> {
    void insertOrUpdate(WebscoketOnline webscoketOnline);
}
