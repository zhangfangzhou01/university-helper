package com.yhm.universityhelper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhm.universityhelper.entity.po.Task;
import org.apache.ibatis.annotations.Select;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhm
 * @since 2023-03-04
 */
public interface TaskMapper extends BaseMapper<Task> {

//  \connect --mysql wsj@localhost/

    /**
     * @param userId
     * 选择给定用户所发布的所有任务
     * @return
     */
    @Select( "Select * from uh_task where userId = ${userId} " )
    @NotNull
    ArrayList<Task> selectByUserRelease(Integer userId);

    /**
     * @param userId
     * 选择给定用户所接取的所有任务
     * @return
     */
    @Select( "Select * from uh_task where taskId in ( Select taskId from uh_usertaketask where userId = ${userId} ) " )
    @NotNull
    ArrayList<Task> selectByUserTake(Integer userId);

    @Select( "Select * from uh_task" )
    @NotNull
    ArrayList<Task> selectAllType();

    @Select( "Select * from uh_task where ( select substr((tags), 1, 2) == #{type} ) " )
    @NotNull
    ArrayList<Task> selectByType(String type);

    @Select("Select * from uh_task where DateDiff( date(releaseTime), date(${releaseTimeMax}) )<0 )")
    @NotNull
    ArrayList<Task> selectReleaseTimeMax(LocalDateTime releaseTimeMax);

    @Select("Select * from uh_task where DateDiff( date(releaseTime), date(${releaseTimeMin}) )>0 )")
    @NotNull
    ArrayList<Task> selectReleaseTimeMin(LocalDateTime releaseTimeMin);


    /**
     * 选择今天发布的任务，（外卖类型经常调用）
     * @return
     */
    @Select("Select * from uh_task where DateDiff( date(releaseTime), date(now()) ) = 0 ")
    @NotNull
    ArrayList<Task> selectReleaseTimeToday();

    @Select("Select * from uh_task order by priority)")
    @NotNull
    ArrayList<Task> selectOrderByPriority();

    @Select("Select * from uh_task order by priority desc")
    @NotNull
    ArrayList<Task> selectOrderByPriorityDesc();

    @Select("Select * from uh_task where maxNumOfPeople = ${maxNumOfPeople}")
    @NotNull
    ArrayList<Task> selectByMaxNumOfPeople(Integer maxNumOfPeople);

    @Select( "Select * from uh_task where completeFlag = ${completeFlag} )" )
    @NotNull
    ArrayList<Task> selectByCompleteFlag(Integer completeFlag);

    // 外卖

    @Select( "Select * from uh_task order by arrivalTime" )
    @NotNull
    ArrayList<Task> selectOrderByArrivalTime();

    // arrivalTime
    @Select( "Select * from uh_task where DateDiff(time(arrivalTime), time(${arrivalTimeMax}) )<0" )
    @NotNull
    ArrayList<Task> selectArrivalTimeMax(LocalDateTime arrivalTimeMax);

    @Select( "Select * from uh_task where DateDiff(time(arrivalTime), time(${arrivalTimeMin}) )>0" )
    @NotNull
    ArrayList<Task> selectArrivalTimeMin(LocalDateTime arrivalTimeMin);

    // arrivalLocation
    @Select("Select * from uh_task where arrivalLocation = #{arrivalLocation}")
    @NotNull
    ArrayList<Task> selectByArrivalLocation(String arrivalLocation);

    // targetLocation
    @Select("Select * from uh_task where targetLocation = #{targetLocation}")
    @NotNull
    ArrayList<Task> selectByTargetLocation(String targetLocation);

    // 交易

    // transactionAmount
    @Select( "Select * from uh_task where transactionAmount < ${transactionAmountMax} ) " )
    @NotNull
    ArrayList<Task> selectTransactionAmountMax(Integer transactionAmountMax);

    @Select( "Select * from uh_task where transactionAmount > ${transactionAmountMin} ) " )
    @NotNull
    ArrayList<Task> selectTransactionAmountMin(Integer transactionAmountMin);


}
