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
public class TaskWrapper {
    public final static String[] FUZZY_QUERY_COLUMNS = {"requireDescription", "title", "arrivalLocation", "targetLocation"};
    private final static List<String> STOP_WORDS = Arrays.asList(new FileReader("static/stopwords.txt").readString().split("\n"));
    private final static JiebaEngine JIEBA = new JiebaEngine();
    private final LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
    @Autowired
    private UsertaketaskMapper usertaketaskMapper;

    public static OrderItem prioritySort(
            Integer releaseTimeMax,
            Integer releaseTimeMin,
            Integer expectedPeriodMax,
            Integer expectedPeriodMin,
            Integer leftNumOfPeopleTakeMax,
            Integer leftNumOfPeopleTakeMin,
            Integer arrivalTimeMin,
            Integer arrivalTimeMax,
            Integer transactionAmountMax,
            Integer transactionAmountMin
    ) {
        return OrderItem.desc("(case type " +
                "when '外卖' then " +
                "(releaseTime - " + arrivalTimeMin + ") / (" + arrivalTimeMax + " - " + arrivalTimeMin + " + 1) + " +
                "(expectedPeriod - " + expectedPeriodMin + ") / (" + expectedPeriodMax + " - " + expectedPeriodMin + " + 1) + " +
                "(leftNumOfPeopleTake - " + leftNumOfPeopleTakeMin + ") / (" + leftNumOfPeopleTakeMax + " - " + leftNumOfPeopleTakeMin + " + 1) " +
                "when '交易' then " +
                "(releaseTime - " + releaseTimeMin + ") / (" + releaseTimeMax + " - " + releaseTimeMin + " + 1) + " +
                "(expectedPeriod - " + expectedPeriodMin + ") / (" + expectedPeriodMax + " - " + expectedPeriodMin + " + 1) + " +
                "(leftNumOfPeopleTake - " + leftNumOfPeopleTakeMin + ") / (" + leftNumOfPeopleTakeMax + " - " + leftNumOfPeopleTakeMin + " + 1) + " +
                "(transactionAmount - " + transactionAmountMin + ") / (" + transactionAmountMax + " - " + transactionAmountMin + " + 1) " +
                "end) ");
    }

    public static OrderItem fuzzySearch(String field, String keyword) {
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


    public TaskWrapper userRelease(Long userId) {
        wrapper.eq(Task::getUserId, userId);
        return this;
    }

    public TaskWrapper userTake(Long userId) {
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

    public TaskWrapper type(String type) {
        wrapper.eq(Task::getType, type);
        return this;
    }

    public TaskWrapper releaseTimeMax(LocalDateTime releaseTimeMax) {
        wrapper.le(Task::getReleaseTime, releaseTimeMax);
        return this;
    }

    public TaskWrapper releaseTimeMin(LocalDateTime releaseTimeMin) {
        wrapper.ge(Task::getReleaseTime, releaseTimeMin);
        return this;
    }

    public TaskWrapper releaseTime(LocalDateTime releaseTime) {
        wrapper.eq(Task::getReleaseTime, releaseTime);
        return this;
    }

    public TaskWrapper releaseDate(LocalDate releaseTime) {
        wrapper.ge(Task::getReleaseTime, releaseTime.atStartOfDay())
                .le(Task::getReleaseTime, releaseTime.plusDays(1).atStartOfDay());
        return this;
    }

    public TaskWrapper maxNumOfPeopleTake(Integer maxNumOfPeopleTake) {
        wrapper.le(Task::getMaxNumOfPeopleTake, maxNumOfPeopleTake);
        return this;
    }

    public TaskWrapper taskState(Integer taskState) {
        wrapper.eq(Task::getTaskState, taskState);
        return this;
    }

    public TaskWrapper arrivalTimeMax(LocalDateTime arrivalTimeMax) {
        wrapper.le(Task::getArrivalTime, arrivalTimeMax);
        return this;
    }

    public TaskWrapper arrivalTimeMin(LocalDateTime arrivalTimeMin) {
        wrapper.ge(Task::getArrivalTime, arrivalTimeMin);
        return this;
    }

    public TaskWrapper arrivalLocation(String arrivalLocation) {
        return this;
    }

    public TaskWrapper targetLocation(String targetLocation) {
        return this;
    }

    public TaskWrapper transactionAmountMax(Integer transactionAmountMax) {
        wrapper.le(Task::getTransactionAmount, transactionAmountMax);
        return this;
    }

    public TaskWrapper transactionAmountMin(Integer transactionAmountMin) {
        wrapper.ge(Task::getTransactionAmount, transactionAmountMin);
        return this;
    }

    public TaskWrapper taskId(Long taskId) {
        wrapper.eq(Task::getTaskId, taskId);
        return this;
    }

    public TaskWrapper userId(Long userId) {
        wrapper.eq(Task::getUserId, userId);
        return this;
    }

    public TaskWrapper tags(JSONArray tags) {
        for (Object tag : tags) {
            wrapper.like(Task::getTags, tag);
        }
        return this;
    }

    public TaskWrapper priority(Integer priority) {
        wrapper.eq(Task::getPriority, priority);
        return this;
    }

    public TaskWrapper title(String title) {
        return this;
    }

    public TaskWrapper requireDescription(String requireDescription) {
        return this;
    }

    public TaskWrapper score(Integer score) {
        wrapper.eq(Task::getScore, score);
        return this;
    }


    public TaskWrapper takeoutId(Integer takeoutId) {
        wrapper.eq(Task::getTakeoutId, takeoutId);
        return this;
    }

    public TaskWrapper orderTime(LocalDateTime orderTime) {
        wrapper.eq(Task::getOrderTime, orderTime);
        return this;
    }

    public TaskWrapper arrivalTime(LocalDateTime arrivalTime) {
        wrapper.eq(Task::getArrivalTime, arrivalTime);
        return this;
    }

    public TaskWrapper distance(Integer distance) {
        wrapper.eq(Task::getDistance, distance);
        return this;
    }

    public TaskWrapper phoneNumForNow(Integer phoneNumForNow) {
        wrapper.eq(Task::getPhoneNumForNow, phoneNumForNow);
        return this;
    }

    public TaskWrapper transactionAmount(Double transactionAmount) {
        wrapper.eq(Task::getTransactionAmount, transactionAmount);
        return this;
    }

    public TaskWrapper expectedPeriod(Integer expectedPeriod) {
        wrapper.eq(Task::getExpectedPeriod, expectedPeriod);
        return this;
    }

    public TaskWrapper isHunter(Integer isHunter) {
        wrapper.eq(Task::getIsHunter, isHunter);
        return this;
    }

    public TaskWrapper leftNumOfPeopleTake(Integer leftNumOfPeopleTake) {
        wrapper.eq(Task::getLeftNumOfPeopleTake, leftNumOfPeopleTake);
        return this;
    }
}
