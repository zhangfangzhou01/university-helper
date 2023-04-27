package com.yhm.universityhelper.dao.wrapper;

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
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@Component
@Scope("prototype")
public class CustomTaskWrapper extends CustomWrapper {
    public final static String[] FUZZY_SEARCH_COLUMNS = {"title", "requireDescription", "arrivalLocation", "targetLocation"};
    private final QueryWrapper<Task> queryWrapper = new QueryWrapper<>();

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

        long arrivalTimeMax, arrivalTimeMin;
        double transactionAmountMax, transactionAmountMin;
        try {
            arrivalTimeMax = ((Timestamp)maps.get(0).get("arrivalTimeMax")).toLocalDateTime().toEpochSecond(ZoneOffset.of("+8"));
            arrivalTimeMin = ((Timestamp)maps.get(0).get("arrivalTimeMin")).toLocalDateTime().toEpochSecond(ZoneOffset.of("+8"));
            transactionAmountMax = (double)maps.get(1).get("transactionAmountMax");
            transactionAmountMin = (double)maps.get(1).get("transactionAmountMin");
        } catch (NullPointerException e) {
            arrivalTimeMax = ((Timestamp)maps.get(1).get("arrivalTimeMax")).toLocalDateTime().toEpochSecond(ZoneOffset.of("+8"));
            arrivalTimeMin = ((Timestamp)maps.get(1).get("arrivalTimeMin")).toLocalDateTime().toEpochSecond(ZoneOffset.of("+8"));
            transactionAmountMax = (double)maps.get(0).get("transactionAmountMax");
            transactionAmountMin = (double)maps.get(0).get("transactionAmountMin");
        }

