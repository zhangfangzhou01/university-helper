package com.yhm.universityhelper.validation;

import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONObject;

import java.util.Optional;

public class ForumValidator extends CustomValidator {

    public static void insertPost(JSONObject post) {
        Optional.ofNullable(post.getLong("userId"))
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));

        Optional.ofNullable(post.getStr("title"))
                .map(title -> Validator.validateMatchRegex(".{1,255}", title, "标题长度必须在1-255之间"))
                .orElseThrow(() -> new IllegalArgumentException("必须指定title"));

        Optional.ofNullable(post.getStr("tags"))
                .map(tags -> Validator.validateMatchRegex(JSON_ARRAY_REGEX, tags, "标签必须是JSON数组"))
                .map(tags -> Validator.validateMatchRegex(".{1,255}", tags, "标签长度必须在1-255之间"))
                .orElseThrow(() -> new IllegalArgumentException("必须指定tags"));

        Optional.ofNullable(post.getStr("content"))
                .map(content -> Validator.validateMatchRegex(".{1,65535}", content, "内容长度必须在1-65535之间"))
                .orElseThrow(() -> new IllegalArgumentException("必须指定content"));

        Validator.validateNull(post.get("postId"), "postId默认为自增，不需要指定");
        Validator.validateNull(post.get("releaseTime"), "releaseTime默认为当前时间，不需要指定");
        Validator.validateNull(post.get("lastModifyTime"), "lastModifyTime默认为当前时间，不需要指定");
        Validator.validateNull(post.get("likeNum"), "likeNum默认为0，不需要指定");
        Validator.validateNull(post.get("starNum"), "starNum默认为0，不需要指定");
        Validator.validateNull(post.get("commentNum"), "commentNum默认为0，不需要指定");
    }

    public static void deletePost(Long userId, Long postId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));

        Optional.ofNullable(postId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定postId"));
    }

    public static void updatePost(JSONObject post) {
        Optional.ofNullable(post.getLong("userId"))
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));

        Optional.ofNullable(post.getStr("title"))
                .map(title -> Validator.validateMatchRegex(".{1,255}", title, "标题长度必须在1-255之间"));

        Optional.ofNullable(post.getStr("tags"))
                .map(tags -> Validator.validateMatchRegex(JSON_ARRAY_REGEX, tags, "标签必须是JSON数组"))
                .map(tags -> Validator.validateMatchRegex(".{1,255}", tags, "标签长度必须在1-255之间"));

        Optional.ofNullable(post.getStr("content"))
                .map(content -> Validator.validateMatchRegex(".{1,65535}", content, "内容长度必须在1-65535之间"));

        Validator.validateNull(post.get("postId"), "postId默认为自增，不需要指定");
        Validator.validateNull(post.get("releaseTime"), "releaseTime默认为当前时间，不需要指定");
        Validator.validateNull(post.get("lastModifyTime"), "lastModifyTime默认为当前时间，不需要指定");
        Validator.validateNull(post.get("likeNum"), "likeNum不能修改，不需要指定");
        Validator.validateNull(post.get("starNum"), "starNum不能修改，不需要指定");
        Validator.validateNull(post.get("commentNum"), "commentNum不能修改，不需要指定");
    }

    public static void insertComment(JSONObject comment) {
        Optional.ofNullable(comment.getLong("userId"))
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));

        Optional.ofNullable(comment.getLong("postId"))
                .orElseThrow(() -> new IllegalArgumentException("必须指定postId"));

        Optional.ofNullable(comment.getStr("content"))
                .map(content -> Validator.validateMatchRegex(".{1,65535}", content, "内容长度必须在1-65535之间"))
                .orElseThrow(() -> new IllegalArgumentException("必须指定content"));

        Validator.validateNull(comment.get("commentId"), "commentId默认为自增，不需要指定");
        Validator.validateNull(comment.get("releaseTime"), "releaseTime默认为当前时间，不需要指定");
        Validator.validateNull(comment.get("likeNum"), "likeNum默认为0，不需要指定");
        Validator.validateNull(comment.get("replayCommentId"), "replayCommentId默认为0，不需要指定");
    }

    public static void deleteComment(Long userId, Long commentId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));

        Optional.ofNullable(commentId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定commentId"));
    }

    public static void selectCommentByUserId(Long userId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));
    }

    public static void selectCommentByPostId(Long userId, Long postId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));

        Optional.ofNullable(postId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定postId"));
    }

    public static void selectReplyByUserId(Long userId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));
    }

    public static void likeComment(Long userId, Long commentId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));

        Optional.ofNullable(commentId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定commentId"));
    }

    public static void likePost(Long userId, Long postId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));

        Optional.ofNullable(postId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定postId"));
    }

    public static void insertStar(Long userId, Long postId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));

        Optional.ofNullable(postId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定postId"));
    }

    public static void deleteStar(Long userId, Long postId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));

        Optional.ofNullable(postId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定postId"));
    }

    public static void selectStar(Long userId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));
    }

    public static void insertHistory(Long userId, Long postId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));

        Optional.ofNullable(postId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定postId"));
    }

    public static void deleteHistory(Long userId, Long postId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));

        Optional.ofNullable(postId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定postId"));
    }

    public static void selectHistory(Long userId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new IllegalArgumentException("必须指定userId"));
    }
}