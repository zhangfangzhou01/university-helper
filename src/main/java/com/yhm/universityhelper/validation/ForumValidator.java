package com.yhm.universityhelper.validation;

import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.yhm.universityhelper.util.SensitiveUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ForumValidator extends CustomValidator {

    public static void insertPost(JSONObject post) {
        Optional.ofNullable(post.getLong("userId")).orElseThrow(() -> new IllegalArgumentException("必须指定userId"));

        Optional.ofNullable(post.getStr("title")).map(title -> Validator.validateMatchRegex(".{1,255}", title, "title长度必须在1-255之间")).map(SensitiveUtils::getAllSensitive).map(sensitiveWords -> Validator.validateTrue(sensitiveWords.isEmpty(), "title中包含敏感词：" + sensitiveWords)).orElseThrow(() -> new IllegalArgumentException("必须指定title"));

        Optional.ofNullable(post.getJSONArray("tags")).map(JSONArray::toString).map(tags -> Validator.validateMatchRegex(JSON_ARRAY_REGEX, tags, "tags必须是JSON数组")).map(SensitiveUtils::getAllSensitive).map(sensitiveWords -> Validator.validateTrue(sensitiveWords.isEmpty(), "tags中包含敏感词：" + sensitiveWords)).orElseThrow(() -> new IllegalArgumentException("必须指定tags"));

        Optional.ofNullable(post.getStr("content")).map(content -> Validator.validateMatchRegex(".{1,65535}", content, "content长度必须在1-65535之间")).map(SensitiveUtils::getAllSensitive).map(sensitiveWords -> Validator.validateTrue(sensitiveWords.isEmpty(), "content中包含敏感词：" + sensitiveWords)).orElseThrow(() -> new IllegalArgumentException("必须指定content"));

        Validator.validateNull(post.get("postId"), "postId默认为自增，不需要指定");
        Validator.validateNull(post.get("releaseTime"), "releaseTime默认为当前时间，不需要指定");
        Validator.validateNull(post.get("lastModifyTime"), "lastModifyTime默认为当前时间，不需要指定");
        Validator.validateNull(post.get("likeNum"), "likeNum默认为0，不需要指定");
        Validator.validateNull(post.get("starNum"), "starNum默认为0，不需要指定");
        Validator.validateNull(post.get("commentNum"), "commentNum默认为0，不需要指定");
    }

    public static void updatePost(JSONObject post) {
        Optional.ofNullable(post.getLong("userId")).orElseThrow(() -> new IllegalArgumentException("必须指定userId"));

        Optional.ofNullable(post.getLong("postId")).orElseThrow(() -> new IllegalArgumentException("必须指定postId"));

        Optional.ofNullable(post.getStr("title")).map(title -> Validator.validateMatchRegex(".{1,255}", title, "title长度必须在1-255之间")).map(SensitiveUtils::getAllSensitive).map(sensitiveWords -> Validator.validateTrue(sensitiveWords.isEmpty(), "title中包含敏感词：" + sensitiveWords));

        Optional.ofNullable(post.getJSONArray("tags")).map(JSONArray::toString).map(tags -> Validator.validateMatchRegex(JSON_ARRAY_REGEX, tags, "tags必须是JSON数组")).map(SensitiveUtils::getAllSensitive).map(sensitiveWords -> Validator.validateTrue(sensitiveWords.isEmpty(), "标签中包含敏感词：" + sensitiveWords));

        Optional.ofNullable(post.getStr("content")).map(content -> Validator.validateMatchRegex(".{1,65535}", content, "content长度必须在1-65535之间")).map(SensitiveUtils::getAllSensitive).map(sensitiveWords -> Validator.validateTrue(sensitiveWords.isEmpty(), "content中包含敏感词：" + sensitiveWords));

        Validator.validateNull(post.get("releaseTime"), "releaseTime默认为当前时间，不需要指定");
        Validator.validateNull(post.get("lastModifyTime"), "lastModifyTime默认为当前时间，不需要指定");
        Validator.validateNull(post.get("likeNum"), "likeNum不能修改，不需要指定");
        Validator.validateNull(post.get("starNum"), "starNum不能修改，不需要指定");
        Validator.validateNull(post.get("commentNum"), "commentNum不能修改，不需要指定");
    }

    public static void insertComment(JSONObject comment) {
        Optional.ofNullable(comment.getLong("userId")).orElseThrow(() -> new IllegalArgumentException("必须指定userId"));

        Optional.ofNullable(comment.getLong("postId")).orElseThrow(() -> new IllegalArgumentException("必须指定postId"));

        Optional.ofNullable(comment.getStr("content")).map(content -> Validator.validateMatchRegex(".{1,65535}", content, "content长度必须在1-65535之间")).map(SensitiveUtils::getAllSensitive).map(sensitiveWords -> Validator.validateTrue(sensitiveWords.isEmpty(), "content中包含敏感词：" + sensitiveWords)).orElseThrow(() -> new IllegalArgumentException("必须指定content"));

        Validator.validateNull(comment.get("commentId"), "commentId默认为自增，不需要指定");
        Validator.validateNull(comment.get("releaseTime"), "releaseTime默认为当前时间，不需要指定");
        Validator.validateNull(comment.get("likeNum"), "likeNum默认为0，不需要指定");
//        Validator.validateNull(comment.get("replayCommentId"), "replayCommentId默认为0，不需要指定");
    }
}