        return OrderItem.desc("(releaseTime - " + releaseTimeMin + ") / (" + releaseTimeMax + " - " + releaseTimeMin + " + 1) + " + "(expectedPeriod - " + expectedPeriodMin + ") / (" + expectedPeriodMax + " - " + expectedPeriodMin + " + 1) + " + "(leftNumOfPeopleTake - " + leftNumOfPeopleTakeMin + ") / (" + leftNumOfPeopleTakeMax + " - " + leftNumOfPeopleTakeMin + " + 1) " + "+ " + "(case type " + "when '外卖' then " + "(arrivalTime - " + arrivalTimeMin + ") / (" + arrivalTimeMax + " - " + arrivalTimeMin + " + 1) " + "when '交易' then " + "(transactionAmount - " + transactionAmountMin + ") / (" + transactionAmountMax + " - " + transactionAmountMin + " + 1) " + "end) ");
    }

    public LambdaQueryWrapper<Task> getLambdaQueryWrapper() {
        return queryWrapper.lambda();
    }
    
    public CustomTaskWrapper userRelease(Long userId) {
        queryWrapper.eq("userId", userId);
        return this;
    }

    public CustomTaskWrapper userTake(Long userId) {
        final List<Long> taskIds = usertaketaskMapper.selectList(new LambdaQueryWrapper<Usertaketask>().eq(Usertaketask::getUserId, userId)).stream().map(Usertaketask::getTaskId).collect(Collectors.toList());

        if (taskIds.isEmpty()) {
            queryWrapper.eq("taskId", -1);
        } else {
            queryWrapper.in("taskId", taskIds);
        }
        return this;
    }

    public CustomTaskWrapper type(String type) {
        queryWrapper.eq("type", type);
        return this;
    }

    public CustomTaskWrapper releaseTimeMax(LocalDateTime releaseTimeMax) {
        queryWrapper.le("releaseTime", releaseTimeMax);
        return this;
    }

    public CustomTaskWrapper releaseTimeMin(LocalDateTime releaseTimeMin) {
        queryWrapper.ge("releaseTime", releaseTimeMin);
        return this;
    }

    public CustomTaskWrapper releaseTime(LocalDateTime releaseTime) {
        queryWrapper.eq("releaseTime", releaseTime);
        return this;
    }

    public CustomTaskWrapper releaseDate(LocalDate releaseTime) {
        queryWrapper.ge("releaseTime", releaseTime.atStartOfDay()).le("releaseTime", releaseTime.plusDays(1).atStartOfDay());
        return this;
    }

    public CustomTaskWrapper maxNumOfPeopleTake(Integer maxNumOfPeopleTake) {
        queryWrapper.le("maxNumOfPeopleTake", maxNumOfPeopleTake);
        return this;
    }

    public CustomTaskWrapper taskState(Integer taskState) {
        queryWrapper.eq("taskState", taskState);
        return this;
    }

    public CustomTaskWrapper arrivalTimeMax(LocalDateTime arrivalTimeMax) {
        queryWrapper.le("arrivalTime", arrivalTimeMax);
        return this;
    }

    public CustomTaskWrapper arrivalTimeMin(LocalDateTime arrivalTimeMin) {
        queryWrapper.ge("arrivalTime", arrivalTimeMin);
        return this;
    }

    public CustomTaskWrapper arrivalLocation(String arrivalLocation) {
        queryWrapper.apply("(@arrivalLocationMatchingDegree := " + fuzzyQuery("arrivalLocation", arrivalLocation) + ")").apply("@arrivalLocationMatchingDegree > 0");/*.orderByDesc("@arrivalLocationMatchingDegree");*/
        return this;
    }

    public CustomTaskWrapper targetLocation(String targetLocation) {
        queryWrapper.apply("(@targetLocationMatchingDegree := " + fuzzyQuery("targetLocation", targetLocation) + ")").apply("@targetLocationMatchingDegree > 0");/*.orderByDesc("@targetLocationMatchingDegree");*/
        return this;
    }

    public CustomTaskWrapper transactionAmountMax(Integer transactionAmountMax) {
        queryWrapper.le("transactionAmount", transactionAmountMax);
        return this;
    }

    public CustomTaskWrapper transactionAmountMin(Integer transactionAmountMin) {
        queryWrapper.ge("transactionAmount", transactionAmountMin);
        return this;
    }

    public CustomTaskWrapper taskId(Long taskId) {
        queryWrapper.eq("taskId", taskId);
        return this;
    }

    public CustomTaskWrapper userId(Long userId) {
        queryWrapper.eq("userId", userId);
        return this;
    }

    public CustomTaskWrapper tags(JSONArray tags) {
        for (Object tag : tags) {
            queryWrapper.like("tags", tag.toString());
        }
        return this;
    }

    public CustomTaskWrapper title(String title) {
        queryWrapper.apply("(@titleMatchingDegree := " + fuzzyQuery("title", title) + ")").apply("@titleMatchingDegree > 0");/*.orderByDesc("@titleMatchingDegree");*/
        return this;
    }

    public CustomTaskWrapper requireDescription(String requireDescription) {
        queryWrapper.apply("(@requireDescriptionMatchingDegree := " + fuzzyQuery("requireDescription", requireDescription) + ")").apply("@requireDescriptionMatchingDegree > 0");/*.orderByDesc("@requireDescriptionMatchingDegree");*/
        return this;
    }

    public CustomTaskWrapper score(Integer score) {
        queryWrapper.eq("score", score);
        return this;
    }


    public CustomTaskWrapper takeoutId(Integer takeoutId) {
        queryWrapper.eq("takeoutId", takeoutId);
        return this;
    }

    public CustomTaskWrapper orderTime(LocalDateTime orderTime) {
        queryWrapper.eq("orderTime", orderTime);
        return this;
    }

    public CustomTaskWrapper arrivalTime(LocalDateTime arrivalTime) {
        queryWrapper.eq("arrivalTime", arrivalTime);
        return this;
    }

    public CustomTaskWrapper distance(Integer distance) {
        queryWrapper.eq("distance", distance);
        return this;
    }

    public CustomTaskWrapper phoneNumForNow(Integer phoneNumForNow) {
        queryWrapper.eq("phoneNumForNow", phoneNumForNow);
        return this;
    }

    public CustomTaskWrapper transactionAmount(Double transactionAmount) {
        queryWrapper.eq("transactionAmount", transactionAmount);
        return this;
    }

    public CustomTaskWrapper expectedPeriod(Integer expectedPeriod) {
        queryWrapper.eq("expectedPeriod", expectedPeriod);
        return this;
    }

    public CustomTaskWrapper isHunter(Integer isHunter) {
        queryWrapper.eq("isHunter", isHunter);
        return this;
    }

    public CustomTaskWrapper leftNumOfPeopleTake(Integer leftNumOfPeopleTake) {
        queryWrapper.eq("leftNumOfPeopleTake", leftNumOfPeopleTake);
        return this;
    }
}
