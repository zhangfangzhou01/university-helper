package com.yhm.universityhelper.dao.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yhm.universityhelper.dao.UsertaketaskMapper;
import com.yhm.universityhelper.entity.po.Task;
import com.yhm.universityhelper.entity.po.Usertaketask;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Data
@Component
@Scope("prototype")
public class TaskQueryWrapper {
    @Autowired
    private UsertaketaskMapper usertaketaskMapper;

    private final LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();

    public void taskId(Long taskId) {
        wrapper.eq(Task::getTaskId, taskId);
    }

    public void userRelease(Long userId) {
        wrapper.eq(Task::getUserId, userId);
    }

    public void userTake(Long userId) {
        wrapper.in(Task::getTaskId, usertaketaskMapper.selectList(
                        new LambdaQueryWrapper<Usertaketask>()
                                .eq(Usertaketask::getUserId, userId)
                )
                .stream()
                .map(Usertaketask::getTaskId)
                .collect(Collectors.toList()));
    }

    public void type(String type) {
        wrapper.eq(Task::getType, type);
    }

    public void releaseTimeMax(LocalDateTime releaseTimeMax) {
        wrapper.le(Task::getReleaseTime, releaseTimeMax);
    }

    public void releaseTimeMin(LocalDateTime releaseTimeMin) {
        wrapper.ge(Task::getReleaseTime, releaseTimeMin);
    }

    public void releaseTime(LocalDateTime releaseTime) {
        wrapper.eq(Task::getReleaseTime, releaseTime);
    }

    public void releaseDate(LocalDate releaseTime) {
        wrapper.ge(Task::getReleaseTime, releaseTime.atStartOfDay())
                .le(Task::getReleaseTime, releaseTime.plusDays(1).atStartOfDay());
    }

    public void maxNumOfPeopleTake(Integer maxNumOfPeopleTake) {
        wrapper.le(Task::getMaxNumOfPeopleTake, maxNumOfPeopleTake);
    }

    public void taskState(Integer taskState) {
        wrapper.eq(Task::getTaskState, taskState);
    }

    public void arrivalTimeMax(LocalDateTime arrivalTimeMax) {
        wrapper.le(Task::getArrivalTime, arrivalTimeMax);
    }

    public void arrivalTimeMin(LocalDateTime arrivalTimeMin) {
        wrapper.ge(Task::getArrivalTime, arrivalTimeMin);
    }

    public void arrivalLocation(String arrivalLocation) {
        wrapper.like(Task::getArrivalLocation, arrivalLocation);
    }

    public void targetLocation(String targetLocation) {
        wrapper.like(Task::getTargetLocation, targetLocation);
    }

    public void transactionAmountMax(Integer transactionAmountMax) {
        wrapper.le(Task::getTransactionAmount, transactionAmountMax);
    }

    public void transactionAmountMin(Integer transactionAmountMin) {
        wrapper.ge(Task::getTransactionAmount, transactionAmountMin);
    }
}
