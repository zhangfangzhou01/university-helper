package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.entity.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhm
 * @since 2023-02-26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from uh_user where username = #{username}")
    @NotNull
    User selectByUsername(String username);
}
