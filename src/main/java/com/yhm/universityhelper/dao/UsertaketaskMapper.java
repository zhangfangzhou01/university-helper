package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.config.MybatisRedisCache;
import com.yhm.universityhelper.entity.po.Usertaketask;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 * 任务与其接受者的表 Mapper 接口
 * </p>
 *
 * @author yhm
 * @since 2023-03-04
 */

@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface UsertaketaskMapper extends BaseMapper<Usertaketask> {
}
