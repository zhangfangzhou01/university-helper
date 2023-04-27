package com.yhm.universityhelper.dao;

import com.github.yulichang.base.MPJBaseMapper;
import com.yhm.universityhelper.entity.po.Task;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yhm
 * @since 2023-03-04
 */

//@CacheNamespace(implementation = RedisCache.class)
public interface TaskMapper extends MPJBaseMapper<Task> {
    @Select("select userId from universityhelper.uh_task where taskId = #{taskId}")
    Long selectUserIdByTaskId(Long taskId);

    @Select("select " +
            "max(releaseTime) as releaseTimeMax, " +
            "min(releaseTime) as releaseTimeMin, " +
            "max(leftNumOfPeopleTake) as leftNumOfPeopleTakeMax, " +
            "min(leftNumOfPeopleTake) as leftNumOfPeopleTakeMin, " +
            "max(expectedPeriod) as expectedPeriodMax, " +
            "min(expectedPeriod) as expectedPeriodMin, " +
            "case when type = '外卖' then max(arrivalTime) end as arrivalTimeMax, " +
            "case when type = '外卖' then min(arrivalTime) end as arrivalTimeMin, " +
            "case when type = '交易' then max(transactionAmount) end as transactionAmountMax, " +
            "case when type = '交易' then min(transactionAmount) end as transactionAmountMin " +
            "from universityhelper.uh_task " +
            "group by type")
    List<Map<String, Object>> selectPriorityRelated();
}
