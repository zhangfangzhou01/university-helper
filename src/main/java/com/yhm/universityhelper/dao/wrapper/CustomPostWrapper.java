package com.yhm.universityhelper.dao.wrapper;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.yhm.universityhelper.entity.po.Post;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Component
@Scope("prototype")
public class CustomPostWrapper extends CustomWrapper {
    private final QueryWrapper<Post> queryWrapper = new QueryWrapper<>();

    public static OrderItem prioritySort() {
        return OrderItem.desc("log10(5 * uh_post.commentNum + 5 * uh_post.likeNum + 2 * uh_post.starNum + 10) " +
                "+ (unix_timestamp(uh_post.releaseTime) - unix_timestamp(now())) / (1000 * 3600 * 24)");
    }

    public LambdaQueryWrapper<Post> getLambdaQueryWrapper() {
        return queryWrapper.lambda();
    }

    public CustomPostWrapper postId(Long postId) {
        queryWrapper.eq("postId", postId);
        return this;
    }

    public CustomPostWrapper userId(Long userId) {
        queryWrapper.eq("userId", userId);
        return this;
    }

    public CustomPostWrapper title(String title) {
        queryWrapper.apply("(@titleMatchingDegree := " + fuzzyQuery("title", title) + ")").apply("@titleMatchingDegree > 0").orderByDesc("@titleMatchingDegree");
        return this;
    }

    public CustomPostWrapper content(String content) {
        queryWrapper.apply("(@contentMatchingDegree := " + fuzzyQuery("content", content) + ")").apply("@contentMatchingDegree > 0").orderByDesc("@contentMatchingDegree");
        return this;
    }

    public CustomPostWrapper tags(JSONArray tags) {
        for (Object tag : tags) {
            queryWrapper.like("tags", tag.toString());
        }
        return this;
    }

    public CustomPostWrapper releaseTime(LocalDateTime releaseTime) {
        queryWrapper.eq("releaseTime", releaseTime);
        return this;
    }

    public CustomPostWrapper releaseDate(LocalDateTime releaseDate) {
        queryWrapper.apply("date(releaseTime) = date('" + releaseDate + "')");
        return this;
    }

    public CustomPostWrapper lastModifyTime(String lastModifyTime) {
        queryWrapper.eq("lastModifyTime", lastModifyTime);
        return this;
    }

    public CustomPostWrapper lastModifyDate(String lastModifyDate) {
        queryWrapper.apply("date(lastModifyTime) = date('" + lastModifyDate + "')");
        return this;
    }

    public CustomPostWrapper likeNum(Long likeNum) {
        queryWrapper.eq("likeNum", likeNum);
        return this;
    }

    public CustomPostWrapper likeNumMin(Long likeNumMin) {
        queryWrapper.ge("likeNum", likeNumMin);
        return this;
    }

    public CustomPostWrapper likeNumMax(Long likeNumMax) {
        queryWrapper.le("likeNum", likeNumMax);
        return this;
    }

    public CustomPostWrapper starNum(Long starNum) {
        queryWrapper.eq("starNum", starNum);
        return this;
    }

    public CustomPostWrapper starNumMin(Long starNumMin) {
        queryWrapper.ge("starNum", starNumMin);
        return this;
    }

    public CustomPostWrapper starNumMax(Long starNumMax) {
        queryWrapper.le("starNum", starNumMax);
        return this;
    }

    public CustomPostWrapper commentNum(Long commentNum) {
        queryWrapper.eq("commentNum", commentNum);
        return this;
    }

    public CustomPostWrapper commentNumMin(Long commentNumMin) {
        queryWrapper.ge("commentNum", commentNumMin);
        return this;
    }

    public CustomPostWrapper commentNumMax(Long commentNumMax) {
        queryWrapper.le("commentNum", commentNumMax);
        return this;
    }
}
