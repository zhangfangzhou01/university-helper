package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.config.RedisCache;
import com.yhm.universityhelper.entity.po.UserRole;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yhm
 * @since 2023-02-26
 */

@CacheNamespace(implementation = RedisCache.class, flushInterval = 231000)
public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Select("select * from universityhelper.uh_user_role where userId = #{userId}")
    List<UserRole> listByUserId(Long userId);
}
