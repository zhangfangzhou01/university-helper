package com.yhm.universityhelper.dao.wrapper;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.extra.tokenizer.Word;
import cn.hutool.extra.tokenizer.engine.jieba.JiebaEngine;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.yhm.universityhelper.dao.UsertaketaskMapper;
import com.yhm.universityhelper.entity.po.Task;
import com.yhm.universityhelper.entity.po.Usertaketask;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Data
@Component
@Scope("prototype")
public class TaskQueryWrapper {
    private final static List<String> STOP_WORDS = Arrays.asList(new FileReader("static/stopwords.txt").readString().split("\n"));
    private final static JiebaEngine JIEBA = new JiebaEngine();
    private final LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
    @Autowired
    private UsertaketaskMapper usertaketaskMapper;

    public static OrderItem fuzzyQuery(String field, String keyword) {
        return StringUtils.isNotEmpty(keyword)
                ? OrderItem.desc(
                StreamSupport
                        .stream(JIEBA.parse(keyword).spliterator(), true)
                        .map(Word::getText)
                        .filter(token -> !STOP_WORDS.contains(token))
                        .map(token -> "(case when " + field + " like '%" + token + "%' then 1 else 0 end)")
                        .collect(Collectors.joining(" + ")))
                : new OrderItem();
    }


    public TaskQueryWrapper userRelease(Long userId) {
        wrapper.eq(Task::getUserId, userId);
        return this;
    }

    public TaskQueryWrapper userTake(Long userId) {
        final List<Long> taskIds = usertaketaskMapper.selectList(
                        new LambdaQueryWrapper<Usertaketask>()
                                .eq(Usertaketask::getUserId, userId)
                )
                .stream()
                .map(Usertaketask::getTaskId)
                .collect(Collectors.toList());

        if (taskIds.isEmpty()) {
            wrapper.eq(Task::getTaskId, -1);
        } else {
            wrapper.in(Task::getTaskId, taskIds);
        }
        return this;
    }

    public TaskQueryWrapper type(String type) {
        wrapper.eq(Task::getType, type);
        return this;
    }

    public TaskQueryWrapper releaseTimeMax(LocalDateTime releaseTimeMax) {
        wrapper.le(Task::getReleaseTime, releaseTimeMax);
        return this;
    }

    public TaskQueryWrapper releaseTimeMin(LocalDateTime releaseTimeMin) {
        wrapper.ge(Task::getReleaseTime, releaseTimeMin);
        return this;
    }

    public TaskQueryWrapper releaseTime(LocalDateTime releaseTime) {
        wrapper.eq(Task::getReleaseTime, releaseTime);
        return this;
    }

    public TaskQueryWrapper releaseDate(LocalDate releaseTime) {
        wrapper.ge(Task::getReleaseTime, releaseTime.atStartOfDay())
                .le(Task::getReleaseTime, releaseTime.plusDays(1).atStartOfDay());
        return this;
    }

    public TaskQueryWrapper maxNumOfPeopleTake(Integer maxNumOfPeopleTake) {
        wrapper.le(Task::getMaxNumOfPeopleTake, maxNumOfPeopleTake);
        return this;
    }

    public TaskQueryWrapper taskState(Integer taskState) {
        wrapper.eq(Task::getTaskState, taskState);
        return this;
    }

    public TaskQueryWrapper arrivalTimeMax(LocalDateTime arrivalTimeMax) {
        wrapper.le(Task::getArrivalTime, arrivalTimeMax);
        return this;
    }

    public TaskQueryWrapper arrivalTimeMin(LocalDateTime arrivalTimeMin) {
        wrapper.ge(Task::getArrivalTime, arrivalTimeMin);
        return this;
    }

    public TaskQueryWrapper arrivalLocation(String arrivalLocation) {
        return this;
    }

    public TaskQueryWrapper targetLocation(String targetLocation) {
        return this;
    }

    public TaskQueryWrapper transactionAmountMax(Integer transactionAmountMax) {
        wrapper.le(Task::getTransactionAmount, transactionAmountMax);
        return this;
    }

    public TaskQueryWrapper transactionAmountMin(Integer transactionAmountMin) {
        wrapper.ge(Task::getTransactionAmount, transactionAmountMin);
        return this;
    }

    public TaskQueryWrapper taskId(Long taskId) {
        wrapper.eq(Task::getTaskId, taskId);
        return this;
    }

    public TaskQueryWrapper userId(Long userId) {
        wrapper.eq(Task::getUserId, userId);
        return this;
    }

    public TaskQueryWrapper tags(JSONArray tags) {
        for (Object tag : tags) {
            wrapper.like(Task::getTags, tag);
        }
        return this;
    }

    public TaskQueryWrapper priority(Integer priority) {
        wrapper.eq(Task::getPriority, priority);
        return this;
    }

    public TaskQueryWrapper title(String title) {
        return this;
    }

    public TaskQueryWrapper requireDescription(String requireDescription) {
        return this;
    }

    public TaskQueryWrapper score(Integer score) {
        wrapper.eq(Task::getScore, score);
        return this;
    }


    public TaskQueryWrapper takeoutId(Integer takeoutId) {
        wrapper.eq(Task::getTakeoutId, takeoutId);
        return this;
    }

    public TaskQueryWrapper orderTime(LocalDateTime orderTime) {
        wrapper.eq(Task::getOrderTime, orderTime);
        return this;
    }

    public TaskQueryWrapper arrivalTime(LocalDateTime arrivalTime) {
        wrapper.eq(Task::getArrivalTime, arrivalTime);
        return this;
    }

    public TaskQueryWrapper distance(Integer distance) {
        wrapper.eq(Task::getDistance, distance);
        return this;
    }

    public TaskQueryWrapper phoneNumForNow(Integer phoneNumForNow) {
        wrapper.eq(Task::getPhoneNumForNow, phoneNumForNow);
        return this;
    }

    public TaskQueryWrapper transactionAmount(Double transactionAmount) {
        wrapper.eq(Task::getTransactionAmount, transactionAmount);
        return this;
    }

    public TaskQueryWrapper expectedPeriod(Integer expectedPeriod) {
        wrapper.eq(Task::getExpectedPeriod, expectedPeriod);
        return this;
    }

    public TaskQueryWrapper isHunter(Integer isHunter) {
        wrapper.eq(Task::getIsHunter, isHunter);
        return this;
    }

    public TaskQueryWrapper leftNumOfPeopleTake(Integer leftNumOfPeopleTake) {
        wrapper.eq(Task::getLeftNumOfPeopleTake, leftNumOfPeopleTake);
        return this;
    }
}
