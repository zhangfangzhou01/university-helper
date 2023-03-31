package com.yhm.universityhelper.dao.wrapper;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.extra.tokenizer.Word;
import cn.hutool.extra.tokenizer.engine.jieba.JiebaEngine;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.yhm.universityhelper.dao.TaskMapper;
import com.yhm.universityhelper.dao.UsertaketaskMapper;
import com.yhm.universityhelper.entity.po.Task;
import com.yhm.universityhelper.entity.po.Usertaketask;
import com.yhm.universityhelper.util.BeanUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Data
@Component
@Scope("prototype")
public class TaskWrapper {
    public final static String[] FUZZY_SEARCH_COLUMNS = {"title", "requireDescription", "arrivalLocation", "targetLocation"};
    private final static List<String> STOP_WORDS = Arrays.asList(new FileReader("static/stopwords.txt").readString().split("\n"));
    private final static JiebaEngine JIEBA = new JiebaEngine();
    private final QueryWrapper<Task> queryWrapper = new QueryWrapper<>();

    public LambdaQueryWrapper<Task> getLambdaQueryWrapper() {
        return queryWrapper.lambda();
    }

    @Autowired
    private UsertaketaskMapper usertaketaskMapper;

    public static OrderItem prioritySort() {
        final List<Map<String, Object>> maps = BeanUtils.getBean(TaskMapper.class).selectPriorityRelated();

        long releaseTimeMax = ((Timestamp)maps.get(0).get("releaseTimeMax")).toLocalDateTime().toEpochSecond(ZoneOffset.of("+8"));
        long releaseTimeMin = ((Timestamp)maps.get(0).get("releaseTimeMin")).toLocalDateTime().toEpochSecond(ZoneOffset.of("+8"));
        int leftNumOfPeopleTakeMax = (int)maps.get(0).get("leftNumOfPeopleTakeMax");
        int leftNumOfPeopleTakeMin = (int)maps.get(0).get("leftNumOfPeopleTakeMin");
        int expectedPeriodMax = (int)maps.get(0).get("expectedPeriodMax");
        int expectedPeriodMin = (int)maps.get(0).get("expectedPeriodMin");

        long arrivalTimeMax = ((Timestamp)maps.get(0).get("arrivalTimeMax")).toLocalDateTime().toEpochSecond(ZoneOffset.of("+8"));
        long arrivalTimeMin = ((Timestamp)maps.get(0).get("arrivalTimeMin")).toLocalDateTime().toEpochSecond(ZoneOffset.of("+8"));
        double transactionAmountMax = (double)maps.get(1).get("transactionAmountMax");
        double transactionAmountMin = (double)maps.get(1).get("transactionAmountMin");

        return OrderItem.desc("(releaseTime - " + releaseTimeMin + ") / (" + releaseTimeMax + " - " + releaseTimeMin + " + 1) + " +
                "(expectedPeriod - " + expectedPeriodMin + ") / (" + expectedPeriodMax + " - " + expectedPeriodMin + " + 1) + " +
                "(leftNumOfPeopleTake - " + leftNumOfPeopleTakeMin + ") / (" + leftNumOfPeopleTakeMax + " - " + leftNumOfPeopleTakeMin + " + 1) " +
                "+ " +
                "(case type " +
                "when '外卖' then " +
                "(arrivalTime - " + arrivalTimeMin + ") / (" + arrivalTimeMax + " - " + arrivalTimeMin + " + 1) " +
                "when '交易' then " +
                "(transactionAmount - " + transactionAmountMin + ") / (" + transactionAmountMax + " - " + transactionAmountMin + " + 1) " +
                "end) ");
    }

    public static String fuzzyQueryString(String field, String keyword) {
        return StreamSupport
                .stream(JIEBA.parse(keyword).spliterator(), true)
                .map(Word::getText)
                .filter(token -> !STOP_WORDS.contains(token))
                .map(token -> "(case when " + field + " like \"%" + token + "%\" then 1 else 0 end)")
                .collect(Collectors.joining(" + "));
    }


