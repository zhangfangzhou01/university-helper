package com.yhm.universityhelper.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yhm.universityhelper.entity.po.Task;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
public class TaskQueryWrapper {
    private final LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();

    public LambdaQueryWrapper<Task> selectByUserRelease(Long userId) {
        wrapper.eq(Task::getUserId, userId);
        return wrapper;
    }

    public LambdaQueryWrapper<Task> selectByUserTake(Long userId) {
        return null;
    }

    public LambdaQueryWrapper<Task> selectAllType() {
        return null;
    }

    public LambdaQueryWrapper<Task> selectByType(String type) {
        return null;
    }

    public LambdaQueryWrapper<Task> selectReleaseTimeMax(LocalDateTime releaseTimeMax) {
        return null;
    }

    public LambdaQueryWrapper<Task> selectReleaseTimeMin(LocalDateTime releaseTimeMin) {
        return null;
    }

    public LambdaQueryWrapper<Task> selectReleaseTimeToday() {
        return null;
    }

    public LambdaQueryWrapper<Task> selectOrderByPriority() {
        return null;
    }

    public LambdaQueryWrapper<Task> selectOrderByPriorityDesc() {
        return null;
    }

    public LambdaQueryWrapper<Task> selectByMaxNumOfPeople(Integer maxNumOfPeople) {
        return null;
    }

    public LambdaQueryWrapper<Task> selectByTaskState(Integer taskState) {
        return null;
    }

    public LambdaQueryWrapper<Task> selectOrderByArrivalTime() {
        return null;
    }

    public LambdaQueryWrapper<Task> selectArrivalTimeMax(LocalDateTime arrivalTimeMax) {
        return null;
    }

    public LambdaQueryWrapper<Task> selectArrivalTimeMin(LocalDateTime arrivalTimeMin) {
        return null;
    }

    public LambdaQueryWrapper<Task> selectByArrivalLocation(String arrivalLocation) {
        return null;
    }

    public LambdaQueryWrapper<Task> selectByTargetLocation(String targetLocation) {
        return null;
    }

    public LambdaQueryWrapper<Task> selectTransactionAmountMax(Integer transactionAmountMax) {
        return null;
    }

    public LambdaQueryWrapper<Task> selectTransactionAmountMin(Integer transactionAmountMin) {
        return null;
    }
}
