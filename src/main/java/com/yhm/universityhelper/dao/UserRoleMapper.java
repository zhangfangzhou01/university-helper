package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.entity.po.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhm
 * @since 2023-02-26
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Select("select * from uh_user_role where userId = #{userId}")
    @NotNull
    List<UserRole> listByUserId(Integer userId);
}