    public TaskWrapper userRelease(Long userId) {
        queryWrapper.eq("userId", userId);
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
            queryWrapper.eq("taskId", -1);
        } else {
            queryWrapper.in("taskId", taskIds);
        }
        return this;
    }

    public TaskWrapper type(String type) {
        queryWrapper.eq("type", type);
        return this;
    }

    public TaskWrapper releaseTimeMax(LocalDateTime releaseTimeMax) {
        queryWrapper.le("releaseTime", releaseTimeMax);
        return this;
    }

    public TaskWrapper releaseTimeMin(LocalDateTime releaseTimeMin) {
        queryWrapper.ge("releaseTime", releaseTimeMin);
        return this;
    }

    public TaskWrapper releaseTime(LocalDateTime releaseTime) {
        queryWrapper.eq("releaseTime", releaseTime);
        return this;
    }

    public TaskWrapper releaseDate(LocalDate releaseTime) {
        queryWrapper.ge("releaseTime", releaseTime.atStartOfDay())
                .le("releaseTime", releaseTime.plusDays(1).atStartOfDay());
        return this;
    }

    public TaskWrapper maxNumOfPeopleTake(Integer maxNumOfPeopleTake) {
        queryWrapper.le("maxNumOfPeopleTake", maxNumOfPeopleTake);
        return this;
    }

    public TaskWrapper taskState(Integer taskState) {
        queryWrapper.eq("taskState", taskState);
        return this;
    }

    public TaskWrapper arrivalTimeMax(LocalDateTime arrivalTimeMax) {
        queryWrapper.le("arrivalTime", arrivalTimeMax);
        return this;
    }

    public TaskWrapper arrivalTimeMin(LocalDateTime arrivalTimeMin) {
        queryWrapper.ge("arrivalTime", arrivalTimeMin);
        return this;
    }

    public TaskWrapper arrivalLocation(String arrivalLocation) {
        queryWrapper.orderByDesc(fuzzyQueryString("arrivalLocation", arrivalLocation));
        return this;
    }

    public TaskWrapper targetLocation(String targetLocation) {
        queryWrapper.orderByDesc(fuzzyQueryString("targetLocation", targetLocation));
        return this;
    }

    public TaskWrapper transactionAmountMax(Integer transactionAmountMax) {
        queryWrapper.le("transactionAmount", transactionAmountMax);
        return this;
    }

    public TaskWrapper transactionAmountMin(Integer transactionAmountMin) {
        queryWrapper.ge("transactionAmount", transactionAmountMin);
        return this;
    }

    public TaskWrapper taskId(Long taskId) {
        queryWrapper.eq("taskId", taskId);
        return this;
    }

    public TaskWrapper userId(Long userId) {
        queryWrapper.eq("userId", userId);
        return this;
    }

    public TaskWrapper tags(JSONArray tags) {
        for (Object tag : tags) {
            queryWrapper.like("tags", tag);
        }
        return this;
    }

    public TaskWrapper priority(Integer priority) {
        queryWrapper.eq("priority", priority);
        return this;
    }

    public TaskWrapper title(String title) {
        queryWrapper.orderByDesc(fuzzyQueryString("title", title));
        return this;
    }

    public TaskWrapper requireDescription(String requireDescription) {
        queryWrapper.orderByDesc(fuzzyQueryString("requireDescription", requireDescription));
        return this;
    }

    public TaskWrapper score(Integer score) {
        queryWrapper.eq("score", score);
        return this;
    }


    public TaskWrapper takeoutId(Integer takeoutId) {
        queryWrapper.eq("takeoutId", takeoutId);
        return this;
    }

    public TaskWrapper orderTime(LocalDateTime orderTime) {
        queryWrapper.eq("orderTime", orderTime);
        return this;
    }

    public TaskWrapper arrivalTime(LocalDateTime arrivalTime) {
        queryWrapper.eq("arrivalTime", arrivalTime);
        return this;
    }

    public TaskWrapper distance(Integer distance) {
        queryWrapper.eq("distance", distance);
        return this;
    }

    public TaskWrapper phoneNumForNow(Integer phoneNumForNow) {
        queryWrapper.eq("phoneNumForNow", phoneNumForNow);
        return this;
    }

    public TaskWrapper transactionAmount(Double transactionAmount) {
        queryWrapper.eq("transactionAmount", transactionAmount);
        return this;
    }

    public TaskWrapper expectedPeriod(Integer expectedPeriod) {
        queryWrapper.eq("expectedPeriod", expectedPeriod);
        return this;
    }

    public TaskWrapper isHunter(Integer isHunter) {
        queryWrapper.eq("isHunter", isHunter);
        return this;
    }

    public TaskWrapper leftNumOfPeopleTake(Integer leftNumOfPeopleTake) {
        queryWrapper.eq("leftNumOfPeopleTake", leftNumOfPeopleTake);
        return this;
    }
}
