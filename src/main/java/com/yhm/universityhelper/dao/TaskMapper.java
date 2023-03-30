package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.entity.po.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yhm
 * @since 2023-03-04
 */

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    @Select("select userId from task where taskId = #{taskId}")
    Long selectUserIdByTaskId(Long taskId);
}
