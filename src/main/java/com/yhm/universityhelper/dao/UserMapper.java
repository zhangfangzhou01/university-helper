package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.config.RedisCache;
import com.yhm.universityhelper.entity.po.User;
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

@CacheNamespace(implementation = RedisCache.class, flushInterval = 86400000L)
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from universityhelper.uh_user where username = #{username}")
    User selectByUsername(String username);

    @Select("select * from universityhelper.uh_user_role where userId = #{userId}")
    List<UserRole> listByUserId(Long userId);

    // 由于username和userId是唯一的，所以可以用username或者userId来查询
    @Select("select username from universityhelper.uh_user where userId = #{userId}")
    String selectUsernameByUserId(Long userId);
    
    @Select("select username from universityhelper.uh_user where userId in (#{userIdList})")
    List<String> selectBatchUsernameByBatchUserId(List<Long> userIdList);

    @Select("select userId from universityhelper.uh_user where username = #{username}")
    Long selectUserIdByUsername(String username);
    
    @Select("select userId from universityhelper.uh_user where email = #{email}")
    Long selectUserIdByEmail(String email);
    
    List<String> selectFollowedList(Long userId);
    
    List<String> selectFollowerList(Long userId);
    
    Long selectFollowedCount(Long userId);
    
    Long selectFollowerCount(Long userId);
    
    List<String> selectBlockedList(Long userId);
    
    Long selectBlockedCount(Long userId);
}
