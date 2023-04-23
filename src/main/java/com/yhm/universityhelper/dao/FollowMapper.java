package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.entity.po.Follow;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * follower 关注 followed Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */

@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

}
