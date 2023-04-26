package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.config.RedisCache;
import com.yhm.universityhelper.entity.po.VisitHistory;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 * 用户访问post的历史记录 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */

@CacheNamespace(implementation = RedisCache.class, flushInterval = 204000)
public interface VisitHistoryMapper extends BaseMapper<VisitHistory> {

}
